/*
 * Copyright (c) 2012-2020 Cumulocity GmbH
 * Copyright (c) 2020-2021 Software AG, Darmstadt, Germany and/or Software AG USA Inc., Reston, VA, USA, and/or its subsidiaries and/or its affiliates and/or their licensors.
 *
 * Use, reproduction, transfer, publication or disclosure is prohibited except as specifically provided for in your License Agreement with Software AG.
 */

package com.cumulocity.lpwan.codec.model;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;

import java.util.Map;

import static com.cumulocity.lpwan.codec.util.Constants.DEVICE_MANUFACTURER;
import static com.cumulocity.lpwan.codec.util.Constants.DEVICE_MODEL;

/**
 * The DeviceInfo class uniquely represents one device with the device manufacturer name, the device model and the device type.
 */
@Getter
@AllArgsConstructor
@EqualsAndHashCode
public class DeviceInfo {
    @NonNull
    private String manufacturer;

    @NonNull
    private String model;

    @NonNull
    private DeviceTypeEnum type;

    public String getDeviceTypeName() {
        return manufacturer + " : " + model;
    }

    public Map<String, String> getAttributes() {
        return Map.of(DEVICE_MANUFACTURER, manufacturer,
                DEVICE_MODEL, model);
    }
}
