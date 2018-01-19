package com.cumulocity.microservice.subscription.service;

/**
 * Manages microservice-credentials contexts.
 */
public interface MicroserviceContextService {
    /**
     * Invokes runnable in context of all tenants.
     */
    void runForEachTenant(Runnable runnable);

    /**
     * Invokes runnable in context of tenant.
     */
    void runForTenant(String tenant, Runnable runnable);
}
