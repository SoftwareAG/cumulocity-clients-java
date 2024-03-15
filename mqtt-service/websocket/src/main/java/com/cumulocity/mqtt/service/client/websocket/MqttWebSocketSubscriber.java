package com.cumulocity.mqtt.service.client.websocket;

import com.cumulocity.mqtt.service.client.MessageListener;
import com.cumulocity.mqtt.service.client.MqttClientException;
import com.cumulocity.mqtt.service.client.MqttSubscriber;
import com.cumulocity.mqtt.service.client.SubscriberConfig;

import java.net.URI;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

class MqttWebSocketSubscriber implements MqttSubscriber {

    private final static String WEBSOCKET_URL_PATTERN = "%s/notification2/consumer/?token=%s";

    private final WebSocketClientConfig clientConfig;
    private final SubscriberConfig subscriberConfig;
    private final TokenSupplier tokenSupplier;

    private WebSocketConsumer consumer;

    MqttWebSocketSubscriber(WebSocketClientConfig clientConfig, SubscriberConfig subscriberConfig) {
        this.clientConfig = clientConfig;
        this.subscriberConfig = subscriberConfig;
        this.tokenSupplier = new TokenSupplier(clientConfig.getTokenApi(), subscriberConfig.getTopic(), subscriberConfig.getSubscriber());
    }

    @Override
    public void subscribe(MessageListener listener) {
        if (consumer != null) {
            return;
        }

        final String token = tokenSupplier.getToken().getTokenString();
        if (token == null) {
            throw new MqttClientException(String.format("Token could not be created for topic %s", subscriberConfig.getTopic()));
        }
        try {
            final URI uri = new URI(String.format(WEBSOCKET_URL_PATTERN, clientConfig.getBaseUrl(), token));
            consumer = new WebSocketConsumer(uri, subscriberConfig, listener);
            consumer.connectBlocking(clientConfig.getConnectionTimeout(), TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            throw new MqttClientException("WebSocket connection could not be established!", e);
        }
    }

    @Override
    public void resubscribe() throws MqttClientException {
        final MessageListener listener = consumer.messageListener;
        close();
        subscribe(listener);
    }

    @Override
    public void unsubscribe() {
        clientConfig.getTokenApi().unsubscribe(tokenSupplier.getToken());
    }

    @Override
    public boolean isConnected() {
        return consumer != null && consumer.isOpen();
    }

    @Override
    public void close() {
        if (isConnected()) {
            consumer.close();
        }
        consumer = null;
    }

    private static final class WebSocketConsumer extends AbstractWebSocketClient {

        private final MessageListener messageListener;

        WebSocketConsumer(URI serverUri, SubscriberConfig subscriberConfig, MessageListener messageListener) {
            super(serverUri, subscriberConfig.getId(), subscriberConfig.getConnectionListener());
            this.messageListener = messageListener;
        }

        @Override
        public void onMessage(String message) {
            final WebSocketMessage webSocketMessage = WebSocketMessage.parse(message);

            final Optional<String> ackHeader = webSocketMessage.getAckHeader();
            final byte[] avroPayload = webSocketMessage.getAvroPayload();

            messageListener.onMessage(MqttMessageConverter.decode(avroPayload));

            ackHeader.ifPresent(this::send);
        }

    }

}
