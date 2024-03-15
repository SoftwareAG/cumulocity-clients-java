package com.cumulocity.mqtt.service.client;

/**
 * Interface for subscribing to messages from the server.
 * <p>
 * {@link MqttSubscriber} instances are created using {@link MqttClient#buildSubscriber(SubscriberConfig)}
 */
public interface MqttSubscriber extends AutoCloseable {

    /**
     * Starts this subscriber, using the parameter {@link MessageListener} to handle its received messages
     *
     * @param listener the listener object
     */
    void subscribe(MessageListener listener) throws MqttClientException;

    /**
     * Resubscribes the subscriber by closing the current connection and subscribing again
     */
    void resubscribe() throws MqttClientException;

    /**
     * Unsubscribes the subscriber
     */
    void unsubscribe();

    /**
     * Checks client connection status.
     *
     * @return true if the client is connected to the server, false otherwise.
     */
    boolean isConnected();

    @Override
    void close();

}
