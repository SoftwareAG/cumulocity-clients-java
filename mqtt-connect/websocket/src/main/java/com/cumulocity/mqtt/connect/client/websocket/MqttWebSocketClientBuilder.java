package com.cumulocity.mqtt.connect.client.websocket;

import com.cumulocity.mqtt.connect.client.*;
import com.cumulocity.mqtt.connect.client.model.MqttMessage;
import com.cumulocity.sdk.client.messaging.notifications.TokenApi;

import java.net.MalformedURLException;

import static com.google.common.base.Preconditions.checkNotNull;

public class MqttWebSocketClientBuilder {

    private String url;
    private TokenApi tokenApi;

    private MqttWebSocketClientBuilder() {
    }

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
        this.url = url;
        return this;
    }

    /**
     * @param tokenApi see <code>TokenApi</code>
     * @return self
     */
    public MqttWebSocketClientBuilder tokenApi(final TokenApi tokenApi) {
        this.tokenApi = tokenApi;
        return this;
    }

    /**
     * Constructs a {@link MqttClient} instance and sets it to connect to the specified URI. The
     * client does not attempt to connect automatically. The connection will only be established once you
     * obtain the instance of {@link MqttPublisher} or {@link MqttSubscriber} and invoke
     * {@link MqttPublisher#publish(MqttMessage)} or {@link MqttSubscriber#subscribe(MqttMessageListener)}
     *
     * @return a {@link MqttClient} instance
     */
    public MqttClient build() {
        validateURL(url);
        checkNotNull(tokenApi, "Token API cannot be null");

        return new MqttClient() {
            @Override
            public MqttPublisher buildPublisher(final MqttConfig config) {
                return new MqttWebSocketPublisher(url, tokenApi, (MqttWebSocketConfig) config);
            }

            @Override
            public MqttSubscriber buildSubscriber(final MqttConfig config) {
                return new MqttWebSocketSubscriber(url, tokenApi, (MqttWebSocketConfig) config);
            }

            @Override
            public void close() {
                // do nothing
            }
        };
    }

    private void validateURL(String url) {
        checkNotNull(url, "Server URI has to be provided");

        if (!(url.startsWith("ws://") || url.startsWith("wss://"))) {
            throw new MqttClientException("Server URI should specify either 'ws://' or 'wss://' protocol", new MalformedURLException());
        }
    }

}
