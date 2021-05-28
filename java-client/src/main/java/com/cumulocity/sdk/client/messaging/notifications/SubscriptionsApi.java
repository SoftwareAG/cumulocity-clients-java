package com.cumulocity.sdk.client.messaging.notifications;

import com.cumulocity.rest.representation.reliable.notification.NotificationSubscriptionRepresentation;
import com.cumulocity.rest.representation.reliable.notification.NotificationSubscriptionCollectionRepresentation;

public interface SubscriptionsApi {

    NotificationSubscriptionRepresentation subscribe(NotificationSubscriptionRepresentation description);
    
    NotificationSubscriptionRepresentation getById(String id);
    
    NotificationSubscriptionCollectionRepresentation getBySource(String deviceId);

    NotificationSubscriptionCollectionRepresentation getBySource(String deviceId, int page, int pageSize);
    
    void deleteById(String subscriptionId);
    
    void deleteBySource(String deviceId);
}
