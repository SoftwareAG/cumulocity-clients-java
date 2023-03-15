package com.cumulocity.generic.mqtt.client;

import com.cumulocity.generic.mqtt.client.model.GenericMqttMessage;

import java.net.URI;

public interface GenericMqttMessageListener {

    void onOpen(URI serverUri);

    void onMessage(GenericMqttMessage genericMqttMessage);

    void onError(Throwable t);

    void onClose(int code, String reason, boolean remote);

}
