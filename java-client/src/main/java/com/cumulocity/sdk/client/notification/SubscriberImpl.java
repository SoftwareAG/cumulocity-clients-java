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

import java.util.Collection;
import java.util.concurrent.CopyOnWriteArraySet;

import org.cometd.bayeux.Message;
import org.cometd.bayeux.client.ClientSession;
import org.cometd.bayeux.client.ClientSessionChannel;
import org.cometd.bayeux.client.ClientSessionChannel.MessageListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cumulocity.sdk.client.SDKException;

class SubscriberImpl<T> implements Subscriber<T, Message> {

    private static final Logger log = LoggerFactory.getLogger(SubscriberImpl.class);

    private final SubscriptionNameResolver<T> subscriptionNameResolver;

    private final BayeuxSessionProvider bayeuxSessionProvider;

    private final Collection<SubscriptionRecord> subscriptions = new CopyOnWriteArraySet<SubscriptionRecord>();

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
        ensureConnection();
        final ClientSessionChannel channel = getChannel(object);
        final MessageListenerAdapter listener = new MessageListenerAdapter(handler, channel, object);
        final ClientSessionChannel metaSubscribeChannel = session.getChannel(ClientSessionChannel.META_SUBSCRIBE);
        metaSubscribeChannel.addListener(new SubscriptionSuccessListener(new SubscriptionRecord(object, handler), listener,
                metaSubscribeChannel, channel));
        channel.subscribe(listener);

        return listener.getSubscription();
    }

    private void ensureConnection() {
        synchronized (this) {
            if (!isConnected()) {
                start();
                session.getChannel(ClientSessionChannel.META_HANDSHAKE).addListener(new ReconnectListener());
            }
        }
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
            subscriptions.clear();
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

    private final class ReconnectListener implements MessageListener {
        @Override
        public void onMessage(ClientSessionChannel channel, Message message) {
            for (SubscriptionRecord subscribed : subscriptions) {
                subscribe(subscribed.getId(), subscribed.getListener());
            }
        }
    }

    private final class UnsubscribeListener implements MessageListener {
        private final SubscriptionRecord subscribed;

        public UnsubscribeListener(SubscriptionRecord subscribed) {
            this.subscribed = subscribed;
        }

        @Override
        public void onMessage(ClientSessionChannel channel, Message message) {
            if (subscriptionNameResolver.apply(subscribed.getId()).equals(message.get(Message.SUBSCRIPTION_FIELD))
                    && message.isSuccessful()) {
                subscribed.remove();
            }
        }
    }

    private final class SubscriptionSuccessListener implements MessageListener {

        private final MessageListenerAdapter listener;

        private final ClientSessionChannel metaSubscribeChannel;

        private final ClientSessionChannel channel;

        private final SubscriptionRecord subscription;

        private SubscriptionSuccessListener(SubscriptionRecord subscribed, MessageListenerAdapter listener,
                ClientSessionChannel subscribeChannel, ClientSessionChannel channel) {
            this.subscription = subscribed;
            this.listener = listener;
            this.metaSubscribeChannel = subscribeChannel;
            this.channel = channel;
        }

        @Override
        public void onMessage(ClientSessionChannel channel, Message message) {
            log.info("subscribing to {} - {}", this.channel, message);
            try {
                if (!isSubscriptionToChannel(message))
                    return;
                if (isSuccessfulySubscribed(message)) {
                    session.getChannel(ClientSessionChannel.META_UNSUBSCRIBE).addListener(new UnsubscribeListener(subscription));
                    subscriptions.add(subscription);
                } else {
                    subscription.getListener().onError(
                            listener.getSubscription(),
                            new SDKException("unable to subscribe on Channel " + channel.getChannelId() + " "
                                    + message.get(Message.ERROR_FIELD)));
                }
            } catch (NullPointerException ex) {
                log.warn("NPE on message {} - {}", message, this.channel);
                throw new RuntimeException(ex);
            } finally {
                metaSubscribeChannel.removeListener(this);
            }
        }

        private boolean isSubscriptionToChannel(Message message) {
            return this.channel.getId().equals(message.get(Message.SUBSCRIPTION_FIELD));
        }

        private boolean isSuccessfulySubscribed(Message message) {
            return message.isSuccessful();
        }
    }

    private class ChannelSubscription implements Subscription<T> {
        private final MessageListener listener;

        private final ClientSessionChannel channel;

        private T object;

        ChannelSubscription(MessageListener listener, ClientSessionChannel channel, T object) {
            this.listener = listener;
            this.channel = channel;
            this.object = object;
        }

        @Override
        public void unsubscribe() {
            channel.unsubscribe(listener);
        }

        @Override
        public T getObject() {
            return object;
        }
    }

    private final class MessageListenerAdapter implements MessageListener {
        private final SubscriptionListener<T, Message> handler;

        private final Subscription<T> subscription;

        MessageListenerAdapter(SubscriptionListener<T, Message> handler, ClientSessionChannel channel, T object) {
            this.handler = handler;
            this.subscription = createSubscription(channel, object);
        }

        protected ChannelSubscription createSubscription(ClientSessionChannel channel, T object) {
            return new ChannelSubscription(this, channel, object);
        }

        @Override
        public void onMessage(ClientSessionChannel channel, Message message) {
            handler.onNotification(subscription, message);
        }

        public Subscription<T> getSubscription() {
            return subscription;
        }

    }

    private final class SubscriptionRecord {

        private final T id;

        private final SubscriptionListener<T, Message> listener;

        public SubscriptionRecord(T id, SubscriptionListener<T, Message> listener) {
            this.id = id;
            this.listener = listener;
        }

        public void remove() {
            subscriptions.remove(this);
        }

        public T getId() {
            return id;
        }

        public SubscriptionListener<T, Message> getListener() {
            return listener;
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((id == null) ? 0 : id.hashCode());
            result = prime * result + ((listener == null) ? 0 : listener.hashCode());
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            SubscriptionRecord other = (SubscriptionRecord) obj;
            if (id == null) {
                if (other.id != null)
                    return false;
            } else if (!id.equals(other.id))
                return false;
            if (listener == null) {
                if (other.listener != null)
                    return false;
            } else if (!listener.equals(other.listener))
                return false;
            return true;
        }

    }

}
