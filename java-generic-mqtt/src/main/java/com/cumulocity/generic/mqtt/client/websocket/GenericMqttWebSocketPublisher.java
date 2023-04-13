package com.cumulocity.generic.mqtt.client.websocket;

import com.cumulocity.generic.mqtt.client.GenericMqttPublisher;
import com.cumulocity.generic.mqtt.client.converter.GenericMqttMessageConverter;
import com.cumulocity.generic.mqtt.client.exception.GenericMqttClientException;
import com.cumulocity.generic.mqtt.client.model.GenericMqttMessage;
import com.cumulocity.sdk.client.messaging.notifications.TokenApi;
import org.apache.commons.codec.binary.Base64;

import java.net.URI;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

class GenericMqttWebSocketPublisher implements GenericMqttPublisher {
    private final static String SUBSCRIBER = "javaGenericMqttWSClientPub";
    private final static String WEBSOCKET_URL_PATTERN = "%s/notification2/producer/?token=%s";
    private final String webSocketBaseUrl;

    private final GenericMqttMessageConverter genericMqttMessageConverter = new GenericMqttMessageConverter();

    private final AtomicInteger sequence = new AtomicInteger();
    private final GenericMqttWebSocketConfig config;
    private final TokenSupplier tokenSupplier;

    private GenericMqttWebSocketClient producer;


    GenericMqttWebSocketPublisher(String webSocketBaseUrl, TokenApi tokenApi, GenericMqttWebSocketConfig config) {
        this.webSocketBaseUrl = webSocketBaseUrl;
        this.config = config;
        this.tokenSupplier = new TokenSupplier(tokenApi, config.getTopic(), SUBSCRIBER);
    }

    @Override
    public void publish(GenericMqttMessage message) {
        final String token = tokenSupplier.get();

        if (token == null) {
            throw new GenericMqttClientException(String.format("Token could not be created for topic %s", config.getTopic()));
        }

        if (producer == null) {
            try {
                final URI uri = new URI(String.format(WEBSOCKET_URL_PATTERN, webSocketBaseUrl, token));
                producer = new GenericMqttWebSocketClient(uri);
                producer.connectBlocking(config.getConnectionTimeout(), TimeUnit.MILLISECONDS);
            } catch (Exception e) {
                throw new GenericMqttClientException("Error publishing message!", e);
            }
        }

        final byte[] data = genericMqttMessageConverter.encode(message);
        final String publishMessage = sequence.incrementAndGet() + "\n" + Base64.encodeBase64String(data);

        producer.send(publishMessage);
    }

    @Override
    public void close() throws Exception {
        if (producer != null) {
            producer.close();
        }
    }
}
