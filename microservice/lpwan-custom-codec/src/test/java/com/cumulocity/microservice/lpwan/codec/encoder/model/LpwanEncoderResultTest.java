package com.cumulocity.microservice.lpwan.codec.encoder.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;
import static org.junit.jupiter.api.Assertions.*;

public class LpwanEncoderResultTest {
    @Test
    public void fportShouldNotAppearTwiceOnSerialization() throws JsonProcessingException {
        LpwanEncoderResult lpwanEncoderResult = new LpwanEncoderResult();
        lpwanEncoderResult.setEncodedCommand("9F000000");
        Map<String, String> properties = new HashMap<>();
        properties.put("fport", "20");
        lpwanEncoderResult.setProperties(properties);
        lpwanEncoderResult.setSuccess(true);
        lpwanEncoderResult.setMessage("Successfully Encoded the payload");

        ObjectMapper objectMapper = new ObjectMapper();
        String outputJson = objectMapper.writeValueAsString(lpwanEncoderResult);
        assertTrue(outputJson.contains("\"properties\":{\"fport\":\"20\"}"));
        // Used to appear in form {"encodedCommand":"9F000000","properties":{"fport":"20"},"message":"Successfully Encoded the payload","success":true,"fport":20} when @JsonIgnore was missing
        assertFalse(outputJson.contains(",\"fport\":20"));
    }

    @Test
    public void nullFieldsShouldBeIgnoredOnSerialization() throws JsonProcessingException {
        LpwanEncoderResult lpwanEncoderResult = new LpwanEncoderResult();
        lpwanEncoderResult.setMessage("\"EncoderInputData is missing mandatory fields: 'sourceDeviceId', 'sourceDeviceEui', 'manufacturer, model and/or supportedCommands', 'commandName'\"");
        ObjectMapper objectMapper = new ObjectMapper();

        String outputJson = objectMapper.writeValueAsString(lpwanEncoderResult);
        assertThat(outputJson, containsString("message"));
        assertThat(outputJson, containsString("success"));
        assertThat(outputJson, not(containsString("encodedCommand")));
        assertThat(outputJson, not(containsString("properties")));
    }

    @Test
    public void deserializedObjectShouldHaveFPort() throws JsonProcessingException {
        String inputJson = "{\"encodedCommand\":\"9F000000\",\"properties\":{\"fport\":\"20\"},\"message\":\"Successfully Encoded the payload\",\"success\":true}";
        ObjectMapper objectMapper = new ObjectMapper();
        LpwanEncoderResult lpwanEncoderResult = objectMapper.readValue(inputJson, LpwanEncoderResult.class);
        assertEquals(20, lpwanEncoderResult.getFport());
    }
}