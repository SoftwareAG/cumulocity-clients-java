package com.cumulocity.generic.mqtt.client;

import com.cumulocity.generic.mqtt.client.websocket.GenericMqttWebSocketConfig;
import com.cumulocity.generic.mqtt.client.websocket.GenericMqttWebSocketConfig.GenericMqttWebSocketConfigBuilder;

/**
 * {@link GenericMqttConfig} provides interface for configuring instances of {@link GenericMqttPublisher} and {@link GenericMqttSubscriber}
 */
public interface GenericMqttConfig {

    static GenericMqttWebSocketConfigBuilder webSocket() {
        return GenericMqttWebSocketConfig.builder();
    }

    String getTopic();

}
