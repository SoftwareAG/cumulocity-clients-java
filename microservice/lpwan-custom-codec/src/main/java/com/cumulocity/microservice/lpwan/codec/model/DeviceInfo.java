/*
 * Copyright (c) 2012-2020 Cumulocity GmbH
 * Copyright (c) 2020-2021 Software AG, Darmstadt, Germany and/or Software AG USA Inc., Reston, VA, USA, and/or its subsidiaries and/or its affiliates and/or their licensors.
 *
 * Use, reproduction, transfer, publication or disclosure is prohibited except as specifically provided for in your License Agreement with Software AG.
 */

package com.cumulocity.microservice.lpwan.codec.model;

import com.google.common.base.Strings;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Null;
import java.util.*;

/**
 * The <b>DeviceInfo</b> class uniquely represents one device with the device manufacturer name, the device model.
 *
 */
@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class DeviceInfo {
    static final String DEVICE_MANUFACTURER = "deviceManufacturer";
    static final String DEVICE_MODEL = "deviceModel";

    private static final String SUPPORTED_DEVICE_COMMANDS = "supportedDeviceCommands";

    @NotBlank
    @EqualsAndHashCode.Include
    private String manufacturer;

    @NotBlank
    @EqualsAndHashCode.Include
    private String model;

    @Null
    private Set<DeviceCommand> supportedCommands;

    /**
     * Instantiates a new DeviceInfo.
     * @param manufacturer represents the name of the device manufacturer.
     * @param model        represents the name of the device model.
     */
    public DeviceInfo(@NotBlank String manufacturer, @NotBlank String model) {
        this(manufacturer, model, null);
    }

    public DeviceInfo(@NotBlank String manufacturer, @NotBlank String model, @Null Set<DeviceCommand> supportedCommands) {
        this.manufacturer = manufacturer;
        this.model = model;
        this.supportedCommands = supportedCommands;
    }

    public Map<String, Object> getAttributes() {
        Map<String,Object> attributes = new HashMap<>(3);
        attributes.put(DEVICE_MANUFACTURER, manufacturer);
        attributes.put(DEVICE_MODEL, model);
        if(Objects.nonNull(supportedCommands)) {
            List<Map<String, Object>> supportedCommandsAttributesList = new ArrayList<>();
            attributes.put(SUPPORTED_DEVICE_COMMANDS, supportedCommandsAttributesList);
            for(DeviceCommand supportedCommand: supportedCommands) {
                supportedCommandsAttributesList.add(supportedCommand.getAttributes());
            }
        }
        return attributes;
    }
    /**
     * This method validates the object field.
     *
     * @throws IllegalArgumentException if the field marked with <b>@NotBlank</b> are either null or blank.
     * @see <a href="https://docs.oracle.com/javase/7/docs/api/java/lang/IllegalArgumentException.html">IllegalArgumentException</a>
     */
    public void validate() {
        List<String> missingParameters = new ArrayList<>(3);

        if (Strings.isNullOrEmpty(manufacturer)) {
            missingParameters.add("'manufacturer'");
        }

        if (Strings.isNullOrEmpty(model)) {
            missingParameters.add("'model'");
        }

        if(Objects.nonNull(supportedCommands) && !supportedCommands.isEmpty()) {
            for(DeviceCommand supportedCommand: supportedCommands) {
                try {
                    supportedCommand.validate();
                } catch (IllegalArgumentException e) {
                    missingParameters.add("'name, category and/or command'");
                }
            }
        }

        if(!missingParameters.isEmpty()) {
            throw new IllegalArgumentException("DeviceInfo is missing mandatory parameters: " + String.join(", ", missingParameters));
        }
    }
}
