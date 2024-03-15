package com.cumulocity.mqtt.service.client.websocket;

import com.cumulocity.sdk.client.messaging.notifications.TokenApi;
import lombok.Data;

@Data
class WebSocketClientConfig {

    private static final long DEFAULT_CONNECTION_TIMEOUT_MILLIS = 2000;

    private String baseUrl;
    private TokenApi tokenApi;
    private long connectionTimeout = DEFAULT_CONNECTION_TIMEOUT_MILLIS;

}
