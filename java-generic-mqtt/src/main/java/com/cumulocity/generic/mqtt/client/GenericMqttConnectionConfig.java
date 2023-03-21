package com.cumulocity.generic.mqtt.client;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class GenericMqttConnectionConfig {

    private final static long DEFAULT_CONNECTION_TIMEOUT_IN_MILLIS = 2000;

    /**
     * Specify the topic to which instance of {@link GenericMqttFactory#buildPublisher(GenericMqttConnectionConfig)} or {@link GenericMqttFactory#buildSubscriber(GenericMqttConnectionConfig)} will connect to.
     */
    private final String topic;


    /**
     * Connection timeout in millis second until the websocket connected or failed to do so.
     */
    @Builder.Default
    private long connectionTimeoutInMillis = DEFAULT_CONNECTION_TIMEOUT_IN_MILLIS;
}
