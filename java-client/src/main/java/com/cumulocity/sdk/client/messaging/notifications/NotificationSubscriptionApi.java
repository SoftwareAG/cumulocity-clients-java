package com.cumulocity.sdk.client.messaging.notifications;

import com.cumulocity.rest.representation.reliable.notification.NotificationSubscriptionRepresentation;
import com.cumulocity.sdk.client.SDKException;

/**
 * Manage notification subscriptions
 */
public interface NotificationSubscriptionApi {

    /**
     * Creates a subscription to a source.
     * 
     * @param representation initial values for subscription
     * @return subscription populated with an id
     * @throws SDKException 
     */
    NotificationSubscriptionRepresentation subscribe(NotificationSubscriptionRepresentation representation) throws SDKException;
    
    /**
     * Gets all the subscriptions.
     * 
     * @return all the subscriptions
     * @throws SDKException 
     */
    NotificationSubscriptionCollection getSubscriptions() throws SDKException;

    /**
     * Gets all the subscriptions matching a filter. If the filter is null,
     * return all subscriptions.
     * 
     * @param filter values to be matched on
     * @return subscriptions matching values
     * @throws SDKException 
     */
    NotificationSubscriptionCollection getSubscriptionsByFilter(NotificationSubscriptionFilter filter) throws SDKException;
    
    /**
     * Delete by object.
     * 
     * @param subscription to delete
     * @throws SDKException 
     */
    void delete(NotificationSubscriptionRepresentation subscription) throws SDKException;
    
    /**
     * Delete by ID.
     * 
     * @param subscriptionId of subscription to delete
     * @throws SDKException 
     */
    void deleteById(String subscriptionId) throws SDKException;
    
    /**
     * Deletes all subscriptions matching a filter.
     * 
     * @param filter 
     */    
    void deleteByFilter(NotificationSubscriptionFilter filter);
    
    /**
     * Deletes all subscriptions to a source.
     * 
     * @param source 
     */
    void deleteBySource(String source);
}
