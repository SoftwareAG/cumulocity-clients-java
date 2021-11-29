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
import lombok.extern.slf4j.Slf4j;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

/**
 * The DeviceInfo class uniquely represents one device with the device manufacturer name, the device model and the device type.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Slf4j
public class DeviceInfo {
    @NotBlank
    private String manufacturer;

    @NotBlank
    private String model;

    public void validate() {
        boolean isValid = true;
        List<String> missingParameters = new ArrayList<>(2);

        if (Strings.isNullOrEmpty(manufacturer)) {
            isValid = false;
            missingParameters.add("'manufacturer'");
        }

        if (Strings.isNullOrEmpty(model)) {
            isValid = false;
            missingParameters.add("'model'");
        }

        if (!isValid) {
            throw new IllegalArgumentException("DeviceInfo is missing mandatory parameters: " + String.join(", ", missingParameters));
        }
    }
}
