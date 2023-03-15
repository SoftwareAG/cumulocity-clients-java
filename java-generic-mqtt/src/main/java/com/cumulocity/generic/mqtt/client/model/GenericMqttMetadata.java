package com.cumulocity.generic.mqtt.client.model;

import lombok.*;

import java.util.Map;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class GenericMqttMetadata {

    private String clientId;
    private boolean dupFlag;
    private Map<String, String> userProperties;
    private PayloadFormatIndicator payloadFormatIndicator;
    private String contentType;
    private byte[] correlationData;
    private String responseTopic;
}
