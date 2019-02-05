package com.cumulocity.sdk.mqtt;

import com.cumulocity.sdk.mqtt.model.ConnectionDetails;

public class MqttClientBuilder {

    public static MqttClient generateClient(final ConnectionDetails connectionDetails) {
        return new MqttClientImpl(connectionDetails);
    }
}
