package com.cumulocity.generic.mqtt.client.websocket;

import com.cumulocity.generic.mqtt.client.*;
import com.cumulocity.generic.mqtt.client.model.GenericMqttMessage;
import com.cumulocity.sdk.client.messaging.notifications.TokenApi;

import static com.google.common.base.Preconditions.checkNotNull;

public class GenericMqttWebSocketClientBuilder implements GenericMqttClientBuilder {

    private String url;
    private TokenApi tokenApi;

    @Override
    public GenericMqttWebSocketClientBuilder url(final String url) {
        this.url = url;
        return this;
    }

    @Override
    public GenericMqttWebSocketClientBuilder tokenApi(final TokenApi tokenApi) {
        this.tokenApi = tokenApi;
        return this;
    }

    @Override
    public GenericMqttClient build() {
        checkNotNull(url, "Web socket url has to be provided");
        checkNotNull(tokenApi, "Token API cannot be null");
        if (!url.startsWith("ws://")) {
            url = "ws://" + url;
        }
        return new GenericMqttWebSocketClient(url, tokenApi);
    }

}
