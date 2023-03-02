package com.cumulocity.generic.mqtt;

import lombok.Data;

import java.util.Map;

@Data
public class GenericMqttMetadata {

    private final String clientId;
    private final boolean dupFlag;
    private final Map<String, String> userProperties;
    private final PayloadFormatIndicator payloadFormatIndicator;
    private final String contentType;
    private final byte[] correlationData;
    private final String responseTopic;
}
