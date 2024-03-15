package com.cumulocity.mqtt.service.client;

import com.cumulocity.mqtt.service.client.model.MqttMessage;

/**
 * Interface for publishing messages to the server.
 * <p>
 * {@link MqttPublisher} instances are created using {@link MqttClient#buildPublisher(PublisherConfig)}.
 */
public interface MqttPublisher extends AutoCloseable {

    /**
     * Reconnect the publisher by closing existing connection if needed and starting the new connection.
     */
    void reconnect() throws MqttClientException;

    /**
     * Sends {@link MqttMessage message} to the MQTT Service.
     *
     * @param message The {@link MqttMessage} to be published.
     */
    void publish(MqttMessage message);

    /**
     * Checks client connection status.
     *
     * @return true if the client is connected to the server, false otherwise.
     */
    boolean isConnected();

    @Override
    void close();

}
