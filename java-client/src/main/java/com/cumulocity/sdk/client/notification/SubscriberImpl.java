/*
 * Copyright (C) 2013 Cumulocity GmbH
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of 
 * this software and associated documentation files (the "Software"),
 * to deal in the Software without restriction, including without limitation the rights to use,
 * copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software,
 * and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
 * OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE,
 * ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.cumulocity.sdk.client.notification;

import java.util.concurrent.TimeUnit;

import org.cometd.bayeux.client.ClientSessionChannel;
import org.cometd.client.BayeuxClient;
import org.cometd.client.BayeuxClient.State;

public class SubscriberImpl<T> implements Subscriber<T> {

    private final String url;

    private final SubscriptionNameResolver<T> subscriptionNameResolver;

    private final BayeuxClientProvider bayeuxClientProvider;

    private volatile BayeuxClient client;

    public SubscriberImpl(String url, SubscriptionNameResolver<T> channelNameResolver) {
        this(url, channelNameResolver, BayeuxClientProvider.getInstance());
    }

    public SubscriberImpl(String url, SubscriptionNameResolver<T> channelNameResolver, BayeuxClientProvider bayeuxClientProvider) {
        this.url = url;
        this.subscriptionNameResolver = channelNameResolver;
        this.bayeuxClientProvider = bayeuxClientProvider;
    }

    @Override
    public void start() {
        checkState(!isConnected(), "subscriber already started");
        BayeuxClient client = bayeuxClientProvider.get(url);
        client.handshake();
        boolean handshake = client.waitFor(TimeUnit.SECONDS.toMillis(10), State.CONNECTED);
        checkState(handshake, "unable to connect to server");
        this.client = client;
    }

    public Subscription<T> subscribe(T object, SubscriptionListener<T> handler) {

        checkArgument(object != null, "object can't be null");
        checkArgument(handler != null, "handler can't be null");
        checkState(isConnected(), "subscriber not connected to server, invoke start first");

        final ClientSessionChannel channel = getChannel(object);
        final MessageListenerAdapter<T> listener = new MessageListenerAdapter<T>(handler, channel, object);
        channel.subscribe(listener);
        return listener.getSubscription();
    }

    private boolean isConnected() {
        return client != null;
    }

    private ClientSessionChannel getChannel(final T object) {
        final String channelId = subscriptionNameResolver.apply(object);
        checkState(channelId != null && channelId.length() > 0, "channalId is null or empty for object : " + object);
        return client.getChannel(channelId);
    }

    @Override
    public void stop() {
        checkState(isConnected(), "subscriber not connected to server, invoke start first");
        client.disconnect(10000);
        client = null;
    }

    private final void checkState(boolean state, String message) {
        if (!state) {
            throw new IllegalStateException(message);
        }
    }

    private final void checkArgument(boolean state, String message) {
        if (!state) {
            throw new IllegalArgumentException(message);
        }
    }

}
