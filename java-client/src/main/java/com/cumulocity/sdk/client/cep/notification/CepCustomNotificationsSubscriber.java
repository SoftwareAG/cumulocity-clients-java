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
