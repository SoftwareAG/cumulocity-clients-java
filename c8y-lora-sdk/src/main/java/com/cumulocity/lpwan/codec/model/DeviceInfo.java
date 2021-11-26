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
import lombok.extern.slf4j.Slf4j;

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

    public void validate() {
        if (Strings.isNullOrEmpty(manufacturer)) {
            throw new IllegalArgumentException("'manufacturer' is a mandatory parameter.");
        }

        if (Strings.isNullOrEmpty(model)) {
            throw new IllegalArgumentException("'model' is a mandatory parameter.");
        }
    }
}
