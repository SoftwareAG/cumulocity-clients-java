package com.cumulocity.microservice.lpwan.codec.model;

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