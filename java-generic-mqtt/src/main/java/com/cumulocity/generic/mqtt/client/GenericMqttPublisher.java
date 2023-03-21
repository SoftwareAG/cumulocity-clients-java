package com.cumulocity.generic.mqtt.client;

import com.cumulocity.generic.mqtt.client.model.GenericMqttMessage;

/**
 * <code>GenericMqttPublisher</code> is used to publish messages to a topic.
 * <p>
 * Topic is configured when obtaining the instance of <code>GenericMqttPublisher</code> using {@link GenericMqttFactory#buildPublisher(GenericMqttConnectionConfig)}
 */
public interface GenericMqttPublisher extends AutoCloseable {
    /**
     * Sends {@link GenericMqttMessage genericMqttMessage} to the connected websocket server.
     *
     * @param genericMqttMessage The <code>GenericMqttMessage</code> which will be published.
     */
    void publish(GenericMqttMessage genericMqttMessage);
}
