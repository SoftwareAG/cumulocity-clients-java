package com.cumulocity.sdk.client.notification;

import org.cometd.bayeux.Message;

import com.cumulocity.sdk.client.SDKException;

class TypedSubscriber<T, R> implements Subscriber<T, R> {

    private final Subscriber<T, Message> subscriber;

    private final Class<R> dataType;

    public TypedSubscriber(Subscriber<T, Message> subscriber, Class<R> dataType) {
        this.subscriber = subscriber;
        this.dataType = dataType;
    }

    public Subscription<T> subscribe(T object, SubscriptionListener<T, R> handler) throws SDKException {
        return subscriber.subscribe(object, new MappingSubscriptionListener<T, R>(handler, dataType));
    }

    public void disconnect() {
        subscriber.disconnect();
    }

    private static final class MappingSubscriptionListener<T, R> implements SubscriptionListener<T, Message> {
        private final SubscriptionListener<T, R> handler;

        private final Class<R> dataType;

        private MappingSubscriptionListener(SubscriptionListener<T, R> handler, Class<R> dataType) {
            this.handler = handler;
            this.dataType = dataType;
        }

        @Override
        public void onNotification(Subscription<T> subscription, Message notification) {
            final R data = asDataType(notification);
            handler.onNotification(subscription, data);
        }

        private R asDataType(Message notification) {
            final Object data = notification.getData();
            return data != null && dataType.isAssignableFrom(data.getClass()) ? dataType.cast(data) : null;
        }

        @Override
        public void onError(Subscription<T> subscription, Throwable ex) {
            handler.onError(subscription, ex);
        }
    }
}
