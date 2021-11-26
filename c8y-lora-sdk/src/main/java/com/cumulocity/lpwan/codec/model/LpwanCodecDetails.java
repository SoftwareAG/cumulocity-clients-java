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
import lombok.NonNull;

import java.util.HashMap;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LpwanCodecDetails {

    private static final String CODEC_SERVICE_CONTEXT_PATH = "codecServiceContextPath";
    private static final String DEVICE_MANUFACTURER = "deviceManufacturer";
    private static final String DEVICE_MODEL = "deviceModel";

    @NonNull
    private String deviceManufacturer;

    @NonNull
    private String deviceModel;

    @NonNull
    private String codecServiceContextPath;

    public Map<String, String> getAttributes() {
        Map<String,String> attributes = new HashMap<>(3);

        attributes.put(DEVICE_MANUFACTURER, deviceManufacturer);
        attributes.put(DEVICE_MODEL, deviceModel);
        attributes.put(CODEC_SERVICE_CONTEXT_PATH, codecServiceContextPath);

        return attributes;
    }

    public void validate() {
        if (Strings.isNullOrEmpty(deviceManufacturer)) {
            throw new IllegalArgumentException("'deviceManufacturer' is a mandatory parameter.");
        }

        if (Strings.isNullOrEmpty(deviceModel)) {
            throw new IllegalArgumentException("'deviceModel' is a mandatory parameter.");
        }

        if (Strings.isNullOrEmpty(codecServiceContextPath)) {
            throw new IllegalArgumentException("'codecServiceContextPath' is a mandatory parameter.");
        }
    }
}
