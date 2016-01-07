package com.cumulocity.agent.server.agent;

import java.util.Arrays;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.cumulocity.agent.server.context.DeviceContextService;
import com.cumulocity.agent.server.devicecontrol.DeviceControlListener;
import com.cumulocity.agent.server.repository.DeviceControlRepository;
import com.cumulocity.model.idtype.GId;
import com.google.common.collect.Sets;

@Component
public class AgentDeviceControlInitalizer {

    private static final Logger logger = LoggerFactory.getLogger(AgentDeviceControlInitalizer.class);

    private final DeviceControlRepository deviceControlRepository;

    private final DeviceContextService contextService;

    private final DeviceControlListener listener;
    
    private final AgentService agentService;
    
    private final Set<String> connectedTenants;
    
    @Autowired
    public AgentDeviceControlInitalizer(DeviceControlRepository deviceControlRepository,
            DeviceContextService contextService, DeviceControlListener listener, AgentService agentService, 
            @Value("${C8Y.tenant.autoconnect:}") String autoConnectTenants) {
        this.deviceControlRepository = deviceControlRepository;
        this.contextService = contextService;
        this.listener = listener;
        this.connectedTenants = autoConnectTenants(autoConnectTenants);
        this.agentService = agentService;
    }

    @PostConstruct
    public void initalize() {
        for (String tenant : connectedTenants) {
            contextService.runWithinContext(agentService.getAgentContext(tenant), new AgentPushConnection());
        }
    }
    
    public void addTenant(String tenant) {
        logger.info("Add tenant {} to device push", tenant);
        if (!connectedTenants.contains(tenant)) {
            connectedTenants.add(tenant);
            contextService.runWithinContext(agentService.getAgentContext(tenant), new AgentPushConnection());
        }
    }

    private static Set<String> autoConnectTenants(String tenantList) {
        return Sets.newHashSet(Arrays.asList(tenantList.split(",")));
    }
    
    private class AgentPushConnection implements Runnable {

        @Override
        public void run() {
            logger.info("Initialize push connection");
            GId agentId = agentService.getAgentId();
            logger.info("start listening for operations");
            deviceControlRepository.subscribe(agentId, listener);
        }
        
    }

}
