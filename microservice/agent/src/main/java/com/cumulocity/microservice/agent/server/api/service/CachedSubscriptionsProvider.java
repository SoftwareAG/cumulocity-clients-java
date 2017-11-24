package com.cumulocity.microservice.agent.server.api.service;

import static com.google.common.cache.CacheBuilder.newBuilder;

import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cumulocity.agent.server.context.DeviceCredentials;
import com.cumulocity.microservice.exception.AgentInvalidDataException;
import com.cumulocity.rest.representation.application.ApplicationUserRepresentation;
import com.google.common.cache.Cache;

public class CachedSubscriptionsProvider {

    private static final Logger logger = LoggerFactory.getLogger(CachedSubscriptionsProvider.class);

    private MicroserviceRepository microserviceRepository;
    private Cache<String, DeviceCredentials> cache;
    private AgentApplication agentApplication;

    public CachedSubscriptionsProvider(MicroserviceRepository microserviceRepository, AgentApplication application) {
        this.microserviceRepository = microserviceRepository;
        this.agentApplication = application;
        this.cache = newBuilder().expireAfterWrite(60, TimeUnit.MINUTES).build();
    }
    
    public DeviceCredentials getAsDeviceCredentials(String tenantId) {
        DeviceCredentials deviceCredentials = cache.getIfPresent(tenantId);
        if (deviceCredentials == null) {
            deviceCredentials = loadCredentials(tenantId);
        }
        return deviceCredentials;
    }
    
    private synchronized DeviceCredentials loadCredentials(String tenantId) {
        try {
            for (final ApplicationUserRepresentation user : microserviceRepository.getSubscriptions(agentApplication.getId())) {
                if (user.getTenant().equals(tenantId)) {
                    DeviceCredentials credentials = new DeviceCredentials(user.getTenant(), user.getName(), user.getPassword(), null, null);
                    cache.put(tenantId, credentials);
                    return credentials;
                }
            }
        } catch (final Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
        throw new AgentInvalidDataException("Tenant " + tenantId + " is not subscribed to this application");

    }
}
