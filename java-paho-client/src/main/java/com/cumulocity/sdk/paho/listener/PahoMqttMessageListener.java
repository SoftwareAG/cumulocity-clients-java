package com.cumulocity.sdk.paho.listener;

import com.cumulocity.sdk.paho.model.MessageResponse;
import org.eclipse.paho.client.mqttv3.IMqttMessageListener;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public abstract class PahoMqttMessageListener implements IMqttMessageListener {

    private static final String MSG_DELIMITER = ",";

    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {
        final String mqttMessage = new String(message.getPayload());
        final String clientId = mqttMessage.split(MSG_DELIMITER)[1];
        final MessageResponse messageResponse = MessageResponse.builder().topicName(topic)
                                                    .clientId(clientId)
                                                        .qos(message.getQos())
                                                            .messageContent(mqttMessage)
                                                                .build();
        messageArrived(messageResponse);
    }

    public abstract void messageArrived(MessageResponse messageResponse);
}
