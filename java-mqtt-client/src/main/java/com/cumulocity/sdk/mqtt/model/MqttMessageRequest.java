package com.cumulocity.sdk.mqtt.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MqttMessageRequest {

    private String topicName;

    private QoS qoS;

    private String messageContent;
}
