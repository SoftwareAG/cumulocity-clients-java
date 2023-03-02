package com.cumulocity.generic.mqtt;

import lombok.Data;

@Data
public class GenericMqttMessage {

    private final byte[] payload;
    private final GenericMqttMetadata metadata;

}
