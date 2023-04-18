package com.cumulocity.mqtt.connect.client.converter;

import com.cumulocity.mqtt.connect.client.model.GenericMqttMessage;
import com.cumulocity.mqtt.connect.client.model.GenericMqttMetadata;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
public class GenericMqttMessageConverterTest {

    private final GenericMqttMessageConverter genericMqttMessageConverter = new GenericMqttMessageConverter();

    @Test
    void shouldEncodeAndDecodeCorrectly() {
        // Given
        final GenericMqttMetadata givenGenericMqttMetadata = new GenericMqttMetadata();
        givenGenericMqttMetadata.setClientId("test");
        givenGenericMqttMetadata.setMessageId(100);
        final GenericMqttMessage givenGenericMqttMessage = new GenericMqttMessage("message".getBytes(), givenGenericMqttMetadata);

        // When
        final byte[] encoded = genericMqttMessageConverter.encode(givenGenericMqttMessage);
        final GenericMqttMessage obtainedGenericMqttMessage = genericMqttMessageConverter.decode(encoded);

        // Then
        assertThat(new String(obtainedGenericMqttMessage.getPayload())).isEqualTo(new String(givenGenericMqttMessage.getPayload()));
        assertThat(obtainedGenericMqttMessage.getMetadata().getClientId()).isEqualTo(givenGenericMqttMessage.getMetadata().getClientId());
        assertThat(obtainedGenericMqttMessage.getMetadata().getMessageId()).isEqualTo(givenGenericMqttMessage.getMetadata().getMessageId());
    }
}