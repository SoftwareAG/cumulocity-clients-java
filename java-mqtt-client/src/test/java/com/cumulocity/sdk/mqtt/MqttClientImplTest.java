package com.cumulocity.sdk.mqtt;

import com.cumulocity.sdk.mqtt.exception.MqttDeviceSDKException;
import com.cumulocity.sdk.mqtt.model.ConnectionDetails;
import com.cumulocity.sdk.mqtt.model.MqttMessageRequest;
import org.eclipse.paho.client.mqttv3.MqttAsyncClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.cumulocity.sdk.mqtt.model.QoS.AT_LEAST_ONCE;
import static com.cumulocity.sdk.mqtt.model.QoS.EXACTLY_ONCE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MqttClientImplTest {

    @Mock(lenient = true)
    private MqttOperationsProvider operationsProvider;

    @Mock
    private MqttAsyncClient mqttAsyncClient;

    @InjectMocks
    private MqttClientImpl pahoMqttClient;

    @BeforeEach
    public void setup() {
        final ConnectionDetails connectionDetails = ConnectionDetails.builder().host("test.c8y.io")
                                                                        .clientId("XNPP-EMEA-1234")
                                                                        .userName("tenant/user")
                                                                        .password("password")
                                                                        .cleanSession(true)
                                                                        .build();
        pahoMqttClient = new MqttClientImpl(connectionDetails);
        MockitoAnnotations.initMocks(this);

        when(operationsProvider.isConnectionEstablished()).thenReturn(true);
    }

    @Test
    public void cleanSessionIsTrueByDefault() {
        // When
        final ConnectionDetails connectionDetails = ConnectionDetails.builder().build();

        // Then
        assertThat(connectionDetails.isCleanSession()).isTrue();
    }

    @Test
    public void testPublishToTopic() throws Exception {
        // Given
        String topic = "s/us";
        String payload = "100, My MQTT device, c8y_MQTTDevice";
        MqttMessageRequest message = MqttMessageRequest.builder().topicName(topic)
                                        .qoS(EXACTLY_ONCE).messageContent(payload).build();

        // When
        pahoMqttClient.publish(message);

        // Then
        verify(operationsProvider, times(1)).publish(message);
    }

    @Test
    public void testPublishToWrongTopic() {
        // Given
        String topic = "s/usp";
        String payload = "100, My MQTT device, c8y_MQTTDevice";
        MqttMessageRequest message = MqttMessageRequest.builder().topicName(topic)
                .qoS(EXACTLY_ONCE).messageContent(payload).build();

        // When
        Throwable thrown = catchThrowable(() -> pahoMqttClient.publish(message));

        // Then
       assertThat(thrown).isInstanceOf(MqttDeviceSDKException.class);
    }

    @Test
    public void testSubscribeToTopic() throws Exception {
        // Given
        String topic = "s/ds";
        MqttMessageRequest message = MqttMessageRequest.builder().topicName(topic)
                .qoS(EXACTLY_ONCE).build();

        // When
        pahoMqttClient.subscribe( message,null);

        // Then
        verify(operationsProvider, times(1)).subscribe(message, null);
    }

    @Test
    public void testUnsubscribeFromTopic() throws Exception {
        // Given
        String topic = "s/ds";

        // When
        pahoMqttClient.unsubscribe(topic);

        // Then
        verify(operationsProvider, times(1)).unsubscribe(topic);
    }

    @Test
    public void testSubscribeToWrongTopic() {
        // Given
        String topic = "s/xyz";
        MqttMessageRequest message = MqttMessageRequest.builder().topicName(topic)
                .qoS(AT_LEAST_ONCE).build();

        // When
        Throwable thrown = catchThrowable(() -> pahoMqttClient.subscribe(message, null));

        // Then
        assertThat(thrown).isInstanceOf(MqttDeviceSDKException.class);
    }

    @Test
    public void testUnsubscribeFromWrongTopic() {
        // Given
        String topic = "s/xyz";

        // When
        Throwable thrown = catchThrowable(() -> pahoMqttClient.unsubscribe(topic));

        // Then
        assertThat(thrown).isInstanceOf(MqttDeviceSDKException.class);
    }
}
