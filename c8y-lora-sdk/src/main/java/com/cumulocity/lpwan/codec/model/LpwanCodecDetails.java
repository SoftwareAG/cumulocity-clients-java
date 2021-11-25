/*
 * Copyright (c) 2012-2020 Cumulocity GmbH
 * Copyright (c) 2020-2021 Software AG, Darmstadt, Germany and/or Software AG USA Inc., Reston, VA, USA, and/or its subsidiaries and/or its affiliates and/or their licensors.
 *
 * Use, reproduction, transfer, publication or disclosure is prohibited except as specifically provided for in your License Agreement with Software AG.
 */

package com.cumulocity.lpwan.codec.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LpwanCodecDetails {

    public static final String CODEC_SERVICE_CONTEXT_PATH = "codecServiceContextPath";
    public static final String DEVICE_MANUFACTURER = "deviceManufacturer";
    public static final String DEVICE_MODEL = "deviceModel";

    private String deviceManufacturer;
    private String deviceModel;
    private String codecServiceContextPath;

    public Map<String, String> getAttributes() {
        Map<String,String> attributes = new HashMap<>(3);

        attributes.put(DEVICE_MANUFACTURER, deviceManufacturer);
        attributes.put(DEVICE_MODEL, deviceModel);
        attributes.put(CODEC_SERVICE_CONTEXT_PATH, codecServiceContextPath);

        return attributes;
    }
}
