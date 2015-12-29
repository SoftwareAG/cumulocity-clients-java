package com.cumulocity.agent.server.agent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cumulocity.agent.server.repository.IdentityRepository;
import com.cumulocity.agent.server.repository.InventoryRepository;
import com.cumulocity.model.ID;
import com.cumulocity.model.idtype.GId;
import com.cumulocity.rest.representation.inventory.ManagedObjectRepresentation;
import com.cumulocity.sdk.client.SDKException;

@Component
public class DeviceCreatorService {

    private final static Logger logger = LoggerFactory.getLogger(DeviceCreatorService.class);
    
    public final static String DEVICE_IDENTIFIER_TYPE = "c8y_Serial";
    
    private final IdentityRepository identity;
    
    private final InventoryRepository inventory;
    
    private final AgentService agentService;
    
    @Autowired
    public DeviceCreatorService(IdentityRepository identity, InventoryRepository inventory, AgentService agentService) {
        this.identity = identity;
        this.inventory = inventory;
        this.agentService = agentService;
    }
    
    public GId getDeviceId(String serial) throws SDKException {
        return identity.find(new ID(DEVICE_IDENTIFIER_TYPE, serial));
    }
    
    public ManagedObjectRepresentation createDevice(ManagedObjectRepresentation device, String serial) {
        ManagedObjectRepresentation deviceObject = inventory.save(device);
        identity.save(deviceObject.getId(), new ID(DEVICE_IDENTIFIER_TYPE, serial));
        addDeviceAsAgentChild(deviceObject);
        return deviceObject;
    }
    
    private void addDeviceAsAgentChild(final ManagedObjectRepresentation device) {
        try {
            GId agentId  = agentService.getAgentId();
            inventory.bindToParent(agentId, device.getId());
        } catch (Exception e) {
            logger.error("Could not attach device as agent child", e);
        }
    }
    
}
