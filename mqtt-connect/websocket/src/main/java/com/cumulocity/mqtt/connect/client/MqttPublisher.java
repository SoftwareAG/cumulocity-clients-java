package com.cumulocity.mqtt.connect.client;

import com.cumulocity.mqtt.connect.client.model.MqttMessage;

/**
 * Interface for publishing messages to the server.
 * <p>
 * {@link MqttPublisher} instances are created using {@link MqttClient#buildPublisher(MqttConfig)}.
 */
public interface MqttPublisher extends AutoCloseable {
    /**
     * Sends {@link MqttMessage message} to the MQTT Connect.
     *
     * @param message The {@link MqttMessage} to be published.
     */
    void publish(MqttMessage message);

    @Override
    void close();

}
