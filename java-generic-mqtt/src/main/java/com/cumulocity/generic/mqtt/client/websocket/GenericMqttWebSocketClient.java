package com.cumulocity.generic.mqtt.client.websocket;

import com.cumulocity.generic.mqtt.client.*;
import com.cumulocity.sdk.client.messaging.notifications.TokenApi;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
class GenericMqttWebSocketClient implements GenericMqttClient {

    private final String url;
    private final TokenApi tokenApi;
    private final WebSocketConfig config = new WebSocketConfig();

    @Override
    public GenericMqttPublisherBuilder newPublisher() {
        return new GenericMqttPublisherBuilder() {
            @Override
            public GenericMqttPublisherBuilder topic(final String topic) {
                config.setTopic(topic);
                return this;
            }

            @Override
            public GenericMqttPublisherBuilder connectionTimeout(final long timeoutInMillis) {
                config.setConnectionTimeout(timeoutInMillis);
                return null;
            }

            @Override
            public GenericMqttPublisher create() {
                return new GenericMqttWebSocketPublisher(url, tokenApi, config);
            }
        };
    }

    @Override
    public GenericMqttSubscriberBuilder newSubscriber() {
        return new GenericMqttSubscriberBuilder() {
            @Override
            public GenericMqttSubscriberBuilder topic(final String topic) {
                config.setTopic(topic);
                return this;
            }

            @Override
            public GenericMqttSubscriberBuilder connectionTimeout(final long timeoutInMillis) {
                config.setConnectionTimeout(timeoutInMillis);
                return null;
            }

            @Override
            public GenericMqttSubscriber create() {
                return new GenericMqttWebSocketSubscriber(url, tokenApi, config);
            }
        };
    }

    @Override
    public void close() throws Exception {
    }
}
