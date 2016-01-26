package com.cumulocity.agent.server.logging;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import c8y.LogfileRequest;

import com.cumulocity.agent.server.agent.AgentDeviceControlInitalizer;
import com.cumulocity.agent.server.agent.AgentService;
import com.cumulocity.agent.server.context.DeviceContextService;
import com.cumulocity.agent.server.devicecontrol.OperationExecutor;
import com.cumulocity.agent.server.repository.DeviceControlRepository;
import com.cumulocity.agent.server.repository.InventoryRepository;
import com.cumulocity.model.ID;
import com.cumulocity.rest.representation.inventory.ManagedObjectRepresentation;
import com.cumulocity.rest.representation.operation.OperationRepresentation;
import com.cumulocity.rest.representation.operation.Operations;
import com.cumulocity.sdk.client.SDKException;
import com.google.common.collect.Lists;

@Component
public class LoggingOperationExecutor implements OperationExecutor {

    private static final Logger logger = LoggerFactory.getLogger(LoggingOperationExecutor.class);
    
    private static final String MANAGEMENT_TENANT = "management";
    
    protected final DeviceContextService contextService;
    
    protected final AgentDeviceControlInitalizer agentDeviceControlInitalizer;
    
    protected final DeviceControlRepository deviceControl;
    
    protected final LoggingService loggingService;
    
    protected final AgentService agentService;
    
    protected final InventoryRepository inventoryRepository;
    
    protected final List<String> whiteList;
    
    @Autowired
    public LoggingOperationExecutor(DeviceContextService contextService, AgentDeviceControlInitalizer agentDeviceControlInitalizer, 
            DeviceControlRepository deviceControl, LoggingService loggingService, AgentService agentService, InventoryRepository inventoryRepository,
            @Value("${C8Y.log.tenant.whitelist:}") String whiteList) {
        this.contextService = contextService;
        this.agentDeviceControlInitalizer = agentDeviceControlInitalizer;
        this.deviceControl = deviceControl;
        this.loggingService = loggingService;
        this.agentService = agentService;
        this.inventoryRepository = inventoryRepository;
        this.whiteList = buildWhitelist(whiteList);
    }
    
    @Override
    public boolean supports(OperationRepresentation operation) {
        if (operation.get(LogfileRequest.class) != null) {
            return true;
        };
        return false;
    }

    @Override
    public void handle(OperationRepresentation operation) {
        try {
            logger.info("New LogfileRequest received wit id: {}", operation.getId());
            deviceControl.save(Operations.asExecutingOperation(operation.getId()));
            LogfileRequest logRequest = operation.get(LogfileRequest.class);
            String contextTenant = getCurrentTenant();
            String operationTenant = logRequest.getTenant();
            if (!whiteList.contains(contextTenant)) {
                if (operationTenant == null) {
                    logRequest.setTenant(contextTenant);
                } else if (!contextTenant.equals(operationTenant)) {
                    deviceControl.save(Operations.asFailedOperation(operation.getId(), "Cannot query log for different tenant"));
                    return;
                }
            }
            if (StringUtils.isEmpty(logRequest.getDeviceUser())) {
                ID agentId = agentService.getAgentId();
                if (!agentId.equals(operation.getDeviceId())){
                    ManagedObjectRepresentation device = inventoryRepository.findById(operation.getDeviceId());
                    logRequest.setDeviceUser(device.getOwner());
                }
            }
            loggingService.readLog(operation);
        } catch (SDKException e) {
            logger.error("Could not handle LogfileRequest operation with id: " + operation.getId(), e);
        }
    }
    
    protected String getCurrentTenant() {
        return contextService.getCredentials().getTenant();
    }
    
    private static List<String> buildWhitelist(String whitelist) {
        if (whitelist.length() < 1) {
           return Lists.newArrayList(MANAGEMENT_TENANT) ;
        }
        return Lists.asList(MANAGEMENT_TENANT, whitelist.split(","));
    }
}
