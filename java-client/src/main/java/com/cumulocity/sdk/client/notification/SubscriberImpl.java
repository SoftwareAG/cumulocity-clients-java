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

import org.cometd.bayeux.Message;
import org.cometd.bayeux.client.ClientSession;
import org.cometd.bayeux.client.ClientSessionChannel;
import org.cometd.bayeux.client.ClientSessionChannel.MessageListener;

import com.cumulocity.sdk.client.SDKException;

class SubscriberImpl<T> implements Subscriber<T, Message> {

    private final SubscriptionNameResolver<T> subscriptionNameResolver;

    private final BayeuxSessionProvider bayeuxSessionProvider;

    private volatile ClientSession session;

    public SubscriberImpl(SubscriptionNameResolver<T> channelNameResolver, BayeuxSessionProvider bayeuxSessionProvider) {
        this.subscriptionNameResolver = channelNameResolver;
        this.bayeuxSessionProvider = bayeuxSessionProvider;
    }

    public void start() throws SDKException {
        checkState(!isConnected(), "subscriber already started");
        session = bayeuxSessionProvider.get();
    }

    public Subscription<T> subscribe(T object, final SubscriptionListener<T, Message> handler) throws SDKException {

        checkArgument(object != null, "object can't be null");
        checkArgument(handler != null, "handler can't be null");

        if (!isConnected()) {
            start();
        }

        final ClientSessionChannel channel = getChannel(object);
        final MessageListenerAdapter<T> listener = new MessageListenerAdapter<T>(handler, channel, object);
        final ClientSessionChannel subscribeChannel = session.getChannel(ClientSessionChannel.META_SUBSCRIBE);

        subscribeChannel.addListener(new SubscriptionSuccessListener(listener, handler, subscribeChannel));
        channel.subscribe(listener);

        return listener.getSubscription();
    }

    private boolean isConnected() {
        return session != null;
    }

    private ClientSessionChannel getChannel(final T object) {
        final String channelId = subscriptionNameResolver.apply(object);
        checkState(channelId != null && channelId.length() > 0, "channalId is null or empty for object : " + object);
        return session.getChannel(channelId);
    }

    @Override
    public void disconnect() {
        if (isConnected()) {
            session.disconnect();
            session = null;
        }
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

    private final class SubscriptionSuccessListener implements MessageListener {
        private final MessageListenerAdapter<T> listener;

        private final SubscriptionListener<T, Message> handler;

        private final ClientSessionChannel subscribeChannel;

        private SubscriptionSuccessListener(MessageListenerAdapter<T> listener, SubscriptionListener<T, Message> handler,
                ClientSessionChannel subscribeChannel) {
            this.listener = listener;
            this.handler = handler;
            this.subscribeChannel = subscribeChannel;
        }

        @Override
        public void onMessage(ClientSessionChannel channel, Message message) {
            try {
                if (channel.getChannelId().equals(message.get(Message.SUCCESSFUL_FIELD)) && !message.isSuccessful()) {
                    handler.onError(listener.getSubscription(), new SDKException("unable to subscribe on Channel"));
                }
            } finally {
                subscribeChannel.removeListener(this);
            }
        }
    }

}
