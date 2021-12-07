/*
 * Copyright (c) 2012-2020 Cumulocity GmbH
 * Copyright (c) 2020-2021 Software AG, Darmstadt, Germany and/or Software AG USA Inc., Reston, VA, USA, and/or its subsidiaries and/or its affiliates and/or their licensors.
 *
 * Use, reproduction, transfer, publication or disclosure is prohibited except as specifically provided for in your License Agreement with Software AG.
 */

package com.cumulocity.microservice.lpwan.codec.model;

import com.google.common.base.Strings;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The <b>LpwanCodecDetails</b> class represents the fragment details that is added in the device type managed object which is created on the codec microservice subscription.
 *
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LpwanCodecDetails {

    private static final String CODEC_SERVICE_CONTEXT_PATH = "codecServiceContextPath";
    private static final String DEVICE_MANUFACTURER = "deviceManufacturer";
    private static final String DEVICE_MODEL = "deviceModel";

    @NotBlank
    private String deviceManufacturer;

    @NotBlank
    private String deviceModel;

    @NotBlank
    private String codecServiceContextPath;

    /**
     * This method returns information about the <b>deviceManufacturer</b>, <b>deviceModel</b> and <b>codecServiceContextPath</b> that are added to the <b>c8y_LpwanCodecDetails</b> fragment.
     *
     * @return Map<String, String> the attributes
     */
    public Map<String, String> getAttributes() {
        Map<String,String> attributes = new HashMap<>(3);

        attributes.put(DEVICE_MANUFACTURER, deviceManufacturer);
        attributes.put(DEVICE_MODEL, deviceModel);
        attributes.put(CODEC_SERVICE_CONTEXT_PATH, codecServiceContextPath);

        return attributes;
    }

    /**
     * This method checks if the fields are null or empty.
     *
     * @throws IllegalArgumentException if the field marked with <b>@NotBlank</b> are either null or blank.
     * @see IllegalArgumentException
     */
    public void validate() {
        List<String> missingParameters = new ArrayList<>(3);

        if (Strings.isNullOrEmpty(deviceManufacturer)) {
            missingParameters.add("'deviceManufacturer'");
        }

        if (Strings.isNullOrEmpty(deviceModel)) {
            missingParameters.add("'deviceModel'");
        }

        if (Strings.isNullOrEmpty(codecServiceContextPath)) {
            missingParameters.add("'codecServiceContextPath'");
        }

        if(!missingParameters.isEmpty()) {
            throw new IllegalArgumentException("LpwanCodecDetails is missing mandatory parameters: " + String.join(", ", missingParameters));
        }
    }
}
