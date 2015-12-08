package com.cumulocity.agent.server.executor;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cumulocity.agent.server.context.DeviceContextService;
import com.cumulocity.agent.server.devicecontrol.OperationExecutor;
import com.cumulocity.agent.server.devicecontrol.OperationSubscriber;
import com.cumulocity.rest.representation.operation.OperationRepresentation;

@Component
public class DeviceOperationExecutor implements OperationExecutor {

    protected static final String AGENT_LOG_FRAGMENT = "c8y_AgentLogRequest";
    
    protected Map<String,Map<String, OperationSubscriber>> subscribers = new HashMap<String, Map<String,OperationSubscriber>>();
    
    protected final DeviceContextService contextService;
    
    @Autowired
    public DeviceOperationExecutor(DeviceContextService contextService) {
        this.contextService = contextService;
    }
    
    @Override
    public boolean supports(OperationRepresentation operation) {
        if (operation.get(AGENT_LOG_FRAGMENT) != null) {
            return false;
        };
        return true;
    }

    @Override
    public void handle(OperationRepresentation operation) {
        OperationSubscriber subscriber = getSubscriber(operation.getDeviceId().getValue());
        if (subscriber != null) {
            subscriber.executeOperation(operation);
        }

    }
    
    protected OperationSubscriber getSubscriber(String deviceId) {
        Map<String, OperationSubscriber> tenantSubscribers = subscribers.get(getCurrentTenant());
        if (tenantSubscribers != null) {
            return tenantSubscribers.get(deviceId);
        }
        return null;
    }
    

    protected String getCurrentTenant() {
        return contextService.getContext().getLogin().getTenant();
    }
}
