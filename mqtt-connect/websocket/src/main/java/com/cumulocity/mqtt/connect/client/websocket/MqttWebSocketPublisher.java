package com.cumulocity.mqtt.connect.client.websocket;

import com.cumulocity.mqtt.connect.client.MqttClientException;
import com.cumulocity.mqtt.connect.client.MqttPublisher;
import com.cumulocity.mqtt.connect.client.model.MqttMessage;
import com.cumulocity.sdk.client.messaging.notifications.TokenApi;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;

import java.net.URI;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
class MqttWebSocketPublisher implements MqttPublisher {

    private final static String SUBSCRIBER = "mqttConnectPublisher";
    private final static String WEBSOCKET_URL_PATTERN = "%s/notification2/producer/?token=%s";
    private final String webSocketBaseUrl;

    private final AtomicInteger sequence = new AtomicInteger();
    private final MqttWebSocketConfig config;
    private final TokenSupplier tokenSupplier;

    private WebSocketProducer producer;

    MqttWebSocketPublisher(String webSocketBaseUrl, TokenApi tokenApi, MqttWebSocketConfig config) {
        this.webSocketBaseUrl = webSocketBaseUrl;
        this.config = config;
        this.tokenSupplier = new TokenSupplier(tokenApi, config.getTopic(), SUBSCRIBER);
    }

    @Override
    public void publish(MqttMessage message) {
        final String token = tokenSupplier.getToken().getTokenString();

        if (token == null) {
            throw new MqttClientException(String.format("Token could not be created for topic %s", config.getTopic()));
        }
        if (producer == null) {
            try {
                final URI uri = new URI(String.format(WEBSOCKET_URL_PATTERN, webSocketBaseUrl, token));
                producer = new WebSocketProducer(uri);
                producer.connectBlocking(config.getConnectionTimeout(), TimeUnit.MILLISECONDS);
            } catch (Exception e) {
                throw new MqttClientException("Error publishing message!", e);
            }
        }
        final byte[] data = MqttMessageConverter.encode(message);
        final String publishMessage = sequence.incrementAndGet() + "\n" + Base64.encodeBase64String(data);

        producer.send(publishMessage);
    }

    @Override
    public void close() {
        if (producer != null) {
            producer.close();
        }
    }

    private static final class WebSocketProducer extends AbstractWebSocketClient {

        public WebSocketProducer(URI serverUri) {
            super(serverUri);
        }

        @Override
        public void onMessage(String message) {
            log.debug("Received ack for publish message {}", message);
        }

    }
}
