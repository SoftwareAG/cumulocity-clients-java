package com.cumulocity.microservice.subscription.service.impl;

import com.cumulocity.microservice.context.ContextService;
import com.cumulocity.microservice.context.credentials.MicroserviceCredentials;
import com.cumulocity.microservice.subscription.service.MicroserviceContextService;
import com.cumulocity.microservice.subscription.service.MicroserviceSubscriptionsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MicroserviceContextServiceImpl implements MicroserviceContextService {

    private final ContextService<MicroserviceCredentials> contextService;
    private final MicroserviceSubscriptionsService subscriptionsService;

    @Autowired
    public MicroserviceContextServiceImpl(
            final ContextService<MicroserviceCredentials> contextService,
            final MicroserviceSubscriptionsService subscriptionsService) {
        this.contextService = contextService;
        this.subscriptionsService = subscriptionsService;
    }

    @Override
    public void runForEachTenant(final Runnable runnable) {
        for (final MicroserviceCredentials credentials : subscriptionsService.getAll()) {
            contextService.runWithinContext(credentials, runnable);
        }
    }

    @Override
    public void runForTenant(final String tenant, final Runnable runnable) {
        for (final MicroserviceCredentials credentials : subscriptionsService.getCredentials(tenant).asSet()) {
            contextService.runWithinContext(credentials, runnable);
        }
    }
}
