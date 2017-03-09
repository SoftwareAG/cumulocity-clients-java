package com.cumulocity.sms.client.request;

import com.cumulocity.model.sms.Address;
import com.cumulocity.model.sms.OutgoingMessageRequest;
import com.cumulocity.model.sms.SendMessageRequest;
import com.cumulocity.sms.client.messaging.MessagingClient;
import com.cumulocity.sms.client.messaging.SmsClientException;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.client.fluent.Executor;
import org.apache.http.client.fluent.Request;
import org.apache.http.util.EntityUtils;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.io.IOException;
import java.lang.reflect.Field;

import static com.cumulocity.model.sms.Protocol.ICCID;
import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

public class MicroserviceRequestTest extends MessagingClient {

    public MicroserviceRequestTest() {
        super("testBaseUrl/service/sms/smsmessaging/", mock(Executor.class));
    }

    @Test(expected = SmsClientException.class)
    public void shouldThrowExceptionWhenSendRequestFails() throws IOException {
        when(authorizedTemplate.execute(any(Request.class))).thenThrow(IOException.class);

        sendMessage(new Address(), new OutgoingMessageRequest());
    }

    @Test(expected = SmsClientException.class)
    public void shouldThrowExceptionWhenGetMessagesRequestFails() throws IOException {
        when(authorizedTemplate.execute(any(Request.class))).thenThrow(IOException.class);

        getAllMessages(new Address());
    }

    @Test(expected = SmsClientException.class)
    public void shouldThrowExceptionWhenGetMessageRequestFails() throws IOException {
        when(authorizedTemplate.execute(any(Request.class))).thenThrow(IOException.class);

        getMessage(new Address(), "1");
    }

    @Test
    public void shouldSendCorrectContentTypeInHeader() throws IOException {
        sendMessage(new Address(ICCID, "123"), new OutgoingMessageRequest());

        final ArgumentCaptor<Request> captor = ArgumentCaptor.forClass(Request.class);
        verify(authorizedTemplate).execute(captor.capture());

        assertThat(captor.getValue().toString()).isEqualTo("POST testBaseUrl/service/sms/smsmessaging/outbound/iccid:123/requests HTTP/1.1");
    }

    @Test
    @Ignore
    public void shouldUseSecureEndpoint() throws IOException {
        sendMessage(new Address(), new OutgoingMessageRequest());
        getMessage(new Address(), "");
        getAllMessages(new Address());

        ArgumentCaptor<Request> captor = ArgumentCaptor.forClass(Request.class);
        verify(authorizedTemplate).execute(captor.capture());
        assertThat(captor.getValue().toString()).isEqualTo("testBaseUrl/service/sms/smsmessaging/outbound/{senderAddress}/requests");

        verify(authorizedTemplate).execute(captor.capture());
        assertThat(captor.getValue().toString()).isEqualTo("testBaseUrl/service/sms/smsmessaging/inbound/registrations/{receiveAddress}/messages");

        verify(authorizedTemplate).execute(captor.capture());
        assertThat(captor.getValue().toString()).isEqualTo("testBaseUrl/service/sms/smsmessaging/inbound/registrations/{receiveAddress}/messages/{messageId}");
    }

    @Test
    public void shouldSerializeBodyCorrectly() throws IOException, NoSuchFieldException, IllegalAccessException {
        SendMessageRequest message = SendMessageRequest.builder()
                .withSender(Address.phoneNumber("123"))
                .withReceiver(Address.phoneNumber("245"))
                .withMessage("test text").build();
        sendMessage(Address.phoneNumber("123"), new OutgoingMessageRequest(message));

        final ArgumentCaptor<Request> captor = ArgumentCaptor.forClass(Request.class);
        verify(authorizedTemplate).execute(captor.capture());

        final HttpEntityEnclosingRequest actualRequest = getFieldValue(captor.getValue());
        assertThat(EntityUtils.toString(actualRequest.getEntity())).isEqualTo("{\"outboundSMSMessageRequest\":{\"address\":[{\"number\":\"245\"," +
                "\"protocol\":\"MSISDN\"}],\"clientCorrelator\":null,\"deviceId\":null,\"outboundSMSTextMessage\":{\"message\":\"test text\"}," +
                "\"receiptRequest\":{\"callbackData\":null,\"notifyURL\":null},\"senderAddress\":{" +
                "\"number\":\"123\",\"protocol\":\"MSISDN\"},\"senderName\":null}}");
    }

    private HttpEntityEnclosingRequest getFieldValue(Request value) throws NoSuchFieldException, IllegalAccessException {
        final Field field = Request.class.getDeclaredField("request");
        field.setAccessible(true);
        return (HttpEntityEnclosingRequest) field.get(value);
    }

}
