package com.cumulocity.mqtt.connect.client.websocket;

import com.cumulocity.mqtt.connect.client.model.MqttMessage;
import lombok.experimental.UtilityClass;
import org.apache.pulsar.client.api.Schema;

@UtilityClass
class MqttMessageConverter {

    private static final Schema<MqttMessage> SCHEMA = Schema.AVRO(MqttMessage.class);

    public static byte[] encode(MqttMessage message) {
        return SCHEMA.encode(message);
    }

    public static MqttMessage decode(byte[] bytes) {
        return SCHEMA.decode(bytes);
    }

}
