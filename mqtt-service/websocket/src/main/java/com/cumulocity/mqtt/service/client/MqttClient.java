package com.cumulocity.mqtt.service.client;

import com.cumulocity.mqtt.service.client.websocket.MqttWebSocketClientBuilder;

/**
 * {@link MqttClient} is used to configure and create instances of {@link MqttPublisher} and {@link MqttSubscriber}.
 */
public interface MqttClient extends AutoCloseable {

    static MqttWebSocketClientBuilder webSocket() {
        return MqttWebSocketClientBuilder.builder();
    }

    /**
     * Creates an instance of {@link MqttPublisher} with the configured topic.
     *
     * @param config {@link MqttConfig}
     * @return the instance of {@link MqttPublisher}
     */
    MqttPublisher buildPublisher(MqttConfig config);

    /**
     * Creates an instance of {@link MqttSubscriber} with the configured topic.
     *
     * @param config {@link MqttConfig}
     * @return the instance of {@link MqttSubscriber}
     */
    MqttSubscriber buildSubscriber(MqttConfig config);

    @Override
    void close();

}
