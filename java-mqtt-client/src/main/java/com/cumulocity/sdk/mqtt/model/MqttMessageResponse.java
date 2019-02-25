package com.cumulocity.sdk.mqtt.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MqttMessageResponse {

    private String topicName;

    private String clientId;

    private int qos;

    private String messageContent;
}
