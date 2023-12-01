package com.cumulocity.sdk.client.notification;

import com.cumulocity.sdk.client.PlatformParameters;
import com.cumulocity.sdk.client.SDKException;

public class RealtimeNotificationSubscriber<T> implements Subscriber<String, T> {

    private final Subscriber<String, T> subscriber;

    public RealtimeNotificationSubscriber(PlatformParameters parameters, Class<T> type) {
        subscriber = createSubscriber(parameters, type);
    }

    private Subscriber<String, T> createSubscriber(PlatformParameters parameters, Class<T> type) {
        return SubscriberBuilder.<String, T>anSubscriber()
                .withParameters(parameters)
                .withEndpoint(SubscriberBuilder.REALTIME)
                .withSubscriptionNameResolver(name -> name)
                .withDataType(type)
                .build();
    }

    public Subscription<String> subscribe(final String deviceId, final SubscriptionListener<String, T> handler)
            throws SDKException {
        return subscriber.subscribe(deviceId, handler);
    }

    @Override
    public Subscription<String> subscribe(final String deviceId, SubscriptionListener<String, T> handler, int numberOfMaxRetries) throws SDKException {
        return subscriber.subscribe(deviceId, handler);
    }

    @Override
    public Subscription<String> subscribe(String deviceId,
                                          SubscribeOperationListener subscribeOperationListener,
                                          SubscriptionListener<String, T> handler,
                                          boolean autoRetry) throws SDKException {
        return subscriber.subscribe(deviceId, subscribeOperationListener, handler, autoRetry);
    }

    public void disconnect() {
        subscriber.disconnect();
    }
}
