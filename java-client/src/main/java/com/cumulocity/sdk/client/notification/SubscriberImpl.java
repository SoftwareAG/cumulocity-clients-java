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

import com.cumulocity.sdk.client.SDKException;
import org.cometd.bayeux.Channel;
import org.cometd.bayeux.Message;
import org.cometd.bayeux.Message.Mutable;
import org.cometd.bayeux.client.ClientSession;
import org.cometd.bayeux.client.ClientSession.Extension;
import org.cometd.bayeux.client.ClientSessionChannel;
import org.cometd.bayeux.client.ClientSessionChannel.MessageListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.CopyOnWriteArraySet;

import static com.cumulocity.sdk.client.notification.SubscribeOperationRetryPolicy.Mode.AUTO;

class SubscriberImpl<T> implements Subscriber<T, Message>, ConnectionListener {

    private static final Logger log = LoggerFactory.getLogger(SubscriberImpl.class);

    private final SubscriptionNameResolver<T> subscriptionNameResolver;

    private final BayeuxSessionProvider bayeuxSessionProvider;

    private final Collection<SubscriptionRecord> subscriptions = new CopyOnWriteArraySet<SubscriptionRecord>();

    private final Collection<SubscriptionRecord> pendingSubscriptions = new CopyOnWriteArraySet<SubscriptionRecord>();

    private final Object lock = new Object();

    private volatile ClientSession session;

    public SubscriberImpl(SubscriptionNameResolver<T> channelNameResolver, BayeuxSessionProvider bayeuxSessionProvider, final UnauthorizedConnectionWatcher unauthorizedConnectionWatcher) {
        this.subscriptionNameResolver = channelNameResolver;
        this.bayeuxSessionProvider = bayeuxSessionProvider;
        unauthorizedConnectionWatcher.addListener(this);
    }

    public void start() throws SDKException {
        log.trace("starting new subscriber");
        checkState(!isConnected(), "subscriber already started");
        session = bayeuxSessionProvider.get();
    }

    private boolean isSuccessfulHandshake(Mutable message) {
        return ClientSessionChannel.META_HANDSHAKE.equals(message.getChannel()) && message.isSuccessful();
    }

    public Subscription<T> subscribe(T object,
                                     final SubscribeOperationListener subscribeOperationListener,
                                     final SubscriptionListener<T, Message> handler,
                                     final SubscribeOperationRetryPolicy retryPolicy) throws SDKException {
        checkArgument(object != null, "object can't be null");
        checkArgument(handler != null, "handler can't be null");
        checkArgument(handler != null, "subscribeOperationListener can't be null");
        checkArgument(handler != null, "retryPolicy can't be null");

        ensureConnection();
        final ClientSessionChannel channel = getChannel(object);
        log.debug("subscribing to channel {}", channel.getId());
        final MessageListenerAdapter listener = new MessageListenerAdapter(handler, channel, object);
        final ClientSessionChannel metaSubscribeChannel = session.getChannel(ClientSessionChannel.META_SUBSCRIBE);
        SubscriptionRecord subscriptionRecord = new SubscriptionRecord(object, handler);
        SubscriptionResultListener subscriptionResultListener = new SubscriptionResultListener(
                subscriptionRecord, listener, subscribeOperationListener, channel, retryPolicy);
        metaSubscribeChannel.addListener(subscriptionResultListener);
        channel.subscribe(listener);
        if (AUTO.equals(retryPolicy.getMode())) {
            pendingSubscriptions.add(subscriptionRecord);
        }

        return listener.getSubscription();
    }

    public Subscription<T> subscribe(T object, final SubscriptionListener<T, Message> handler) throws SDKException {
        return this.subscribe(object, new LoggingSubscribeOperationListener(), handler, SubscribeOperationRetryPolicy.semi(1));
    }

    private void ensureConnection() {
        synchronized (lock) {
            if (!isConnected()) {
                start();
                session.addExtension(new ReconnectOnSuccessfulHandshake());
            }
        }
    }

