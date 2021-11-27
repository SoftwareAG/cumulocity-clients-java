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

class DecoderInputTest {

    @Test
    void doValidate_FailForAllNullParameters() {
        DecoderInput decoderInput = DecoderInput.builder().build();

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, decoderInput::validate);

        List<String> missingParameters = Arrays.asList("'deviceMoId'", "'deviceEui'", "'payload'", "'updateTime'", "'deviceInfo'");
        assertEquals("DecoderInput is missing mandatory parameters: " + String.join(", ", missingParameters), exception.getMessage());
    }

    @Test
    void doValidate_FailForNullAndEmptyParameters() {
        DecoderInput decoderInput = DecoderInput.builder()
                .deviceMoId("MO ID")
                .deviceEui("")
                .payload("")
                .updateTime(null)
                .deviceInfo(null)
                .build();

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, decoderInput::validate);

        List<String> missingParameters = Arrays.asList("'deviceEui'", "'payload'", "'updateTime'", "'deviceInfo'");
        assertEquals("DecoderInput is missing mandatory parameters: " + String.join(", ", missingParameters), exception.getMessage());
    }

    @Test
    void doValidate_FailForNullDeviceInfoDetails() {
        DecoderInput decoderInput = DecoderInput.builder()
                .deviceMoId("MO ID")
                .deviceEui("EUI ID")
                .payload("Payload text")
                .updateTime(System.currentTimeMillis())
                .deviceInfo(new DeviceInfo())
                .build();

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, decoderInput::validate);

        List<String> missingParameters = Arrays.asList("'manufacturer'", "'model'");
        assertEquals("DeviceInfo is missing mandatory parameters: " + String.join(", ", missingParameters), exception.getMessage());
    }
}