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

    private final ChannelNameResolver<T> channelNameResolver;

    private final BayeuxClientProvider bayeuxClientProvider;

    private BayeuxClient client;

    public SubscriberImpl(String url, ChannelNameResolver<T> channelNameResolver) {
        this(url, channelNameResolver, BayeuxClientProvider.getInstance());
    }

    public SubscriberImpl(String url, ChannelNameResolver<T> channelNameResolver, BayeuxClientProvider bayeuxClientProvider) {
        this.url = url;
        this.channelNameResolver = channelNameResolver;
        this.bayeuxClientProvider = bayeuxClientProvider;
    }

    @Override
    public void connect() {
        client = bayeuxClientProvider.get(url);
        client.handshake();
        boolean handshake = client.waitFor(TimeUnit.SECONDS.toMillis(10),State.CONNECTED);
        checkState(handshake, "unable to connect to server");
    }
    

    @Override 
    public Subscription subscribe(T object, NotificationListener handler) {
        checkArgument(object != null, "object can't be null");
        checkArgument(handler != null, "handler can't be null");
        checkState(client != null, "subscriber not connected to server");
        
        final ClientSessionChannel channel = getChannel(object);
        final MessageListenerAdapter listener = new MessageListenerAdapter(handler);
        channel.subscribe(listener);
        return new ChannelSubscription(listener, channel);
    }

    private ClientSessionChannel getChannel(final T object) {
        final String channelId = channelNameResolver.apply(object);
        checkState(channelId != null && channelId.length() > 0, "channalId is null or empty for object : "+object);
        return client.getChannel(channelId);
    }

    @Override
    public void disconnect() {
        checkState(client != null, "subscriber not connected to server");
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
