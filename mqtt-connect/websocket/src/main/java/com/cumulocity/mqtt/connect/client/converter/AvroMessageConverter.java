package com.cumulocity.mqtt.connect.client.converter;

import org.apache.pulsar.client.api.Schema;
import org.apache.pulsar.client.api.SchemaSerializationException;

/**
 * Avro message converter to serialize message object to byte array and vice versa.
 */
public interface AvroMessageConverter<T> {

    /**
     * Encode an object representing the message content into a byte array.
     *
     * @param message the message object
     * @return a byte array with the serialized content
     * @throws SchemaSerializationException if the serialization fails
     */
    byte[] encode(T message);

    /**
     * Decode a byte array into an object of type <code>T</code>.
     *
     * @param bytes the byte array to decode
     * @return the deserialized object
     */
    T decode(byte[] bytes);

    /**
     * Create an Avro schema type using the default configuration for the class.
     *
     * @param <T>   The class the Schema is generated for.
     * @param clazz class used to generate the Avro schema.
     * @return a Schema instance
     */
    static <T> Schema<T> getSchema(Class<T> clazz) {
        return Schema.AVRO(clazz);
    }

}
