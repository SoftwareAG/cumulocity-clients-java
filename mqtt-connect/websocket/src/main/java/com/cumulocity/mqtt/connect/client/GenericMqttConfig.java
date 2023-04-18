package com.cumulocity.mqtt.connect.client;

import com.cumulocity.mqtt.connect.client.websocket.GenericMqttWebSocketConfig;
import com.cumulocity.mqtt.connect.client.websocket.GenericMqttWebSocketConfig.GenericMqttWebSocketConfigBuilder;

/**
 * {@link GenericMqttConfig} provides interface for configuring instances of {@link GenericMqttPublisher} and {@link GenericMqttSubscriber}
 */
public interface GenericMqttConfig {

    static GenericMqttWebSocketConfigBuilder webSocket() {
        return GenericMqttWebSocketConfig.builder();
    }

    String getTopic();

}
