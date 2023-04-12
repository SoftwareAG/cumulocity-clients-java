package com.cumulocity.generic.mqtt.client.websocket;

import com.cumulocity.generic.mqtt.client.GenericMqttMessageListener;
import com.cumulocity.generic.mqtt.client.GenericMqttSubscriber;
import com.cumulocity.generic.mqtt.client.exception.GenericMqttClientException;
import com.cumulocity.sdk.client.messaging.notifications.TokenApi;

import java.net.URI;
import java.util.concurrent.TimeUnit;

class GenericMqttWebSocketSubscriber implements GenericMqttSubscriber {

    private final static String SUBSCRIBER = "javaGenericMqttWSClientSub";
    private final static String WEBSOCKET_URL_PATTERN = "%s/notification2/consumer/?token=%s";

    private final String webSocketBaseUrl;
    private final GenericMqttWebSocketConfig config;
    private final TokenSupplier tokenSupplier;

    private GenericMqttWebSocketClient consumer;

    GenericMqttWebSocketSubscriber(String webSocketBaseUrl, TokenApi tokenApi, GenericMqttWebSocketConfig config) {
        this.webSocketBaseUrl = webSocketBaseUrl;
        this.config = config;
        this.tokenSupplier = new TokenSupplier(tokenApi, config.getTopic(), SUBSCRIBER);
    }

    @Override
    public void subscribe(GenericMqttMessageListener listener) {
        if (consumer != null) {
            return;
        }

        final String token = tokenSupplier.get();

        if (token == null) {
            throw new GenericMqttClientException(String.format("Token could not be created for topic %s", config.getTopic()));
        }

        try {
            final URI uri = new URI(String.format(WEBSOCKET_URL_PATTERN, webSocketBaseUrl, token));
            consumer = new GenericMqttWebSocketClient(uri, listener);
            consumer.connectBlocking(config.getConnectionTimeout(), TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            throw new GenericMqttClientException("WebSocket connection could not be established!", e);
        }
    }

    @Override
    public void close() throws Exception {
        if (consumer != null) {
            consumer.close();
        }
    }
}
