package com.cumulocity.sdk.mqtt;

import com.cumulocity.sdk.mqtt.exception.MqttDeviceSDKException;
import com.cumulocity.sdk.mqtt.model.ConnectionDetails;
import com.cumulocity.sdk.mqtt.model.MqttMessageRequest;
import com.cumulocity.sdk.mqtt.operations.MqttOperationsProvider;
import org.eclipse.paho.client.mqttv3.MqttAsyncClient;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.*;

import static com.cumulocity.sdk.mqtt.model.QoS.AT_LEAST_ONCE;
import static com.cumulocity.sdk.mqtt.model.QoS.EXACTLY_ONCE;

@RunWith(MockitoJUnitRunner.class)
public class MqttClientImplTest {

    @Mock
    private MqttOperationsProvider operationsProvider;

    @Mock
    private MqttAsyncClient mqttAsyncClient;

    @InjectMocks
    private MqttClientImpl pahoMqttClient;

    @Before
    public void setup() throws Exception {

        final ConnectionDetails connectionDetails = ConnectionDetails.builder().host("test.c8y.io")
                                                                        .clientId("XNPP-EMEA-1234")
                                                                            .userName("tenant/user")
                                                                                .password("password")
                                                                                    .cleanSession(true)
                                                                                      .build();
        pahoMqttClient = new MqttClientImpl(connectionDetails);
        MockitoAnnotations.initMocks(this);

        when(operationsProvider.isConnectionEstablished()).thenReturn(true);
        when(mqttAsyncClient.isConnected()).thenReturn(true);
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

    @Test(expected = MqttDeviceSDKException.class)
    public void testPublishToWrongTopic() throws Exception {

        // Given
        String topic = "s/usp";
        String payload = "100, My MQTT device, c8y_MQTTDevice";
        MqttMessageRequest message = MqttMessageRequest.builder().topicName(topic)
                .qoS(EXACTLY_ONCE).messageContent(payload).build();

        // When
        pahoMqttClient.publish(message);

        // Then
        fail();
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

    @Test(expected = MqttDeviceSDKException.class)
    public void testSubscribeToWrongTopic() throws Exception {

        // Given
        String topic = "s/xyz";
        MqttMessageRequest message = MqttMessageRequest.builder().topicName(topic)
                .qoS(AT_LEAST_ONCE).build();

        // When
        pahoMqttClient.subscribe(message, null);

        // Then
        fail();
    }
}
