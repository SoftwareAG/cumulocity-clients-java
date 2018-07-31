package com.cumulocity.microservice.subscription.service;

import com.cumulocity.microservice.context.credentials.MicroserviceCredentials;
import com.google.common.base.Optional;

import java.util.Collection;
import java.util.concurrent.Callable;

/**
 * 
 * This service gets microservice subscriptions and notifies listeners for MicroserviceSubscription*Event events.
 *
 */
public interface MicroserviceSubscriptionsService {

    /**
     * Returns current tenant.
     */
    String getTenant();

    /**
     * Invokes runnable in context of all subscribed tenants.
     */
    void runForEachTenant(Runnable runnable);

    /**
     * Invokes runnable in context of subscribed tenant.
     */
    void runForTenant(String tenant, Runnable runnable);

    /**
     * Invokes runnable in context of subscribed tenant.
     */
    <T> T callForTenant(String tenant, Callable<T> runnable);

    /**
     * Fetches microservice subscriptions and emits MicroserviceSubscriptionRemovedEvent, 
     * MicroserviceSubscriptionAddedEvent to the MicroserviceChangedListener listeners
     * <p>
     * If a listener throws exception during event execution, the event is created again in 
     * the next call to the method.
     * </p>
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

    boolean isSubscribed();
}
