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
import org.cometd.bayeux.Message;

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

    @Override
    public Subscription<T> subscribe(T agentId, SubscribeOperationListener subscribeOperationListener,
                                       SubscriptionListener<T, R> handler,
                                       SubscribeOperationRetryPolicy retryPolicy) throws SDKException {
        return subscriber.subscribe(agentId, subscribeOperationListener,
                new MappingSubscriptionListener<T, R>(handler, dataType), retryPolicy);
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
