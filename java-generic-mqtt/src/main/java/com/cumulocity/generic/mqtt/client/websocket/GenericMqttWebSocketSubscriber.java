package com.cumulocity.generic.mqtt.client.websocket;

import com.cumulocity.generic.mqtt.client.GenericMqttConnectionConfig;
import com.cumulocity.generic.mqtt.client.GenericMqttMessageListener;
import com.cumulocity.generic.mqtt.client.GenericMqttSubscriber;
import com.cumulocity.generic.mqtt.client.websocket.exception.GenericMqttWebSocketClientException;
import com.cumulocity.rest.representation.reliable.notification.NotificationTokenRequestRepresentation;
import com.cumulocity.sdk.client.messaging.notifications.TokenApi;

import java.net.URI;
import java.util.concurrent.TimeUnit;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;

class GenericMqttWebSocketSubscriber implements GenericMqttSubscriber {

    private final static String WEBSOCKET_URL_PATTERN = "%s/notification2/consumer/?token=%s";
    private static final String TOKEN_TYPE = "mqtt";

    private final String webSocketBaseUrl;
    private final TokenApi tokenApi;
    private final GenericMqttConnectionConfig config;

    private GenericMqttWebSocketClient consumer;

    public GenericMqttWebSocketSubscriber(String webSocketBaseUrl, TokenApi tokenApi, GenericMqttConnectionConfig config) {
        this.webSocketBaseUrl = webSocketBaseUrl;
        this.tokenApi = tokenApi;
        this.config = config;
    }

    @Override
    public void subscribe(GenericMqttMessageListener messageListener) {

        if (consumer != null) {
            return;
        }

        final NotificationTokenRequestRepresentation tokenRequestRepresentation = getTokenRepresentation(config.getTopic());
        final String token = tokenApi
                .create(tokenRequestRepresentation)
                .getTokenString();

        if (token == null) {
            throw new GenericMqttWebSocketClientException(String.format("Token could not be created for topic %s", config.getTopic()));
        }

        try {
            final URI uri = new URI(String.format(WEBSOCKET_URL_PATTERN, webSocketBaseUrl, token));
            consumer = new GenericMqttWebSocketClient(uri, messageListener);
            consumer.connectBlocking(config.getConnectionTimeoutInMillis(), TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            throw new GenericMqttWebSocketClientException("WebSocket connection could not be established!", e);
        }
    }

    @Override
    public void close() throws Exception {
        if (consumer != null) {
            consumer.close();
        }
    }

    private NotificationTokenRequestRepresentation getTokenRepresentation(String topic) {
        final NotificationTokenRequestRepresentation representation = new NotificationTokenRequestRepresentation();
        representation.setSubscriber("javaGenericMqttWSClientSub" + randomAlphabetic(10));
        representation.setSubscription(topic);
        representation.setType(TOKEN_TYPE);
        representation.setSigned(true);
        representation.setExpiresInMinutes(1440);
        representation.setShared(false);
        representation.setNonPersistent(false);
        return representation;
    }
}
