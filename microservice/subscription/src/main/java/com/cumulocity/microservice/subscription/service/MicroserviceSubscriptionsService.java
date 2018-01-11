package com.cumulocity.microservice.subscription.service;

import com.cumulocity.microservice.context.credentials.MicroserviceCredentials;
import com.google.common.base.Optional;

import java.util.Collection;

/**
 * 
 * This service gets microservice subscriptions and notifies listeners for MicroserviceSubscription*Event events.
 *
 */
public interface MicroserviceSubscriptionsService {

    /**
     * Fetches microservice subscriptions and emits MicroserviceSubscriptionRemovedEvent, 
     * MicroserviceSubscriptionAddedEvent to the MicroserviceChangedListener listeners. 
     */
    void subscribe();

    /**
     * Gets current microservice subscriptions' credentials
     * @return collection of microservice credentials
     */
    Collection<MicroserviceCredentials> getAll();

    /**
     * Gets microservice credentials of the given tenant
     * @param tenant
     * @return  <code>Optional.of</code> microservice credentials if tenant is found in current subscriptions;
     *          <code>Optional.<MicroserviceCredentials>absent()</code> otherwise
     */
    Optional<MicroserviceCredentials> getCredentials(String tenant);

}
