package com.cumulocity.sms.client;

import com.cumulocity.sms.client.messaging.MessagingClient;
import org.junit.Test;

import static org.fest.assertions.Assertions.assertThat;

public class SmsMessagingApiTest {

    @Test
    public void shouldCreateClient() {
        final SmsMessagingApiImpl messaging = new SmsMessagingApiImpl("http://host", "root", null);

        final MessagingClient messagingClient = messaging.getMessagingClient();

        assertThat(messagingClient.getRootEndpoint()).isEqualTo("http://host/root");
    }
}
