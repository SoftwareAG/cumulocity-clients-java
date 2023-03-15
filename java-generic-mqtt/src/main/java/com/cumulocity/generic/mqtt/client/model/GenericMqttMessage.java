package com.cumulocity.generic.mqtt.client.model;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class GenericMqttMessage {

    private byte[] payload;
    private GenericMqttMetadata metadata;

}
