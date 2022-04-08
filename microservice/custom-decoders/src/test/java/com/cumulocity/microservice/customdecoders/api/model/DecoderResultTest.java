package com.cumulocity.microservice.customdecoders.api.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;

public class DecoderResultTest {
    @Test
    public void nullFieldsShouldBeIgnoredOnSerialization() throws JsonProcessingException {
        DecoderResult decoderResult = new DecoderResult();
        decoderResult.setMessage("\"DecoderInputData is missing mandatory fields: 'sourceDeviceId', 'sourceDeviceEui', 'inputData', 'manufacturer and/or model'\"");
        ObjectMapper objectMapper = new ObjectMapper();

        String outputJson = objectMapper.writeValueAsString(decoderResult);
        assertThat(outputJson, containsString("message"));
        assertThat(outputJson, containsString("success"));
        assertThat(outputJson, not(containsString("alarms")));
        assertThat(outputJson, not(containsString("alarmTypesToUpdate")));
        assertThat(outputJson, not(containsString("events")));
        assertThat(outputJson, not(containsString("measurements")));
        assertThat(outputJson, not(containsString("dataFragments")));
        assertThat(outputJson, not(containsString("self")));
    }
}