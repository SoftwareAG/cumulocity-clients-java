package com.cumulocity.agent.server.executor;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import c8y.LogfileRequest;

import com.cumulocity.agent.server.agent.AgentDeviceControlInitalizer;
import com.cumulocity.agent.server.agent.OperationSubscriber;
import com.cumulocity.agent.server.context.DeviceContextService;
import com.cumulocity.agent.server.devicecontrol.OperationExecutor;
import com.cumulocity.agent.server.repository.DeviceControlRepository;
import com.cumulocity.model.operation.OperationStatus;
import com.cumulocity.rest.representation.operation.OperationRepresentation;
import com.cumulocity.sdk.client.devicecontrol.OperationFilter;
import com.google.common.collect.Maps;

@Component
public class DeviceOperationExecutor implements OperationExecutor {

    private static final Logger logger = LoggerFactory.getLogger(DeviceOperationExecutor.class);
    
    protected volatile Map<String,Map<String, OperationSubscriber>> subscribers = Maps.newConcurrentMap();
    
    protected final DeviceContextService contextService;
    
    protected final AgentDeviceControlInitalizer agentDeviceControlInitalizer;
    
    protected final DeviceControlRepository deviceControl;
    
    @Autowired
    public DeviceOperationExecutor(DeviceContextService contextService, AgentDeviceControlInitalizer agentDeviceControlInitalizer, 
            DeviceControlRepository deviceControl) {
        this.contextService = contextService;
        this.agentDeviceControlInitalizer = agentDeviceControlInitalizer;
        this.deviceControl = deviceControl;
    }
    
    @Override
    public boolean supports(OperationRepresentation operation) {
        if (operation.get(LogfileRequest.class) != null) {
            return false;
        };
        return true;
    }

    @Override
    public void handle(OperationRepresentation operation) {
        OperationSubscriber subscriber = getSubscriber(operation.getDeviceId().getValue());
        if (subscriber != null) {
            subscriber.executeOperation(operation);
        } else {
            logger.info("Ignoring operation with id {}. No subscriber found.", operation.getId());
        }
    }
    
    public void subscribe(String deviceId, OperationSubscriber subscriber) {
        logger.info("Subscribe for operation for device with id {}", deviceId);
        synchronized (subscribers) {
            Map<String, OperationSubscriber> tenantSubscribers = getTenantSubscribers();
            if (tenantSubscribers != null) {
                tenantSubscribers.put(deviceId, subscriber);
            } else {
                Map<String, OperationSubscriber> newTenantSubscribers = Maps.newConcurrentMap();
                newTenantSubscribers.put(deviceId, subscriber);
                String tenant = getCurrentTenant();
                subscribers.put(tenant, newTenantSubscribers);
                agentDeviceControlInitalizer.addTenant(tenant);
            }
        }
        getPendingOperations(deviceId);
    }
    
    public void unsubscribe(String deviceId) {
        synchronized (subscribers) {
            Map<String, OperationSubscriber> tenantSubscribers = getTenantSubscribers();
            if (tenantSubscribers == null) {
                return;
            }
            tenantSubscribers.remove(deviceId);
            if (tenantSubscribers.isEmpty()) {
                subscribers.remove(getCurrentTenant());
            }
        }
    }
    
    private void getPendingOperations(String deviceId) {
        logger.info("Get PENDING operations for device with id {}", deviceId);
        for (OperationRepresentation operation : deviceControl.findAllByFilter(getPendingOperationsforDevice(deviceId))) {
            handle(operation);
        }
    }
    
    private OperationSubscriber getSubscriber(String deviceId) {
        Map<String, OperationSubscriber> tenantSubscribers = getTenantSubscribers();
        if (tenantSubscribers != null) {
            return tenantSubscribers.get(deviceId);
        }
        return null;
    }

    private Map<String, OperationSubscriber> getTenantSubscribers() {
        return subscribers.get(getCurrentTenant());
    }

    private static OperationFilter getPendingOperationsforDevice(String deviceId) {
        OperationFilter filter = new OperationFilter();
        filter.byDevice(deviceId);
        filter.byStatus(OperationStatus.PENDING);
        return filter;
    }
    
    protected String getCurrentTenant() {
        return contextService.getCredentials().getTenant();
    }
}
