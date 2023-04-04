package com.cumulocity.generic.mqtt.client.websocket;

import lombok.Data;

@Data
class WebSocketConfig {

    private String topic;
    private long connectionTimeout;

}
