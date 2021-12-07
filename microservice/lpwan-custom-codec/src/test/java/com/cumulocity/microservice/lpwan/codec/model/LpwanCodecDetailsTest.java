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

    @Test
    void doGetAttributes_success() {
        LpwanCodecDetails lpwanCodecDetails = new LpwanCodecDetails("Manufacturer_1", "Model_1", "lpwan-custom-codec-service-path");

        Map<String, String> attributes = lpwanCodecDetails.getAttributes();

        assertEquals("Manufacturer_1", attributes.get(LpwanCodecDetails.DEVICE_MANUFACTURER));
        assertEquals("Model_1", attributes.get(LpwanCodecDetails.DEVICE_MODEL));
        assertEquals("lpwan-custom-codec-service-path", attributes.get(LpwanCodecDetails.CODEC_SERVICE_CONTEXT_PATH));
    }
}