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
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;

/**
 * The DeviceInfo class uniquely represents one device with the device manufacturer name, the device model and the device type.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Slf4j
public class DeviceInfo {
    @NonNull
    private String manufacturer;

    @NonNull
    private String model;

    @NonNull
    private NetworkProviderType networkProviderType;

    public String getDeviceTypeName() {
        return manufacturer + " : " + model;
    }

    public String getDeviceTypeExternalId() {
        return getDeviceTypeName() + " : " + networkProviderType.getFieldbusType();
    }

    public boolean isValid() {
        return Objects.nonNull(manufacturer) && Objects.nonNull(model) && Objects.nonNull(networkProviderType);
    }
}
