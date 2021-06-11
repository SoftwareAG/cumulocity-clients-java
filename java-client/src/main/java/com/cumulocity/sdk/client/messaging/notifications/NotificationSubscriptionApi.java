package com.cumulocity.sdk.client.messaging.notifications;

import com.cumulocity.rest.representation.reliable.notification.NotificationSubscriptionRepresentation;
import com.cumulocity.sdk.client.SDKException;

public interface NotificationSubscriptionApi {

    NotificationSubscriptionRepresentation subscribe(NotificationSubscriptionRepresentation representation) throws SDKException;
    
    NotificationSubscriptionCollection getSubscriptions() throws SDKException;

    NotificationSubscriptionCollection getSubscriptionsByFilter(NotificationSubscriptionFilter filter) throws SDKException;

    void delete(NotificationSubscriptionRepresentation subscription) throws SDKException;
}
