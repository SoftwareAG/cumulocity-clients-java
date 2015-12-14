package com.cumulocity.agent.server.agent;

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
public class AgentService {
    
    private final static Logger logger = LoggerFactory.getLogger(AgentService.class);
    
    protected static final String AGENT_IDENTITY_TYPE = "c8y_AgentIdentifier";
    
    private final String agentUser;
    
    private final String agentPassword;
    
    private final ID agentIdentity;
    
    private final DeviceContextService contextService;
    
    private final IdentityRepository identity;
    
    private final InventoryRepository inventory;
    
    @Autowired
    public AgentService(
            @Value("${C8Y.agent.user}") String agentUser,
            @Value("${C8Y.agent.password}") String agentPassword,
            @Value("${C8Y.application.id}") String applicationId,
            DeviceContextService contextService,
            IdentityRepository identity,
            InventoryRepository inventory) {
        this.agentUser = agentUser;
        this.agentPassword = agentPassword;
        this.agentIdentity = new ID(AGENT_IDENTITY_TYPE, applicationId);
        this.contextService = contextService;
        this.identity = identity;
        this.inventory = inventory;
    }
    
    public GId getAgentId() throws SDKException {
        try {
            String tenant = contextService.getCredentials().getTenant();
            return contextService.callWithinContext(getAgentContext(tenant), new Callable<GId>() {

                @Override
                public GId call() throws Exception {
                    try {
                        logger.debug(contextService.getCredentials().toString());
                        return identity.find(agentIdentity);
                    } catch (SDKException e) {
                        return createAgent(getCurrentTenantContext()).getId();
                    }
                }
            });
        } catch (Exception e) {
            throw new SDKException("Could not get agentId", e);
        }
    }
    
    private ManagedObjectRepresentation createAgent(String tenant) throws SDKException {
        try {
            logger.info("Create agent object for agent: {}", agentIdentity);
            ManagedObjectRepresentation agentObject = inventory.save(defaultAgent(agentIdentity.getValue()));
            logger.info("Set externalId {} for agent with GId {}", agentIdentity, agentObject.getId());
            identity.save(agentObject.getId(), agentIdentity);
            return agentObject;
        } catch (SDKException e) {
            logger.error("Could not create agent " + agentIdentity, e);
            throw new SDKException("Error creating new agent object for tenant " + tenant, e);
        }
    }
    
    public DeviceContext getAgentContext(String tenant) {
        logger.debug("Requesting agent context for tenant {} and user {}", tenant, agentUser);
        return new DeviceContext(new DeviceCredentials(tenant, agentUser, agentPassword, null, null));
    }
    
    private String getCurrentTenantContext() {
        return contextService.getCredentials().getTenant();
    }
    
    protected static ManagedObjectRepresentation defaultAgent(String name) {
        ManagedObjectRepresentation agent = new ManagedObjectRepresentation();
        agent.setName(name);
        agent.set(Maps.newHashMap(), Agent.class);
        return agent;
    }
}
