package com.cumulocity.agent.server.device;

import java.util.HashMap;
import java.util.concurrent.Callable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.cumulocity.agent.server.context.DeviceContext;
import com.cumulocity.agent.server.context.DeviceContextService;
import com.cumulocity.agent.server.context.DeviceCredentials;
import com.cumulocity.agent.server.repository.IdentityRepository;
import com.cumulocity.agent.server.repository.InventoryRepository;
import com.cumulocity.model.Agent;
import com.cumulocity.model.ID;
import com.cumulocity.model.idtype.GId;
import com.cumulocity.rest.representation.inventory.ManagedObjectRepresentation;
import com.cumulocity.sdk.client.SDKException;
import com.google.common.collect.Maps;

@Component
public class DeviceCreatorService {

    private final static Logger logger = LoggerFactory.getLogger(DeviceCreatorService.class);
    
    private static final String AGENT_IDENTITY_TYPE = "c8y_AgentIdentifier";
    
    private final String agentUser;
    
    private final String agentPassword;
    
    private final ID agentIdentity;
    
    private DeviceContextService contextService;
    
    private IdentityRepository identity;
    
    private InventoryRepository inventory;
    
    @Autowired
    public DeviceCreatorService(
            @Value("${C8Y.agent.user}") String agentUser,
            @Value("${C8Y.agent.password}") String agentPassword,
            @Value("${C8Y.agent.identity}") String agentIdentity,
            DeviceContextService contextService,
            IdentityRepository identity,
            InventoryRepository inventory) {
        this.agentUser = agentUser;
        this.agentPassword = agentPassword;
        this.agentIdentity = new ID(AGENT_IDENTITY_TYPE, agentIdentity);
        this.contextService = contextService;
        this.identity = identity;
        this.inventory = inventory;
    }
    
    public ManagedObjectRepresentation createDevice(ManagedObjectRepresentation device, String serial) {
        ManagedObjectRepresentation deviceObject = inventory.save(device);
        identity.save(deviceObject.getId(), new ID("c8y_Serial", serial));
        addDeviceAsAgentChild(deviceObject);
        return deviceObject;
    }
    
    private GId createAgent(String tenant) throws Exception {
        return contextService.withinContext(getAgentContext(tenant), new Callable<GId>() {

            @Override
            public GId call() throws Exception {
                try {
                    logger.info("Create agent object for agent: {}", agentIdentity);
                    ManagedObjectRepresentation agentObject = inventory.save(defaultAgent(agentIdentity.getValue()));
                    logger.info("Set external Id {} for agent with GId {}", agentIdentity, agentObject.getId());
                    identity.save(agentObject.getId(), agentIdentity);
                    return agentObject.getId();
                } catch (SDKException e) {
                    logger.error("Could not create agent " + agentIdentity, e);
                    throw e;
                }
            }
        }).call();
    }
    
    private void addDeviceAsAgentChild(final ManagedObjectRepresentation device) {
        GId agentId;
        try {
            try {
                agentId = identity.find(agentIdentity);
            } catch (SDKException e) {
                agentId = createAgent(contextService.getContext().getLogin().getTenant());
            }
            inventory.bindToParent(agentId, device.getId());
        } catch (Exception e) {
            logger.error("Could not attach device as agent child", e);
        }
    }
    
    private DeviceContext getAgentContext(String tenant) {
        return new DeviceContext(new DeviceCredentials(tenant, agentUser, agentPassword, null, null));
    }
    
    private static ManagedObjectRepresentation defaultAgent(String name) {
        ManagedObjectRepresentation agent = new ManagedObjectRepresentation();
        agent.setName(name);
        agent.set(Maps.newHashMap(), Agent.class);
        return agent;
    }
    
}
