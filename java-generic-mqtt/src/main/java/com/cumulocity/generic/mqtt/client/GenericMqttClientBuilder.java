package com.cumulocity.generic.mqtt.client;

import com.cumulocity.generic.mqtt.client.model.GenericMqttMessage;
import com.cumulocity.sdk.client.messaging.notifications.TokenApi;

public interface GenericMqttClientBuilder {

    /**
     * @param url the server URI to connect to
     * @return self
     */
    GenericMqttClientBuilder url(final String url);

    /**
     * @param tokenApi see {@link TokenApi}
     * @return self
     */
    GenericMqttClientBuilder tokenApi(final TokenApi tokenApi);

    /**
     * Constructs a {@link GenericMqttClient} instance and sets it to connect to the specified URI. The
     * client does not attempt to connect automatically. The connection will only be established once you
     * obtain the instance of {@link GenericMqttPublisher} or {@link GenericMqttSubscriber} and invoke
     * {@link GenericMqttPublisher#publish(GenericMqttMessage)} or {@link GenericMqttSubscriber#subscribe(GenericMqttMessageListener)}
     * <p>
     * NOTE that before calling build method {@link #url(String)} and {@link #tokenApi(TokenApi)} has to be called and set properly,
     * otherwise build method will throw exception.
     */
    GenericMqttClient build();

}
