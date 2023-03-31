package com.cumulocity.generic.mqtt.client.websocket;

import com.cumulocity.generic.mqtt.client.GenericMqttClient;
import com.cumulocity.generic.mqtt.client.GenericMqttConfig;
import com.cumulocity.generic.mqtt.client.GenericMqttPublisher;
import com.cumulocity.generic.mqtt.client.GenericMqttSubscriber;
import com.cumulocity.sdk.client.messaging.notifications.TokenApi;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class GenericMqttWebSocketClient implements GenericMqttClient {

    private final String url;
    private final TokenApi tokenApi;

    @Override
    public GenericMqttPublisher buildPublisher(final GenericMqttConfig config) {
        return new GenericMqttWebSocketPublisher(url, tokenApi, config);
    }

    @Override
    public GenericMqttSubscriber buildSubscriber(final GenericMqttConfig config) {
        return new GenericMqttWebSocketSubscriber(url, tokenApi, config);
    }

    @Override
    public void close() throws Exception {
    }
}
