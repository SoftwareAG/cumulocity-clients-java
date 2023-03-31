package com.cumulocity.generic.mqtt.client;

import com.cumulocity.generic.mqtt.client.model.GenericMqttMessage;
import com.cumulocity.generic.mqtt.client.websocket.GenericMqttWebSocketClient;
import com.cumulocity.sdk.client.messaging.notifications.TokenApi;

import static com.google.common.base.Preconditions.checkNotNull;

public class GenericMqttClientBuilder {

    private String url;
    private TokenApi tokenApi;

    /**
     * @param url the server URI to connect to
     * @return self
     */
    public GenericMqttClientBuilder url(final String url) {
        this.url = url;
        return this;
    }

    /**
     * @param tokenApi see {@link TokenApi}
     * @return self
     */
    public GenericMqttClientBuilder tokenApi(final TokenApi tokenApi) {
        this.tokenApi = tokenApi;
        return this;
    }

    /**
     * Constructs a {@link GenericMqttClient} instance and sets it to connect to the specified URI. The
     * channel does not attempt to connect automatically. The connection will only be established once you
     * obtain the instance of {@link GenericMqttPublisher} or {@link GenericMqttSubscriber} and invoke
     * {@link GenericMqttPublisher#publish(GenericMqttMessage)} or {@link GenericMqttSubscriber#subscribe(GenericMqttMessageListener)}
     * <p>
     * Note that before calling build method {@link #url(String)} and {@link #tokenApi(TokenApi)} has to be called and set properly,
     * otherwise build method will throw exception.
     */
    public GenericMqttClient build() {
        checkNotNull(url, "Web socket url has to be provided");
        checkNotNull(tokenApi, "Token API cannot be null");
        if (!url.startsWith("ws://")) {
            url = "ws://" + url;
        }
        return new GenericMqttWebSocketClient(url, tokenApi);
    }

}
