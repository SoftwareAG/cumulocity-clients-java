package com.cumulocity.generic.mqtt.client;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class GenericMqttConnectionConfig {
    private final String topic;
    private final long connectionTimeoutInMilliSeconds;
}
