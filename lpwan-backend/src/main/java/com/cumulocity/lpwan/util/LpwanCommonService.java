package com.cumulocity.lpwan.util;

import c8y.Agent;
import com.cumulocity.lpwan.lns.connection.model.LnsConnectionDeserializer;
import com.cumulocity.lpwan.lns.connection.model.LpwanDeviceFilter;
import com.cumulocity.rest.representation.inventory.ManagedObjectRepresentation;
import com.cumulocity.rest.representation.inventory.ManagedObjects;
import com.cumulocity.sdk.client.SDKException;
import com.cumulocity.sdk.client.inventory.InventoryApi;
import com.cumulocity.sdk.client.inventory.InventoryFilter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Objects;

@Component
@Slf4j
public class LpwanCommonService {

    public static final String MAINTAINER = "Software AG";

    @Autowired
    InventoryApi inventoryApi;

    public synchronized void migrateOldDeviceWithNewAgentFragment(String version){
        Iterable<ManagedObjectRepresentation> managedObjectRepresentations = getDeviceMosByProvider(LnsConnectionDeserializer.getRegisteredAgentName());
        if (managedObjectRepresentations == null) return;

        for(ManagedObjectRepresentation deviceMo : managedObjectRepresentations){
            Agent agentFromMo = deviceMo.get(Agent.class);
            if(Objects.isNull(agentFromMo)) {
                ManagedObjectRepresentation deviceMoToBeUpdated = ManagedObjects.asManagedObject(deviceMo.getId());
                deviceMoToBeUpdated.set(prepareAgentFragment(version));
                try {
                    inventoryApi.update(deviceMoToBeUpdated);
                    log.info("The device with the Managed object Id '{}' is updated with fragment 'c8y_Agent'", deviceMo.getId());
                } catch (SDKException e) {
                    String message = String.format("Error in updating the device with managed object id '%s", deviceMoToBeUpdated.getId());
                    log.error(message, e);
                }
            }
        }
    }

    public Iterable<ManagedObjectRepresentation> getDeviceMosByProvider(String lpwanProviderName) {
        InventoryFilter inventoryFilter = LpwanDeviceFilter.byServiceProvider(lpwanProviderName);
        Iterable<ManagedObjectRepresentation> managedObjectRepresentations = null;
        try {
            managedObjectRepresentations = inventoryApi.getManagedObjectsByFilter(inventoryFilter).get().allPages();
        } catch (SDKException e) {
            String message = String.format("Error in getting device managed objects from the service provider '%s'", lpwanProviderName);
            log.error(message, e);
            return null;
        }
        return managedObjectRepresentations;
    }

    public Agent prepareAgentFragment(String version) {
        Agent agent = new Agent();
        agent.setName(LnsConnectionDeserializer.getRegisteredAgentName());
        agent.setVersion(version);
        agent.setMaintainer(MAINTAINER);

        return agent;
    }
}
