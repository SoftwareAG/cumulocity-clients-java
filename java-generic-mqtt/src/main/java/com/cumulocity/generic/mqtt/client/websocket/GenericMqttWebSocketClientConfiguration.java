package com.cumulocity.generic.mqtt.client.websocket;

import lombok.Data;

@Data
class GenericMqttWebSocketClientConfiguration {

    private final static long DEFAULT_CONNECTION_TIMEOUT_IN_MILLIS = 2000;

    private String topic;
    private long connectionTimeout = DEFAULT_CONNECTION_TIMEOUT_IN_MILLIS;
}
