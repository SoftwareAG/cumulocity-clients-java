package com.cumulocity.mqtt.service.sdk.websocket;

import com.cumulocity.mqtt.service.sdk.model.MqttServiceMessage;
import lombok.experimental.UtilityClass;
import org.apache.pulsar.client.api.Schema;

@UtilityClass
class MessageConverter {

    private static final Schema<MqttServiceMessage> SCHEMA = Schema.AVRO(MqttServiceMessage.class);

    public static byte[] encode(MqttServiceMessage message) {
        return SCHEMA.encode(message);
    }

    public static MqttServiceMessage decode(byte[] bytes) {
        return SCHEMA.decode(bytes);
    }

}
