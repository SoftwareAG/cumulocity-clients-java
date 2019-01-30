package com.cumulocity.sdk.mqtt;

import com.cumulocity.sdk.mqtt.exception.MqttDeviceSDKException;
import com.cumulocity.sdk.mqtt.operations.MqttOperationsProvider;
import org.eclipse.paho.client.mqttv3.MqttAsyncClient;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.fail;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class MqttClientImplTest {

    @Mock
    private MqttOperationsProvider clientOperations;

    @Mock
    private MqttAsyncClient mqttAsyncClient;

    @InjectMocks
    private MqttClientImpl pahoMqttClient;

    @Before
    public void setup() throws Exception {

        when(clientOperations.isConnectionEstablished()).thenReturn(true);
        when(mqttAsyncClient.isConnected()).thenReturn(true);
    }

    @Test
    public void testPublishToTopic() throws Exception {

        // Given
        String topic = "s/us";
        String payload = "100, My MQTT device, c8y_MQTTDevice";

        // When
        pahoMqttClient.publishToTopic(topic, 2, payload);

        // Then
        verify(clientOperations, times(1)).publish(topic, 2, payload);
    }

    @Test(expected = MqttDeviceSDKException.class)
    public void testPublishToWrongTopic() throws Exception {

        // Given
        String topic = "s/usp";
        String payload = "100, My MQTT device, c8y_MQTTDevice";

        // When
        pahoMqttClient.publishToTopic(topic, 2, payload);

        // Then
        fail();
    }

    @Test
    public void testSubscribeToTopic() throws Exception {

        // Given
        String topic = "s/ds";

        // When
        pahoMqttClient.subscribeToTopic(topic, 2, null);

        // Then
        verify(clientOperations, times(1)).subscribe(topic, 2, null);
    }

    @Test(expected = MqttDeviceSDKException.class)
    public void testSubscribeToWrongTopic() throws Exception {

        // Given
        String topic = "s/xyz";

        // When
        pahoMqttClient.subscribeToTopic(topic, 2, null);

        // Then
        fail();
    }
}
