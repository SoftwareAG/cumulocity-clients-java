package com.cumulocity.mqtt.connect.client;

/**
 * Interface for subscribing to messages from the server.
 * <p>
 * {@link MqttSubscriber} instances are created using {@link MqttClient#buildSubscriber(MqttConfig)}
 */
public interface MqttSubscriber extends AutoCloseable {

    /**
     * Starts this subscriber, using the parameter {@link MqttMessageListener} to handle its received messages
     *
     * @param listener the listener object
     */
    void subscribe(MqttMessageListener listener);

    @Override
    void close();

}
