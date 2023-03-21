package com.cumulocity.generic.mqtt.client.websocket;

import com.cumulocity.generic.mqtt.client.*;
import com.cumulocity.generic.mqtt.client.model.GenericMqttMessage;
import com.cumulocity.sdk.client.messaging.notifications.TokenApi;

public class GenericMqttWebSocketFactory implements GenericMqttFactory {
    private final String webSocketBaseUrl;
    private final TokenApi tokenApi;

    /**
     * Constructs a GenericMqttWebSocketFactory instance and sets it to connect to the specified URI. The
     * channel does not attempt to connect automatically. The connection will only be established once you
     * obtain the instance of {@link GenericMqttPublisher} or {@link GenericMqttSubscriber} and invoke
     * {@link GenericMqttPublisher#publish(GenericMqttMessage)} or {@link GenericMqttSubscriber#subscribe(GenericMqttMessageListener)}
     *
     * @param webSocketBaseUrl the server URI to connect to
     * @param tokenApi         see {@link TokenApi}
     */
    public GenericMqttWebSocketFactory(String webSocketBaseUrl, TokenApi tokenApi) {
        this.webSocketBaseUrl = webSocketBaseUrl;
        this.tokenApi = tokenApi;
    }

    @Override
    public GenericMqttPublisher buildPublisher(GenericMqttConnectionConfig config) {
        return new GenericMqttWebSocketPublisher(webSocketBaseUrl, tokenApi, config);
    }

    @Override
    public GenericMqttSubscriber buildSubscriber(GenericMqttConnectionConfig config) {
        return new GenericMqttWebSocketSubscriber(webSocketBaseUrl, tokenApi, config);
    }
}
