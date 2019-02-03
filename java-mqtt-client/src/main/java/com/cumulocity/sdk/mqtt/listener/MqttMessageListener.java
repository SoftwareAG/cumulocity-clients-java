package com.cumulocity.sdk.mqtt.listener;

import com.cumulocity.sdk.mqtt.model.MqttMessageResponse;

public interface MqttMessageListener {

    void messageArrived(final MqttMessageResponse messageResponse);
}
