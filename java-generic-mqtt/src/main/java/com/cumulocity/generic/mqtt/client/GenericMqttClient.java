package com.cumulocity.generic.mqtt.client;

import com.cumulocity.generic.mqtt.client.websocket.GenericMqttWebSocketClientBuilder;

/**
 * {@link GenericMqttClient} is used to configure and create instances of {@link GenericMqttPublisher} and {@link GenericMqttSubscriber}.
 */
public interface GenericMqttClient extends AutoCloseable {

    static GenericMqttWebSocketClientBuilder webSocket() {
        return GenericMqttWebSocketClientBuilder.builder();
    }

    /**
     * Creates an instance of <code>GenericMqttPublisher</code> with the configured topic.
     *
     * @param config <code>GenericMqttConfig</code>
     * @return the instance of <code>GenericMqttPublisher</code>
     */
    GenericMqttPublisher buildPublisher(GenericMqttConfig config);

    /**
     * Creates an instance of <code>GenericMqttSubscriber</code> with the configured topic.
     *
     * @param config <code>GenericMqttConfig</code>
     * @return the instance of <code>GenericMqttSubscriber</code>
     */
    GenericMqttSubscriber buildSubscriber(GenericMqttConfig config);

}
