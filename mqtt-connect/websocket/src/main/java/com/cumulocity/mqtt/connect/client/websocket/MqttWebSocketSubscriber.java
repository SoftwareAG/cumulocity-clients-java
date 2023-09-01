package com.cumulocity.mqtt.connect.client.websocket;

import com.cumulocity.mqtt.connect.client.MqttClientException;
import com.cumulocity.mqtt.connect.client.MqttMessageListener;
import com.cumulocity.mqtt.connect.client.MqttSubscriber;
import com.cumulocity.sdk.client.messaging.notifications.TokenApi;

import java.net.URI;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

class MqttWebSocketSubscriber implements MqttSubscriber {

    private final static String WEBSOCKET_URL_PATTERN = "%s/notification2/consumer/?token=%s";

    private final String webSocketBaseUrl;
    private final TokenApi tokenApi;
    private final MqttWebSocketConfig config;
    private final TokenSupplier tokenSupplier;

    private WebSocketConsumer consumer;

    MqttWebSocketSubscriber(String webSocketBaseUrl, TokenApi tokenApi, MqttWebSocketConfig config) {
        this.webSocketBaseUrl = webSocketBaseUrl;
        this.tokenApi = tokenApi;
        this.config = config;
        this.tokenSupplier = new TokenSupplier(tokenApi, config.getTopic(), config.getSubscriber());
    }

    @Override
    public void subscribe(MqttMessageListener listener) {
        if (consumer != null) {
            return;
        }

        final String token = tokenSupplier.getToken().getTokenString();

        if (token == null) {
            throw new MqttClientException(String.format("Token could not be created for topic %s", config.getTopic()));
        }
        try {
            final URI uri = new URI(String.format(WEBSOCKET_URL_PATTERN, webSocketBaseUrl, token));
            consumer = new WebSocketConsumer(uri, listener);
            consumer.connectBlocking(config.getConnectionTimeout(), TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            throw new MqttClientException("WebSocket connection could not be established!", e);
        }
    }

    @Override
    public void unsubscribe() {
        tokenApi.unsubscribe(tokenSupplier.getToken());
    }

    @Override
    public void close() {
        if (consumer != null) {
            consumer.close();
        }
    }

    private static final class WebSocketConsumer extends AbstractWebSocketClient {

        private final MqttMessageListener listener;

        WebSocketConsumer(URI serverUri, MqttMessageListener listener) {
            super(serverUri);
            this.listener = listener;
        }

        @Override
        public void onMessage(String message) {
            final WebSocketMessage webSocketMessage = WebSocketMessage.parse(message);

            final Optional<String> ackHeader = webSocketMessage.getAckHeader();
            final byte[] avroPayload = webSocketMessage.getAvroPayload();

            listener.onMessage(MqttMessageConverter.decode(avroPayload));

            ackHeader.ifPresent(this::send);
        }

        @Override
        public void onError(Exception e) {
            super.onError(e);
            listener.onError(e);
        }

    }

}
