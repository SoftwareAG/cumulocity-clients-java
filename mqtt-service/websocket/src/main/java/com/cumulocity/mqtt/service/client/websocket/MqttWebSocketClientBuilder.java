package com.cumulocity.mqtt.service.client.websocket;

import com.cumulocity.mqtt.service.client.*;
import com.cumulocity.mqtt.service.client.model.MqttMessage;
import com.cumulocity.sdk.client.messaging.notifications.TokenApi;
import lombok.NoArgsConstructor;

import java.net.MalformedURLException;

import static com.google.common.base.Preconditions.checkNotNull;
import static lombok.AccessLevel.PRIVATE;
import static org.apache.commons.lang3.StringUtils.isBlank;

@NoArgsConstructor(access = PRIVATE)
public class MqttWebSocketClientBuilder {

    private final WebSocketClientConfig clientConfig = new WebSocketClientConfig();

    /**
     * Creates a builder for {@link MqttClient}.
     *
     * @return the created builder
     */
    public static MqttWebSocketClientBuilder builder() {
        return new MqttWebSocketClientBuilder();
    }

    /**
     * @param url the server URI to connect to
     * @return self
     */
    public MqttWebSocketClientBuilder url(final String url) {
        clientConfig.setBaseUrl(url);
        return this;
    }

    /**
     * @param tokenApi see <code>TokenApi</code>
     * @return self
     */
    public MqttWebSocketClientBuilder tokenApi(final TokenApi tokenApi) {
        clientConfig.setTokenApi(tokenApi);
        return this;
    }

    /**
     * Connection timeout in millis second until the websocket connected or failed to do so.
     *
     * @param connectionTimeout the connection timeout in millis
     * @return self
     */
    public MqttWebSocketClientBuilder connectionTimeout(final long connectionTimeout) {
        clientConfig.setConnectionTimeout(connectionTimeout);
        return this;
    }

    /**
     * Constructs a {@link MqttClient} instance and sets it to connect to the specified URI. The
     * client does not attempt to connect automatically. The connection will only be established once you
     * obtain the instance of {@link MqttPublisher} or {@link MqttSubscriber} and invoke
     * {@link MqttPublisher#publish(MqttMessage)} or {@link MqttSubscriber#subscribe(MessageListener)}
     *
     * @return a {@link MqttClient} instance
     */
    public MqttClient build() {
        validateURL(clientConfig.getBaseUrl());
        checkNotNull(clientConfig.getTokenApi(), "Token API cannot be null");

        return new AbstractMqttClient() {
            @Override
            public MqttPublisher createPublisherInstance(final PublisherConfig config) throws MqttClientException {
                validateTopic(config.getTopic());
                final MqttWebSocketPublisher publisher = new MqttWebSocketPublisher(clientConfig, config);
                publisher.connect();
                return publisher;
            }

            @Override
            public MqttSubscriber createSubscriberInstance(final SubscriberConfig config) {
                validateTopic(config.getTopic());
                validateSubscriber(config.getSubscriber());
                return new MqttWebSocketSubscriber(clientConfig, config);
            }
        };
    }

    private void validateURL(String url) {
        checkNotNull(url, "Server URI has to be provided");

        if (!(url.startsWith("ws://") || url.startsWith("wss://"))) {
            throw new MqttClientException("Server URI should specify either 'ws://' or 'wss://' protocol", new MalformedURLException());
        }
    }

    private void validateTopic(final String topic) {
        if (isBlank(topic)) {
            throw new MqttClientException("Topic has to be provided");
        }
    }

    private void validateSubscriber(String subscriber) {
        if (isBlank(subscriber)) {
            throw new MqttClientException("Subscriber has to be provided");
        }
    }

}
