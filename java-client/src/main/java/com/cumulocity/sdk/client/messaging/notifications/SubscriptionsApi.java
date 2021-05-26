package com.cumulocity.sdk.client.messaging.notifications;

public interface SubscriptionsApi {

    Subscription subscribeToDevice(SubscriptionDescription description);
    
    Subscription getSubscriptionById(String id);
    
    SubscriptionPage getSubscriptionsBySource(String deviceId);

    SubscriptionPage getSubscriptionsBySource(String deviceId, int page, int pageSize);
    
    void deleteSubscriptionById(String subscriptionId);
    
    void deleteSubscriptionBySource(String deviceId);
}
