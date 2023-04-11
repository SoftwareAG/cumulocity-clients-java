package com.cumulocity.generic.mqtt.client.websocket;

import com.cumulocity.generic.mqtt.client.GenericMqttConfig;
import com.cumulocity.generic.mqtt.client.GenericMqttPublisher;
import com.cumulocity.generic.mqtt.client.GenericMqttSubscriber;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class GenericMqttWebSocketConfig implements GenericMqttConfig {

    private final static long DEFAULT_CONNECTION_TIMEOUT_IN_MILLIS = 2000;

    /**
     * Specify the topic to which instance of {@link GenericMqttPublisher} or {@link GenericMqttSubscriber} will connect to.
     */
    private final String topic;

    /**
     * Connection timeout in millis second until the websocket connected or failed to do so.
     */
    @Builder.Default
    private long connectionTimeout = DEFAULT_CONNECTION_TIMEOUT_IN_MILLIS;

    public static class GenericMqttWebSocketConfigBuilder {
    }
}