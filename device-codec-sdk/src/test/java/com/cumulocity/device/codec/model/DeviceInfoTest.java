/*
 * Copyright (c) 2012-2020 Cumulocity GmbH
 * Copyright (c) 2020-2021 Software AG, Darmstadt, Germany and/or Software AG USA Inc., Reston, VA, USA, and/or its subsidiaries and/or its affiliates and/or their licensors.
 *
 * Use, reproduction, transfer, publication or disclosure is prohibited except as specifically provided for in your License Agreement with Software AG.
 */

package com.cumulocity.device.codec.model;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class DeviceInfoTest {

    @Test
    void doValidate_FailForNullParameters() {
        DeviceInfo deviceInfo = new DeviceInfo(null, null);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, deviceInfo::validate);

        List<String> missingParameters = Arrays.asList("'manufacturer'", "'model'");
        assertEquals("DeviceInfo is missing mandatory parameters: " + String.join(", ", missingParameters), exception.getMessage());
    }

    @Test
    void doValidate_FailForNullAndEmptyParameters() {
        DeviceInfo deviceInfo = new DeviceInfo("", null);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, deviceInfo::validate);

        List<String> missingParameters = Arrays.asList("'manufacturer'", "'model'");
        assertEquals("DeviceInfo is missing mandatory parameters: " + String.join(", ", missingParameters), exception.getMessage());
    }
}