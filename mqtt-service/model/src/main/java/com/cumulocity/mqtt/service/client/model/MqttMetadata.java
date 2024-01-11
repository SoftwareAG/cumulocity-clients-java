package com.cumulocity.mqtt.service.client.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * Represents the Metadata of a {@link MqttMessage}
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class MqttMetadata {

    private String clientId;

    private int messageId;

    private boolean dupFlag;

    private Map<String, String> userProperties;

    private PayloadFormatIndicator payloadFormatIndicator;

    private String contentType;

    private byte[] correlationData;

    private String responseTopic;

    private String topic;

    /**
     * @return the client id of the MQTT client.
     */
    public String getClientId() {
        return this.clientId;
    }

    /**
     * @return the message id.
     */
    public int getMessageId() {
        return this.messageId;
    }

    /**
     * The duplicate delivery flag.
     * <p>
     *
     * @return false if this is the first occasion the message is sent to the receiver, true otherwise.
     */
    public boolean isDupFlag() {
        return this.dupFlag;
    }

    /**
     * MQTT 5 property, for MQTT 3 this will be always null.
     *
     * @return the user properties of an MQTT payload.
     */
    public Map<String, String> getUserProperties() {
        return this.userProperties;
    }

    /**
     * MQTT 5 property, for MQTT 3 this will be always null.
     *
     * @return indicator for the payload.
     */
    public PayloadFormatIndicator getPayloadFormatIndicator() {
        return this.payloadFormatIndicator;
    }

    /**
     * MQTT 5 property, for MQTT 3 this will be always null.
     *
     * @return the content type.
     */
    public String getContentType() {
        return this.contentType;
    }

    /**
     * MQTT 5 property, for MQTT 3 this will be always null.
     *
     * @return the correlation data
     */
    public byte[] getCorrelationData() {
        return this.correlationData;
    }

    /**
     * MQTT 5 property, for MQTT 3 this will be always null.
     *
     * @return the response topic
     */
    public String getResponseTopic() {
        return this.responseTopic;
    }

    /**
     * Retrieves the MQTT topic. If necessary, this property must be explicitly configured when employing an MQTT WebSocket Producer.
     *
     * @return the MQTT topic
     */
    public String getTopic() {
        return topic;
    }
}
