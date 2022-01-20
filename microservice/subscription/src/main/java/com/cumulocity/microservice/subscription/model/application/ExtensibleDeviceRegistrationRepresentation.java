package com.cumulocity.microservice.subscription.model.application;

import com.cumulocity.rest.representation.AbstractExtensibleRepresentation;
import lombok.Data;

import java.util.Map;
import java.util.Objects;

@Data
public class ExtensibleDeviceRegistrationRepresentation extends AbstractExtensibleRepresentation {

    public static final String FIELD_NAME = "providesDeviceRegistration";

    private String name;

    private String description;

    public static ExtensibleDeviceRegistrationRepresentation toExtensibleDeviceRegistration(Map<String, Object> extensibleDeviceRegistrationMap) {
        if (Objects.isNull(extensibleDeviceRegistrationMap)) {
            return null;
        }

        ExtensibleDeviceRegistrationRepresentation extensibleDeviceRegistration = new ExtensibleDeviceRegistrationRepresentation();
        if (extensibleDeviceRegistrationMap.containsKey("name")) {
            extensibleDeviceRegistration.setName((String) extensibleDeviceRegistrationMap.get("name"));
        }
        if (extensibleDeviceRegistrationMap.containsKey("description")) {
            extensibleDeviceRegistration.setDescription((String) extensibleDeviceRegistrationMap.get("description"));
        }
        return  extensibleDeviceRegistration;
    }
}
