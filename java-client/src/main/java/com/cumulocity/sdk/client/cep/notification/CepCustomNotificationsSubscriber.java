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
package com.cumulocity.sdk.client.cep.notification;

import com.cumulocity.sdk.client.PlatformParameters;
import com.cumulocity.sdk.client.SDKException;
import com.cumulocity.sdk.client.notification.*;

public class CepCustomNotificationsSubscriber implements Subscriber<String, Object> {
    
    public static final String CEP_CUSTOM_NOTIFICATIONS_URL = "cep/customnotifications";

    private final Subscriber<String, Object> subscriber;

    public CepCustomNotificationsSubscriber(PlatformParameters parameters) {
        subscriber = createSubscriber(parameters);
    }

    private Subscriber<String, Object> createSubscriber(PlatformParameters parameters) {
        // @formatter:off
        return SubscriberBuilder.<String, Object>anSubscriber()
                    .withParameters(parameters)
                    .withEndpoint(CEP_CUSTOM_NOTIFICATIONS_URL)
                    .withSubscriptionNameResolver(new Identity())
                    .withDataType(Object.class)
                    .build();
        // @formatter:on
    }

    public Subscription<String> subscribe(final String channelID, final SubscriptionListener<String, Object> handler) throws SDKException {
        return subscriber.subscribe(channelID, handler);
    }

    @Override
    public Subscription<String> subscribe(String channelID, SubscribeOperationListener subscribeOperationListener,
                                       SubscriptionListener<String, Object> handler,
                                       SubscribingRetryPolicy retryPolicy) throws SDKException {
        return subscriber.subscribe(channelID, subscribeOperationListener, handler, retryPolicy);
    }

    public void disconnect() {
        subscriber.disconnect();
    }

    private static final class Identity implements SubscriptionNameResolver<String> {
        @Override
        public String apply(String id) {
            return id;
        }
    }
}
