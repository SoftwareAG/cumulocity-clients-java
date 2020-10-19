package com.cumulocity.microservice.subscription.service;

import com.cumulocity.microservice.context.credentials.MicroserviceCredentials;
import java.util.Optional;

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
     * @return current tenant id
     */
    String getTenant();

    /**
     * Invokes runnable in context of all subscribed tenants.
     * @param runnable runnable to execute
     */
    void runForEachTenant(Runnable runnable);

    /**
     * Invokes runnable in context of subscribed tenant.
     * @param tenant tenant id
     * @param runnable runnable to execute
     */
    void runForTenant(String tenant, Runnable runnable);

    /**
     * Invokes runnable in context of subscribed tenant.
     * @param tenant tenant id
     * @param runnable callable to execute
     * @param <T> generic type of expected result object
     * @return result object
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
     * @param tenant tenant id
     * @return  <code>Optional.of</code> microservice credentials if tenant is found in current subscriptions;
     *          <code>Optional.&lt;MicroserviceCredentials&gt;empty()</code> otherwise
     */
    Optional<MicroserviceCredentials> getCredentials(String tenant);

    boolean isRegisteredSuccessfully();
}
