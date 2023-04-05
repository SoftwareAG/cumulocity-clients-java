package com.cumulocity.generic.mqtt.client.websocket;

import com.cumulocity.generic.mqtt.client.*;
import com.cumulocity.generic.mqtt.client.model.GenericMqttMessage;
import com.cumulocity.sdk.client.messaging.notifications.TokenApi;

import static com.google.common.base.Preconditions.checkNotNull;

public class GenericMqttWebSocketClientBuilder {

    private String url;
    private TokenApi tokenApi;

    /**
     * Creates a builder for {@link GenericMqttClient}.
     *
     * @return the created builder
     */
    public static GenericMqttWebSocketClientBuilder builder() {
        return new GenericMqttWebSocketClientBuilder();
    }

    /**
     * @param url the server URI to connect to
     * @return self
     */
    public GenericMqttWebSocketClientBuilder url(final String url) {
        this.url = url;
        return this;
    }

    /**
     * @param tokenApi see {@link TokenApi}
     * @return self
     */
    public GenericMqttWebSocketClientBuilder tokenApi(final TokenApi tokenApi) {
        this.tokenApi = tokenApi;
        return this;
    }

    /**
     * Constructs a {@link GenericMqttClient} instance and sets it to connect to the specified URI. The
     * client does not attempt to connect automatically. The connection will only be established once you
     * obtain the instance of {@link GenericMqttPublisher} or {@link GenericMqttSubscriber} and invoke
     * {@link GenericMqttPublisher#publish(GenericMqttMessage)} or {@link GenericMqttSubscriber#subscribe(GenericMqttMessageListener)}
     */
    public GenericMqttClient build() {
        checkNotNull(url, "Web socket url has to be provided");
        checkNotNull(tokenApi, "Token API cannot be null");
        if (!url.startsWith("ws://")) {
            url = "ws://" + url;
        }
        return new GenericMqttClient() {
            @Override
            public GenericMqttPublisher buildPublisher(final GenericMqttConfig config) {
                return new GenericMqttWebSocketPublisher(url, tokenApi, (GenericMqttWebSocketConfig) config);
            }

            @Override
            public GenericMqttSubscriber buildSubscriber(final GenericMqttConfig config) {
                return new GenericMqttWebSocketSubscriber(url, tokenApi, (GenericMqttWebSocketConfig) config);
            }

            @Override
            public void close() throws Exception {
                // do nothing
            }
        };
    }

}
