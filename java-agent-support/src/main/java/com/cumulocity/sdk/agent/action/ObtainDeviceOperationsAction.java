/*
 * Copyright 2012 Nokia Siemens Networks 
 */
package com.cumulocity.sdk.agent.action;

import org.springframework.beans.factory.annotation.Autowired;

import com.cumulocity.model.operation.OperationStatus;
import com.cumulocity.rest.representation.operation.OperationCollectionRepresentation;
import com.cumulocity.sdk.agent.model.DevicesManagingAgent;
import com.cumulocity.sdk.client.Platform;
import com.cumulocity.sdk.client.devicecontrol.OperationFilter;

/**
 * Obtains from the platform operations to be executed on devices controlled be an agent.
 */
public class ObtainDeviceOperationsAction implements AgentAction {

    private Platform platform;

    private DevicesManagingAgent<?> agent;

    @Autowired
    public ObtainDeviceOperationsAction(Platform platform, DevicesManagingAgent<?> agent) {
        this.platform = platform;
        this.agent = agent;
    }

    @Override
    public void run() {
        try {
            String agentId = agent.getGlobalId().getValue();
            OperationStatus status = OperationStatus.PENDING;
            OperationFilter filter = new OperationFilter().byAgent(agentId).byStatus(status);
            OperationCollectionRepresentation collection = platform.getDeviceControlApi().getOperationsByFilter(filter).get();
            agent.getOperationsQueue().addAll(collection.getOperations());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
