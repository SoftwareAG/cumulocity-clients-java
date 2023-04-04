package com.cumulocity.generic.mqtt.client;

import com.cumulocity.generic.mqtt.client.websocket.GenericMqttWebSocketClientBuilder;

/**
 * {@link GenericMqttClient} is used to configure and create instances of {@link GenericMqttPublisher} and {@link GenericMqttSubscriber}.
 */
public interface GenericMqttClient extends AutoCloseable {

    /**
     * Creates a builder for {@link GenericMqttClient}.
     *
     * @return the created builder
     */
    static GenericMqttClientBuilder builder() {
        return new GenericMqttWebSocketClientBuilder();
    }

    /**
     * Starts creation of new {@link GenericMqttPublisher} with returned {@link GenericMqttPublisherBuilder}.
     */
    GenericMqttPublisherBuilder newPublisher();

    /**
     * Starts creation of new {@link GenericMqttSubscriber} with returned {@link GenericMqttSubscriberBuilder}.
     */
    GenericMqttSubscriberBuilder newSubscriber();

}
