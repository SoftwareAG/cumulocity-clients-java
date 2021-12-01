/*
 * Copyright (c) 2012-2020 Cumulocity GmbH
 * Copyright (c) 2020-2021 Software AG, Darmstadt, Germany and/or Software AG USA Inc., Reston, VA, USA, and/or its subsidiaries and/or its affiliates and/or their licensors.
 *
 * Use, reproduction, transfer, publication or disclosure is prohibited except as specifically provided for in your License Agreement with Software AG.
 */

package com.cumulocity.lpwan.codec.model;

import com.google.common.base.Strings;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public Map<String, String> getAttributes() {
        Map<String,String> attributes = new HashMap<>(3);

        attributes.put(DEVICE_MANUFACTURER, deviceManufacturer);
        attributes.put(DEVICE_MODEL, deviceModel);
        attributes.put(CODEC_SERVICE_CONTEXT_PATH, codecServiceContextPath);

        return attributes;
    }

    public void validate() {
        boolean isValid = true;
        List<String> missingParameters = new ArrayList<>(3);

        if (Strings.isNullOrEmpty(deviceManufacturer)) {
            isValid = false;
            missingParameters.add("'deviceManufacturer'");
        }

        if (Strings.isNullOrEmpty(deviceModel)) {
            isValid = false;
            missingParameters.add("'deviceModel'");
        }

        if (Strings.isNullOrEmpty(codecServiceContextPath)) {
            isValid = false;
            missingParameters.add("'codecServiceContextPath'");
        }

        if (!isValid) {
            throw new IllegalArgumentException("LpwanCodecDetails is missing mandatory parameters: " + String.join(", ", missingParameters));
        }
    }
}
