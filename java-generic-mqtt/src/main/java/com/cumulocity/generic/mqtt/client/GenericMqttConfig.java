package com.cumulocity.generic.mqtt.client;

import com.cumulocity.generic.mqtt.client.websocket.GenericMqttWebSocketConfig;
import com.cumulocity.generic.mqtt.client.websocket.GenericMqttWebSocketConfig.GenericMqttWebSocketConfigBuilder;

public interface GenericMqttConfig {

    static GenericMqttWebSocketConfigBuilder webSocket() {
        return GenericMqttWebSocketConfig.builder();
    }

    String getTopic();

}
