package com.cumulocity.mqtt.connect.client.websocket;

import com.cumulocity.mqtt.connect.client.GenericMqttConfig;
import com.cumulocity.mqtt.connect.client.GenericMqttPublisher;
import com.cumulocity.mqtt.connect.client.GenericMqttSubscriber;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class GenericMqttWebSocketConfig implements GenericMqttConfig {

    private final static long DEFAULT_CONNECTION_TIMEOUT_MILLIS = 2000;

    /**
     * Specify the topic to which instance of {@link GenericMqttPublisher} or {@link GenericMqttSubscriber} will connect to.
     */
    private final String topic;

    /**
     * Connection timeout in millis second until the websocket connected or failed to do so.
     */
    @Builder.Default
    private long connectionTimeout = DEFAULT_CONNECTION_TIMEOUT_MILLIS;

    public static class GenericMqttWebSocketConfigBuilder {
    }
}