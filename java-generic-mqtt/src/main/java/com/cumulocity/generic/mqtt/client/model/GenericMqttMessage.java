package com.cumulocity.generic.mqtt.client.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Generic MQTT message. This message contains payload and the associated {@link GenericMqttMetadata metadata}.
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class GenericMqttMessage {

    /**
     * The payload of this message as a byte array. Empty byte array if the payload is not present.
     */
    private byte[] payload;

    /**
     * The metadata of this message.
     */
    private GenericMqttMetadata metadata;

}
