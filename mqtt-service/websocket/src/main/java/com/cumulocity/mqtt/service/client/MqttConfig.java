package com.cumulocity.mqtt.service.client;

import com.cumulocity.mqtt.service.client.websocket.MqttWebSocketConfig;

/**
 * {@link MqttConfig} provides interface for configuring instances of {@link MqttPublisher} and {@link MqttSubscriber}
 */
public interface MqttConfig {

    static MqttWebSocketConfig.MqttWebSocketConfigBuilder webSocket() {
        return MqttWebSocketConfig.builder();
    }

    String getTopic();

    String getSubscriber();
}
