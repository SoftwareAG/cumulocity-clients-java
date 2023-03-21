package com.cumulocity.generic.mqtt.client;

/**
 * <code>GenericMqttSubscriber</code> is used subscribe to messages on a topic.
 * <p>
 * Topic is configured when obtaining the instance of <code>GenericMqttSubscriber</code> using {@link GenericMqttFactory#buildSubscriber(GenericMqttConnectionConfig)}
 */
public interface GenericMqttSubscriber extends AutoCloseable {

    /**
     * Sets a {@link GenericMqttMessageListener} for the subscriber
     * <p>
     * When a {@link GenericMqttMessageListener} is set, application will receive messages through it.
     *
     * @param messageListener the listener object
     */
    void subscribe(GenericMqttMessageListener messageListener);
}
