package com.cumulocity.lpwan.devicetype.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import java.util.Map;
import java.util.HashMap;

import com.cumulocity.lpwan.devicetype.model.DeviceType;
import com.cumulocity.rest.representation.inventory.ManagedObjectRepresentation;
import com.fasterxml.jackson.databind.ObjectMapper;

public class DeviceTypeMapperTest {

    private ObjectMapper objectMapper = new ObjectMapper();
    private DeviceTypeMapper deviceTypeMapper = new DeviceTypeMapper(objectMapper);
    
    @Test
    public void shouldInitializeDeviceTypeObjectFields() {
        ManagedObjectRepresentation managedObject = new ManagedObjectRepresentation();
        DeviceType deviceType = deviceTypeMapper.convertManagedObjectToDeviceType(managedObject);
        
        assertNotNull(deviceType.getMessageIdConfiguration());
        assertNotNull(deviceType.getMessageTypes());
        assertNotNull(deviceType.getMessageTypes().getMessageTypeMappings());
        assertNotNull(deviceType.getUplinkConfigurations());
    }
    
    @Test
    public void shouldInitializeWithMessageTypeMappings() {
        String messageTypeId = "1";
        ManagedObjectRepresentation managedObject = new ManagedObjectRepresentation();
        
        Map<String, Integer[]> registerMappings = new HashMap<>();
        Integer[] registerIndexes = new Integer[]{1};
        registerMappings.put("c8y_Registers", registerIndexes);
        
        Map<String, Map<String, Integer[]>> messageTypeMappings = new HashMap<>();
        messageTypeMappings.put(messageTypeId, registerMappings);
        
        managedObject.setProperty("c8y_MessageTypes", messageTypeMappings);
        
        DeviceType deviceType = deviceTypeMapper.convertManagedObjectToDeviceType(managedObject);
        
        assertNotNull(deviceType.getMessageTypes());
        assertNotNull(deviceType.getMessageTypes().getMessageTypeMappings());
        assertNotNull(deviceType.getMessageTypes().getMappingIndexesByMessageType(messageTypeId));
        assertEquals(deviceType.getMessageTypes().getMappingIndexesByMessageType(messageTypeId).getRegisterIndexes().length, 1);
    }
}
