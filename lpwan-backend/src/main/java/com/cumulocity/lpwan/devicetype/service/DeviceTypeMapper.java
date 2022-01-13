/*
 * Copyright (c) 2012-2020 Cumulocity GmbH
 * Copyright (c) 2020-2022 Software AG, Darmstadt, Germany and/or Software AG USA Inc., Reston, VA, USA, and/or its subsidiaries and/or its affiliates and/or their licensors.
 *
 * Use, reproduction, transfer, publication or disclosure is prohibited except as specifically provided for in your License Agreement with Software AG.
 */

package com.cumulocity.lpwan.devicetype.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cumulocity.lpwan.devicetype.model.DeviceType;
import com.cumulocity.lpwan.devicetype.model.MessageTypes;
import com.cumulocity.lpwan.devicetype.model.UplinkConfiguration;
import com.cumulocity.lpwan.mapping.model.MessageTypeMapping;
import com.cumulocity.lpwan.payload.uplink.model.MessageIdConfiguration;
import com.cumulocity.rest.representation.inventory.ManagedObjectRepresentation;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.MapType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.google.common.collect.Lists;

@Component
public class DeviceTypeMapper {

    private ObjectMapper objectMapper;

    @Autowired
    public DeviceTypeMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public DeviceType convertManagedObjectToDeviceType(ManagedObjectRepresentation managedObject) {
        DeviceType deviceTypeObject = objectMapper.convertValue(managedObject.getAttrs(), DeviceType.class);

        deviceTypeObject.setType(managedObject.getType());
        deviceTypeObject.setName(managedObject.getName());

        if (deviceTypeObject.getMessageTypes() == null) {
            deviceTypeObject.setMessageTypes(new MessageTypes());
        }

        if (deviceTypeObject.getUplinkConfigurations() == null) {
            deviceTypeObject.setUplinkConfigurations(Lists.<UplinkConfiguration> newArrayList());
        }

        if (deviceTypeObject.getMessageIdConfiguration() == null) {
            deviceTypeObject.setMessageIdConfiguration(new MessageIdConfiguration());
        }

        TypeFactory typeFactory = objectMapper.getTypeFactory();
        MapType mapType = typeFactory.constructMapType(HashMap.class, String.class, MessageTypeMapping.class);
        HashMap<String, MessageTypeMapping> map = objectMapper.convertValue(managedObject.get("c8y_MessageTypes"), mapType);

        if (map == null) {
            Map<String, MessageTypeMapping> emptyMap = new HashMap<>();
            deviceTypeObject.getMessageTypes().setMessageTypeMappings(emptyMap);
        } else {
            deviceTypeObject.getMessageTypes().setMessageTypeMappings(map);
        }

        return deviceTypeObject;
    }
}
