package com.cumulocity.mqtt.connect.client;

import com.cumulocity.mqtt.connect.client.websocket.MqttWebSocketConfig;
import com.cumulocity.mqtt.connect.client.websocket.MqttWebSocketConfig.MqttWebSocketConfigBuilder;

/**
 * {@link MqttConfig} provides interface for configuring instances of {@link MqttPublisher} and {@link MqttSubscriber}
 */
public interface MqttConfig {

    static MqttWebSocketConfigBuilder webSocket() {
        return MqttWebSocketConfig.builder();
    }

    String getTopic();

}
