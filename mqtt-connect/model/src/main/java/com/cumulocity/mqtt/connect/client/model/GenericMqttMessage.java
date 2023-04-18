package com.cumulocity.mqtt.connect.client.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * A Generic MQTT message containing a payload and associated {@link GenericMqttMetadata metadata}.
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class GenericMqttMessage {

    /**
     * The payload of this message as a byte array. This will be an empty array if the payload is not present.
     */
    private byte[] payload;

    /**
     * The metadata of this message.
     */
    private GenericMqttMetadata metadata;

}
