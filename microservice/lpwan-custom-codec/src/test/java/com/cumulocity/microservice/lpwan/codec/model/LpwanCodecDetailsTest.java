package com.cumulocity.microservice.lpwan.codec.model;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class LpwanCodecDetailsTest {

    @Test
    void doValidate_FailForNullParameters() {
        LpwanCodecDetails lpwanCodecDetails = new LpwanCodecDetails(null, null);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, lpwanCodecDetails::validate);

        List<String> missingParameters = Arrays.asList("'codecServiceContextPath', 'supportedDevices'");
        assertEquals("LpwanCodecDetails is missing mandatory parameters: " + String.join(", ", missingParameters), exception.getMessage());
    }

    @Test
    void doValidate_FailForNullAndEmptyParameters() {
        DeviceInfo deviceInfo = new DeviceInfo("", null, null);
        LpwanCodecDetails lpwanCodecDetails = new LpwanCodecDetails("", deviceInfo);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, lpwanCodecDetails::validate);

        List<String> missingParameters = Arrays.asList("'codecServiceContextPath', 'manufacturer, model and/or supportedCommands'");
        assertEquals("LpwanCodecDetails is missing mandatory parameters: " + String.join(", ", missingParameters), exception.getMessage());
    }

    @Test
    void doGetAttributes_success() {
        DeviceInfo deviceInfo = new DeviceInfo("Manufacturer_1", "Model_1", null);
        LpwanCodecDetails lpwanCodecDetails = new LpwanCodecDetails("lpwan-custom-codec-service-path", deviceInfo);

        Map<String, Object> attributes = lpwanCodecDetails.getAttributes();

        Map<String, Object> supportedDevicesAttributes = (Map<String, Object>) attributes.get(LpwanCodecDetails.SUPPORTED_DEVICE);
        assertEquals("Manufacturer_1", supportedDevicesAttributes.get(DeviceInfo.DEVICE_MANUFACTURER));
        assertEquals("Model_1", supportedDevicesAttributes.get(DeviceInfo.DEVICE_MODEL));
        assertEquals("lpwan-custom-codec-service-path", attributes.get(LpwanCodecDetails.CODEC_SERVICE_CONTEXT_PATH));
    }
}