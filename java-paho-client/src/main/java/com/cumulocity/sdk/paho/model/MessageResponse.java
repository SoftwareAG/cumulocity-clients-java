package com.cumulocity.sdk.paho.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MessageResponse {

    private String topicName;

    private String clientId;

    private int qos;

    private String messageContent;
}
