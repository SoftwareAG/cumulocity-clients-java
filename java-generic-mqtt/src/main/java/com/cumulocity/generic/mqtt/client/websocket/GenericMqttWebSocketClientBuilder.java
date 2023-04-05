package com.cumulocity.generic.mqtt.client.websocket;

import com.cumulocity.generic.mqtt.client.GenericMqttClient;
import com.cumulocity.generic.mqtt.client.GenericMqttClientBuilder;
import com.cumulocity.generic.mqtt.client.GenericMqttPublisher;
import com.cumulocity.generic.mqtt.client.GenericMqttSubscriber;
import com.cumulocity.sdk.client.messaging.notifications.TokenApi;

import java.util.Properties;

import static com.google.common.base.Preconditions.checkNotNull;

public class GenericMqttWebSocketClientBuilder implements GenericMqttClientBuilder {

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

    @Override
    public GenericMqttClient build() {
        checkNotNull(url, "Web socket url has to be provided");
        checkNotNull(tokenApi, "Token API cannot be null");
        if (!url.startsWith("ws://")) {
            url = "ws://" + url;
        }

        return new GenericMqttClient() {

            @Override
            public GenericMqttPublisher buildPublisher(String topic, Properties properties) {
                validateProperties(properties);

                final GenericMqttWebSocketConfig config = new GenericMqttWebSocketConfig();
                config.setTopic(topic);

                return new GenericMqttWebSocketPublisher(url, tokenApi, config);
            }

            @Override
            public GenericMqttSubscriber buildSubscriber(String topic, Properties properties) {
                validateProperties(properties);

                final GenericMqttWebSocketConfig config = new GenericMqttWebSocketConfig();
                config.setTopic(topic);

                return new GenericMqttWebSocketSubscriber(url, tokenApi, config);
            }

            @Override
            public void close() throws Exception {
                // do nothing
            }

            private void validateProperties(Properties properties) {

            }
        };
    }

}
