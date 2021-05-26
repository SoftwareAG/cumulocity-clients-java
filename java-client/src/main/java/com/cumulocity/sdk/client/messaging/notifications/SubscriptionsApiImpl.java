package com.cumulocity.sdk.client.messaging.notifications;

import com.cumulocity.rest.representation.CumulocityMediaType;
import com.cumulocity.sdk.client.PlatformParameters;
import com.cumulocity.sdk.client.RestConnector;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import static java.util.Objects.requireNonNull;

@Slf4j
@RequiredArgsConstructor
public class SubscriptionsApiImpl implements SubscriptionsApi {
    public static final CumulocityMediaType MEDIA_TYPE = new CumulocityMediaType("application", "json");

    public static final String REQUEST_URI = "reliablenotification/subscriptions";

    private final PlatformParameters platformParameters;
    private final RestConnector restConnector;
    private final static String DEVICE_ID_MARKER = "DEVICE_ID";
    private final static String SUBSCRIPTION_NAME_MARKER = "SUBSCRIPTION_NAME";

    @Override
    public Subscription subscribeToDevice(SubscriptionDescription description) {
        requireNonNull(description, "description");
        Subscription result = restConnector.post(
                getRequestUri(),
                MEDIA_TYPE,
                MEDIA_TYPE,
                description,
                Subscription.class
                );
        return result;
    }

    private String getRequestUri() {
        return platformParameters.getHost() + REQUEST_URI;
    }

    @Override
    public Subscription getSubscriptionById(String id) {
        requireNonNull(id, "id");
        Subscription result = restConnector.get(getRequestUri() + "/" + id, MEDIA_TYPE, Subscription.class);
        return result;
    }
    
    @Override
    public SubscriptionPage getSubscriptionsBySource(String deviceId) {
        requireNonNull(deviceId, "deviceId");
        SubscriptionPage result = restConnector.get(getRequestUri() + "?source=" + deviceId, MEDIA_TYPE, SubscriptionPage.class);
        return result;
    }
    
    @Override
    public SubscriptionPage getSubscriptionsBySource(String deviceId, int page, int pageSize) {
        requireNonNull(deviceId, "deviceId");
        SubscriptionPage result = restConnector.get(getRequestUri() + "?source=" + deviceId + "&page=" + page + "&pageSize=" + pageSize, MEDIA_TYPE, SubscriptionPage.class);
        return result;
    }
    
    @Override
    public void deleteSubscriptionById(String subscriptionId) {
        requireNonNull(subscriptionId, "subscriptionId");
        restConnector.delete(getRequestUri() + "/" + subscriptionId);
    }

    @Override
    public void deleteSubscriptionBySource(String deviceId) {
        requireNonNull(deviceId, "deviceId");
        restConnector.delete(getRequestUri() + "?source=" + deviceId);
    }
}
