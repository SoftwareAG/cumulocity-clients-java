package com.cumulocity.generic.mqtt.client.converter;

import com.cumulocity.generic.mqtt.client.model.GenericMqttMessage;
import org.apache.pulsar.client.api.Schema;

public class GenericMqttMessageConverter implements AvroMessageConverter<GenericMqttMessage> {
    private static final Schema<GenericMqttMessage> SCHEMA = AvroMessageConverter.getSchema(GenericMqttMessage.class);

    @Override
    public byte[] encode(GenericMqttMessage message) {
        return SCHEMA.encode(message);
    }

    @Override
    public GenericMqttMessage decode(byte[] bytes) {
        return SCHEMA.decode(bytes);
    }
}
