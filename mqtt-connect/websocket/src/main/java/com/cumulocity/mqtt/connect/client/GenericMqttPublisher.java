package com.cumulocity.mqtt.connect.client;

import com.cumulocity.mqtt.connect.client.model.GenericMqttMessage;

/**
 * Interface for publishing messages to the server.
 * <p>
 * <code>GenericMqttPublisher</code> instances are setup using {@link GenericMqttClient#buildPublisher(GenericMqttConfig)}.
 */
public interface GenericMqttPublisher extends AutoCloseable {
    /**
     * Sends {@link GenericMqttMessage message} to the Generic MQTT.
     *
     * @param message The <code>GenericMqttMessage</code> to be published.
     */
    void publish(GenericMqttMessage message);
}
