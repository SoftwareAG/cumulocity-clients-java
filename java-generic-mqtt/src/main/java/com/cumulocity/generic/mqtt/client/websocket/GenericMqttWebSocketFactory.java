package com.cumulocity.generic.mqtt.client.websocket;

import com.cumulocity.generic.mqtt.client.GenericMqttConnectionConfig;
import com.cumulocity.generic.mqtt.client.GenericMqttFactory;
import com.cumulocity.generic.mqtt.client.GenericMqttPublisher;
import com.cumulocity.generic.mqtt.client.GenericMqttSubscriber;
import com.cumulocity.sdk.client.messaging.notifications.TokenApi;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class GenericMqttWebSocketFactory implements GenericMqttFactory {

    private final String webSocketBaseUrl;
    private final TokenApi tokenApi;

    @Override
    public GenericMqttPublisher buildPublisher(GenericMqttConnectionConfig config) {
        return new GenericMqttWebSocketPublisher(webSocketBaseUrl, tokenApi, config);
    }

    @Override
    public GenericMqttSubscriber buildSubscriber(GenericMqttConnectionConfig config) {
        return new GenericMqttWebSocketSubscriber(webSocketBaseUrl, tokenApi, config);
    }
}
