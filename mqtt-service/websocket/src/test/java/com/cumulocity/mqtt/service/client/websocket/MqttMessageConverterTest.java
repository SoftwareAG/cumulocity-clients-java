package com.cumulocity.mqtt.service.client.websocket;

import com.cumulocity.mqtt.service.client.model.MqttMessage;
import com.cumulocity.mqtt.service.client.model.MqttMetadata;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class MqttMessageConverterTest {

    @Test
    void shouldEncodeAndDecodeCorrectly() {
        // Given
        final MqttMetadata metadata = MqttMetadata.builder().
                clientId("test").
                messageId(100).
                build();
        final MqttMessage message = new MqttMessage("message".getBytes(), metadata);

        // When
        final byte[] encoded = MqttMessageConverter.encode(message);
        final MqttMessage decodedMessage = MqttMessageConverter.decode(encoded);

        // Then
        assertThat(new String(decodedMessage.getPayload())).isEqualTo(new String(message.getPayload()));
        assertThat(decodedMessage.getMetadata().getClientId()).isEqualTo(message.getMetadata().getClientId());
        assertThat(decodedMessage.getMetadata().getMessageId()).isEqualTo(message.getMetadata().getMessageId());
    }
}
