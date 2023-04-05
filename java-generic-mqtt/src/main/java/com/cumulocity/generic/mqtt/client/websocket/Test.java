package com.cumulocity.generic.mqtt.client.websocket;

import com.cumulocity.generic.mqtt.client.GenericMqttClient;
import com.cumulocity.generic.mqtt.client.GenericMqttMessageListener;
import com.cumulocity.generic.mqtt.client.GenericMqttPublisher;
import com.cumulocity.generic.mqtt.client.GenericMqttSubscriber;
import com.cumulocity.generic.mqtt.client.model.GenericMqttMessage;

import java.util.Properties;

@Deprecated
public class Test {

    public static void main(String[] args) {

        final String payload = "Hello World";

        final GenericMqttMessage genericMqttMessage = new GenericMqttMessage();
        genericMqttMessage.setPayload(payload.getBytes());

        GenericMqttClient genericMqttClient = GenericMqttWebSocketClientBuilder.builder()
                .url("")
                .tokenApi(null)
                .build();

        String topic = "abc";

        // Publish

        GenericMqttPublisher genericMqttPublisher = genericMqttClient.buildPublisher(topic, new Properties());
        genericMqttPublisher.publish(genericMqttMessage);

        // Subscribe

        GenericMqttSubscriber genericMqttSubscriber = genericMqttClient.buildSubscriber(topic, new Properties());
        genericMqttSubscriber.subscribe(new GenericMqttMessageListener() {
            @Override
            public void onMessage(GenericMqttMessage message) {

            }

            @Override
            public void onError(Throwable t) {

            }
        });

    }

}