    private boolean isConnected() {
        return session != null;
    }

    private ClientSessionChannel getChannel(final T object) {
        final String channelId = subscriptionNameResolver.apply(object);
        checkState(channelId != null && channelId.length() > 0, "channelId is null or empty for object : " + object);
        return session.getChannel(channelId);
    }

    @Override
    public void disconnect() {
        synchronized (lock) {
            if (isConnected()) {
                subscriptions.clear();
                session.disconnect();
                session = null;
            }
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

    private void resubscribe() {
        Set<SubscriptionRecord> allSubscriptions = new HashSet<>(subscriptions);
        allSubscriptions.addAll(pendingSubscriptions);
        for (SubscriptionRecord subscribed : allSubscriptions) {
            Subscription<T> subscription = subscribe(subscribed.getId(), subscribed.getListener());
            subscribed.getListener().onError(subscription, new ReconnectedSDKException("bayeux client reconnected clientId: " + session.getId()));
        }
    }

    @Override
    public void onDisconnection(final int httpCode) {
        for (final SubscriptionRecord subscription : subscriptions) {
            subscription.getListener().onError(new DummySubscription(subscription), new SDKException(httpCode, "bayeux client disconnected  clientId: " + session.getId()));
        }
    }

    public final class ReconnectOnSuccessfulHandshake implements Extension {
        @Override
        public boolean sendMeta(ClientSession session, Mutable message) {
            return true;
        }

        @Override
        public boolean send(ClientSession session, Mutable message) {
            return true;
        }

        @Override
        public boolean rcvMeta(ClientSession session, Mutable message) {
            if (isSuccessfulHandshake(message)) {
                log.debug("reconnect operation detected for session {} - {} ", bayeuxSessionProvider, session.getId());
                resubscribe();
            }
            return true;
        }

        @Override
        public boolean rcv(ClientSession session, Mutable message) {
            return true;
        }
    }

    private final class UnsubscribeListener implements MessageListener {
        private final SubscriptionRecord subscribed;
        private final ClientSessionChannel unsubscribeChannel;

        public UnsubscribeListener(SubscriptionRecord subscribed, ClientSessionChannel unsubscribeChannel) {
            this.subscribed = subscribed;
            this.unsubscribeChannel = unsubscribeChannel;
        }

        @Override
        public void onMessage(ClientSessionChannel channel, Message message) {
            if (subscriptionNameResolver.apply(subscribed.getId()).equals(message.get(Message.SUBSCRIPTION_FIELD))
                    && message.isSuccessful()) {
                try {
                    log.debug("unsubscribed successfully from channel {}", channel.getId());
                    subscribed.remove();
                } finally {
                    unsubscribeChannel.removeListener(this);
                }
            }
        }
    }

    public static class LoggingSubscribeOperationListener implements SubscribeOperationListener {

        private static final Logger LOG = LoggerFactory.getLogger(LoggingSubscribeOperationListener.class);

        @Override
        public void onSubscribingSuccess(String channelId) {
            LOG.info("Successfully subscribed: {}", channelId);
        }

        @Override
        public void onSubscribingError(String channelId, String message, Throwable throwable) {
            LOG.error("Error when subscribing channel: {}, error: {}", channelId, message, throwable);
        }
    }

    private final class SubscriptionResultListener implements MessageListener {

        private final SubscribeOperationListener subscribeOperationListener;

        private final MessageListenerAdapter listener;

        private final ClientSessionChannel channel;

        private final SubscriptionRecord subscription;

        private final SubscribeOperationRetryPolicy retryPolicy;

        private SubscriptionResultListener(SubscriptionRecord subscribed, MessageListenerAdapter listener,
                                           SubscribeOperationListener subscribeOperationListener,
                                           ClientSessionChannel channel,
                                           SubscribeOperationRetryPolicy retryPolicy) {
            this.subscription = subscribed;
            this.listener = listener;
            this.subscribeOperationListener = subscribeOperationListener;
            this.channel = channel;
            this.retryPolicy = retryPolicy;
        }

        @Override
        public void onMessage(ClientSessionChannel metaSubscribeChannel, Message message) {
            if (!Channel.META_SUBSCRIBE.equals(metaSubscribeChannel.getId())) {
                // Should never be here
                log.warn("Unexpected message to wrong channel, to SubscriptionSuccessListener: {}, {}", metaSubscribeChannel, message);
                return;
            }
            if (message.isSuccessful() && !isSubscriptionToChannel(message)) {
                return;
            }
            try {
                if (message.isSuccessful()) {
                    log.debug("subscribed successfully to channel {}, {}", this.channel, message);
                    pendingSubscriptions.remove(subscription);
                    ClientSessionChannel unsubscribeChannel = session.getChannel(ClientSessionChannel.META_UNSUBSCRIBE);
                    unsubscribeChannel.addListener(new UnsubscribeListener(subscription, unsubscribeChannel));
                    subscriptions.add(subscription);
                    subscribeOperationListener.onSubscribingSuccess(this.channel.getId());
                } else {
                    log.debug("Error subscribing channel: {}, {}", this.channel.getId(), message);
                    handleError(message);
                }
            } catch (NullPointerException ex) {
                log.warn("NPE on message {} - {}", message, Channel.META_SUBSCRIBE);
                throw new RuntimeException(ex);
            } finally {
                metaSubscribeChannel.removeListener(this);
            }
        }

        private boolean isSubscriptionToChannel(Message message) {
            return Objects.equals(channel.getId(), message.get(Message.SUBSCRIPTION_FIELD));
        }

        private void handleError(Message message) {
            handleRetryIfPossible();
            informSubscribingListener(message);
        }

        private void informSubscribingListener(Message message) {
            String errorMessage = "Unknow error (unspecified by server)";
            Throwable throwable = null;
            Object error = message.get(Message.ERROR_FIELD);
            if (error == null) {
                error = message.get("failure");
                if (error != null && error instanceof Map) {
                    throwable = (Throwable) ((Map) error).get("exception");
                    if (throwable != null) {
                        errorMessage = throwable.getMessage();
                    }
                }
            } else {
                errorMessage = (String) error;
            }
            subscribeOperationListener.onSubscribingError(channel.getId(), errorMessage, throwable);
        }

        private void handleRetryIfPossible() {
            int maxRetries = retryPolicy.getRetries();
            if (maxRetries > 0) {
                log.warn("Retrying... to subscribe to channel {}, remaining possible retries: {}",
                        channel.getId(), maxRetries - 1);
                // This does not sound right when the subscribe operation has been failed, but the fact is the subscription
                // is already there in the channel and nothing will be added if we subscribe again
                channel.unsubscribe(listener);
                subscribe(subscription.getId(), subscribeOperationListener, listener.handler, retryPolicy.useOnce());
            } else {
                if(AUTO.equals(retryPolicy.getMode())) {
                    log.info("Subscribe operation failed for channel: {}, will retry automatically on reconnect!", channel.getId());
                } else {
                    log.error("Failed to subscribe channel: {} and retry policy is 0 or maximum number of retries reached", channel.getId());
                }
            }
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
            log.debug("unsubscribing from channel {}", channel.getId());
            channel.unsubscribe(listener);
        }

        @Override
        public T getObject() {
            return object;
        }
    }

    private final class DummySubscription implements Subscription<T> {

        private final SubscriptionRecord subscription;

        DummySubscription(final SubscriptionRecord subscription) {
            this.subscription = subscription;
        }

        @Override
        public void unsubscribe() {
        }

        @Override
        public T getObject() {
            return subscription.getId();
        }
    }

    private final class MessageListenerAdapter implements MessageListener {
        private final SubscriptionListener<T, Message> handler;

        private final Subscription<T> subscription;

        MessageListenerAdapter(SubscriptionListener<T, Message> handler, ClientSessionChannel channel, T object) {
            this.handler = handler;
            subscription = createSubscription(channel, object);
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
