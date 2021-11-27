/*
 * Copyright (c) 2012-2020 Cumulocity GmbH
 * Copyright (c) 2020-2021 Software AG, Darmstadt, Germany and/or Software AG USA Inc., Reston, VA, USA, and/or its subsidiaries and/or its affiliates and/or their licensors.
 *
 * Use, reproduction, transfer, publication or disclosure is prohibited except as specifically provided for in your License Agreement with Software AG.
 */

package com.cumulocity.lpwan.codec.model;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class LpwanCodecDetailsTest {

    @Test
    void doValidate_FailForNullParameters() {
        LpwanCodecDetails lpwanCodecDetails = new LpwanCodecDetails(null, null, null);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, lpwanCodecDetails::validate);

        List<String> missingParameters = Arrays.asList("'deviceManufacturer'", "'deviceModel', 'codecServiceContextPath'");
        assertEquals("LpwanCodecDetails is missing mandatory parameters: " + String.join(", ", missingParameters), exception.getMessage());
    }

    @Test
    void doValidate_FailForNullAndEmptyParameters() {
        LpwanCodecDetails lpwanCodecDetails = new LpwanCodecDetails("", null, "");

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, lpwanCodecDetails::validate);

        List<String> missingParameters = Arrays.asList("'deviceManufacturer'", "'deviceModel', 'codecServiceContextPath'");
        assertEquals("LpwanCodecDetails is missing mandatory parameters: " + String.join(", ", missingParameters), exception.getMessage());
    }
}