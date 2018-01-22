package com.cumulocity.microservice.subscription.service;

import com.cumulocity.microservice.common.Consumer;
import com.cumulocity.microservice.context.credentials.MicroserviceCredentials;

/**
 * Manages microservice-credentials contexts.
 */
public interface MicroserviceContextService {
    /**
     * Invokes runnable in context of all tenants.
     */
    void runForEachTenant(Consumer<MicroserviceCredentials> runnable);

    /**
     * Invokes runnable in context of tenant.
     */
    void runForTenant(String tenant, Consumer<MicroserviceCredentials> runnable);
}
