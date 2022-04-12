package com.cumulocity.microservice.customdecoders.api.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

import static org.junit.jupiter.api.Assertions.*;

public class DecoderResultTest {
    @Test
    public void nullFieldsShouldBeIgnoredOnSerialization() throws Exception {
        DecoderResult decoderResult = new DecoderResult();
        decoderResult.setMessage("\"DecoderInputData is missing mandatory fields: 'sourceDeviceId', 'sourceDeviceEui', 'inputData', 'manufacturer and/or model'\"");
        ObjectMapper objectMapper = new ObjectMapper();

        String expectedJson = "{\"message\":\"\\\"DecoderInputData is missing mandatory fields: 'sourceDeviceId', 'sourceDeviceEui', 'inputData', 'manufacturer and/or model'\\\"\",\"success\":true}";
        String outputJson = objectMapper.writeValueAsString(decoderResult);
        assertEquals(objectMapper.readTree(expectedJson), objectMapper.readTree(outputJson));
    }

    @Test
    public void nullFieldsShouldBeIgnoredOnSerialization_FailCase() throws Exception {
        DecoderResult decoderResult = new DecoderResult();
        decoderResult.setMessage("\"DecoderInputData is missing mandatory fields: 'sourceDeviceId', 'sourceDeviceEui', 'inputData', 'manufacturer and/or model'\"");
        ObjectMapper objectMapper = new ObjectMapper();

        String expectedJson = "{\"self\":null, \"alarms\":null, \"alarmTypesToUpdate\":null, \"events\":null, \"measurements\":null, \"dataFragments\":null, \"message\":\"\\\"DecoderInputData is missing mandatory fields: 'sourceDeviceId', 'sourceDeviceEui', 'inputData', 'manufacturer and/or model'\\\"\",\"success\":true}";
        String outputJson = objectMapper.writeValueAsString(decoderResult);
        assertNotEquals(objectMapper.readTree(expectedJson), objectMapper.readTree(outputJson));
    }
}