package com.cumulocity.mqtt.connect.client.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * Represents the Metadata of a {@link GenericMqttMessage}
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class GenericMqttMetadata {

    /**
     * The client id of the MQTT client.
     */
    private String clientId;

    /**
     * The message id.
     */
    private int messageId;

    /**
     * The duplicate delivery flag.
     * <p>
     *
     * @return false this is the first occasion the message is sent to the receiver, true otherwise.
     */
    private boolean dupFlag;

    /**
     * The user properties of an MQTT payload.
     */
    private Map<String, String> userProperties;

    /**
     * Indicator for the payload.
     */
    private PayloadFormatIndicator payloadFormatIndicator;

    /**
     * This is the content type.
     * <p>
     * For an MQTT 3 PUBLISH this MQTT 5 property will always be null.
     */
    private String contentType;

    /**
     * This is the correlation data.
     * <p>
     * For an MQTT 3 PUBLISH this MQTT 5 property will always be null.
     */
    private byte[] correlationData;

    /**
     * This is the response topic.
     * <p>
     * For an MQTT 3 PUBLISH this MQTT 5 property will always be null.
     */
    private String responseTopic;
}
