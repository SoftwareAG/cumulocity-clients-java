package com.cumulocity.sdk.client.notification;

import static com.cumulocity.sdk.client.notification.DefaultBayeuxClientProvider.createProvider;

import com.cumulocity.sdk.client.PlatformParameters;

public class SubscriberBuilder<T, R> {

    private String endpoint;

    private Class<R> dataType;

    private PlatformParameters parameters;

    private SubscriptionNameResolver<T> subscriptionNameResolver;

    public static <T, R> SubscriberBuilder<T, R> anSubscriber() {
        return new SubscriberBuilder<T, R>();
    }

    public SubscriberBuilder<T, R> withEndpoint(String endpoint) {
        this.endpoint = endpoint;
        return this;
    }

    public SubscriberBuilder<T, R> withDataType(Class<R> dataType) {
        this.dataType = dataType;
        return this;
    }

    public SubscriberBuilder<T, R> withParameters(PlatformParameters parameters) {
        this.parameters = parameters;
        return this;
    }

    public SubscriberBuilder<T, R> withSubscriptionNameResolver(SubscriptionNameResolver<T> subscriptionNameResolver) {
        this.subscriptionNameResolver = subscriptionNameResolver;
        return this;
    }

    public Subscriber<T, R> build() {
        verifyRequiredFields();
        return new TypedSubscriber<T, R>(new SubscriberImpl<T>(subscriptionNameResolver, createSessionProvider()), dataType);
    }

    private void verifyRequiredFields() {
        checkNotNull(endpoint, "endpoint can't be null");
        checkNotNull(parameters, "plantform parameters can't be null");
        checkNotNull(subscriptionNameResolver, "subscriptionNameResolver can't be null");
        checkNotNull(dataType, "dataType can't be null");
    }

    private void checkNotNull(Object value, String message) {
        if (value == null) {
            throw new IllegalStateException(message);
        }
    }

    private BayeuxSessionProvider createSessionProvider() {
        return createProvider(endpoint, parameters, dataType);
    }

}
