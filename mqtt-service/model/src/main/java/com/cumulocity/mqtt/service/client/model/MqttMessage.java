package com.cumulocity.mqtt.service.client.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * MQTT message representation containing a payload and associated {@link MqttMetadata metadata}.
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class MqttMessage {

    private byte[] payload;

    private MqttMetadata metadata;

    /**
     * @return the payload of this message as a byte array. This will be an empty array if the payload is not present.
     */
    public byte[] getPayload() {
        return this.payload;
    }

    /**
     * @return the metadata of this message.
     */
    public MqttMetadata getMetadata() {
        return this.metadata;
    }

}
