package com.cumulocity.sdk.client.messaging.notifications;

import com.cumulocity.rest.representation.CumulocityMediaType;
import com.cumulocity.rest.representation.reliable.notification.NotificationSubscriptionRepresentation;
import com.cumulocity.rest.representation.reliable.notification.NotificationSubscriptionCollectionRepresentation;

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

    @Override
    public NotificationSubscriptionRepresentation subscribe(NotificationSubscriptionRepresentation description) {
        requireNonNull(description, "description");
        NotificationSubscriptionRepresentation result = restConnector.post(
                getRequestUri(),
                MEDIA_TYPE,
                description
                );
        return result;
    }

    @Override
    public NotificationSubscriptionRepresentation getById(String id) {
        requireNonNull(id, "id");
        NotificationSubscriptionRepresentation result = restConnector.get(getRequestUri() + "/" + id, MEDIA_TYPE,
                NotificationSubscriptionRepresentation.class);
        return result;
    }
    
    @Override
    public NotificationSubscriptionCollectionRepresentation getBySource(String deviceId) {
        requireNonNull(deviceId, "deviceId");
        NotificationSubscriptionCollectionRepresentation result = restConnector.get(getRequestUri() + "?source=" + deviceId, MEDIA_TYPE,
                NotificationSubscriptionCollectionRepresentation.class);
        return result;
    }
    
    @Override
    public NotificationSubscriptionCollectionRepresentation getBySource(String deviceId, int page, int pageSize) {
        requireNonNull(deviceId, "deviceId");
        NotificationSubscriptionCollectionRepresentation result =
                restConnector.get(getRequestUri() + "?source=" + deviceId + "&page=" + page + "&pageSize=" + pageSize,
                        MEDIA_TYPE,
                        NotificationSubscriptionCollectionRepresentation.class);
        return result;
    }
    
    @Override
    public void deleteById(String subscriptionId) {
        requireNonNull(subscriptionId, "subscriptionId");
        restConnector.delete(getRequestUri() + "/" + subscriptionId);
    }

    @Override
    public void deleteBySource(String deviceId) {
        requireNonNull(deviceId, "deviceId");
        restConnector.delete(getRequestUri() + "?source=" + deviceId);
    }

    private String getRequestUri() {
        return platformParameters.getHost() + REQUEST_URI;
    }
}
