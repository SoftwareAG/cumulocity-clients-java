package com.cumulocity.microservice.lpwan.codec.model;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class DeviceCommandTest {
    @Test
    void doValidate_FailForNullParameters() {
        DeviceCommand deviceCommand = new DeviceCommand(null, null, null);
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, deviceCommand::validate);

        List<String> missingParameters = Arrays.asList("'name'", "'category'");
        assertEquals("DeviceCommand is missing mandatory parameters: " + String.join(", ", missingParameters), exception.getMessage());
    }

    @Test
    void doValidate_FailForNullAndEmptyParameters() {
        DeviceCommand deviceCommand = new DeviceCommand(null, "", "");
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, deviceCommand::validate);

        List<String> missingParameters = Arrays.asList("'name'", "'category'");
        assertEquals("DeviceCommand is missing mandatory parameters: " + String.join(", ", missingParameters), exception.getMessage());
    }
}