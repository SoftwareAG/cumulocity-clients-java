package com.cumulocity.mqtt.connect.client.websocket;

import com.cumulocity.mqtt.connect.client.MqttClientException;
import com.cumulocity.mqtt.connect.client.MqttMessageListener;
import com.cumulocity.mqtt.connect.client.MqttSubscriber;
import com.cumulocity.sdk.client.messaging.notifications.TokenApi;

import java.net.URI;
import java.util.concurrent.TimeUnit;

class MqttWebSocketSubscriber implements MqttSubscriber {

    private final static String SUBSCRIBER = "mqttConnectSubscriber";
    private final static String WEBSOCKET_URL_PATTERN = "%s/notification2/consumer/?token=%s";

    private final String webSocketBaseUrl;
    private final MqttWebSocketConfig config;
    private final TokenSupplier tokenSupplier;

    private MqttWebSocketClient consumer;

    MqttWebSocketSubscriber(String webSocketBaseUrl, TokenApi tokenApi, MqttWebSocketConfig config) {
        this.webSocketBaseUrl = webSocketBaseUrl;
        this.config = config;
        this.tokenSupplier = new TokenSupplier(tokenApi, config.getTopic(), config.getSubscriber());
    }

    @Override
    public void subscribe(MqttMessageListener listener) {
        if (consumer != null) {
            return;
        }

        final String token = tokenSupplier.get();
        if (token == null) {
            throw new MqttClientException(String.format("Token could not be created for topic %s", config.getTopic()));
        }
        try {
            final URI uri = new URI(String.format(WEBSOCKET_URL_PATTERN, webSocketBaseUrl, token));
            consumer = new MqttWebSocketClient(uri, listener);
            consumer.connectBlocking(config.getConnectionTimeout(), TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            throw new MqttClientException("WebSocket connection could not be established!", e);
        }
    }

    @Override
    public void close() {
        if (consumer != null) {
            consumer.close();
        }
    }
}
