package com.cumulocity.mqtt.service.client.websocket;

import com.cumulocity.mqtt.service.client.MqttClientException;
import com.cumulocity.mqtt.service.client.MqttPublisher;
import com.cumulocity.mqtt.service.client.PublisherConfig;
import com.cumulocity.mqtt.service.client.model.MqttMessage;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;

import java.net.URI;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
class MqttWebSocketPublisher implements MqttPublisher {

    private final static String SUBSCRIBER = "mqttServicePublisher";
    private final static String WEBSOCKET_URL_PATTERN = "%s/notification2/producer/?token=%s";

    private final WebSocketClientConfig clientConfig;
    private final PublisherConfig publisherConfig;

    private final AtomicInteger sequence = new AtomicInteger();
    private final TokenSupplier tokenSupplier;

    private WebSocketProducer producer;

    MqttWebSocketPublisher(WebSocketClientConfig clientConfig, PublisherConfig publisherConfig) {
        this.clientConfig = clientConfig;
        this.publisherConfig = publisherConfig;
        this.tokenSupplier = new TokenSupplier(clientConfig.getTokenApi(), publisherConfig.getTopic(), SUBSCRIBER);
    }

    void connect() throws MqttClientException {
        final String token = tokenSupplier.getToken().getTokenString();
        if (token == null) {
            throw new MqttClientException(String.format("Token could not be created for topic %s", publisherConfig.getTopic()));
        }
        try {
            final URI uri = new URI(String.format(WEBSOCKET_URL_PATTERN, clientConfig.getBaseUrl(), token));
            producer = new WebSocketProducer(uri, publisherConfig);
            producer.connectBlocking(clientConfig.getConnectionTimeout(), TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            throw new MqttClientException("Error connecting to " + clientConfig.getBaseUrl(), e);
        }
    }

    @Override
    public void reconnect() throws MqttClientException {
        if (isConnected()) {
            close();
        }
        connect();
    }

    @Override
    public void publish(MqttMessage message) {
        final byte[] data = MqttMessageConverter.encode(message);
        final String publishMessage = sequence.incrementAndGet() + "\n" + Base64.encodeBase64String(data);

        producer.send(publishMessage);
    }

    @Override
    public boolean isConnected() {
        return producer != null && producer.isOpen();
    }

    @Override
    public void close() {
        if (isConnected()) {
            producer.close();
        }
        producer = null;
    }

    private static final class WebSocketProducer extends AbstractWebSocketClient {

        public WebSocketProducer(URI serverUri, PublisherConfig publisherConfig) {
            super(serverUri, publisherConfig.getId(), publisherConfig.getConnectionListener());
        }

        @Override
        public void onMessage(String message) {
            log.debug("Received ack for publish message {}", message);
        }

    }
}
