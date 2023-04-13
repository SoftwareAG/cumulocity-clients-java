package com.cumulocity.generic.mqtt.client;

/**
 * Interface for subscribing to messages from the server.
 * <p>
 * <code>GenericMqttSubscriber</code> instances are setup using {@link GenericMqttClient#buildSubscriber(GenericMqttConfig)}
 */
public interface GenericMqttSubscriber extends AutoCloseable {

    /**
     * Sets a <code>GenericMqttMessageListener</code> for the subscriber
     * <p>
     * When a <code>GenericMqttMessageListener</code> is set, application will receive messages through it.
     *
     * @param listener the listener object
     */
    void subscribe(GenericMqttMessageListener listener);
}
