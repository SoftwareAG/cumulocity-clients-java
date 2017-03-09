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
import static com.googlecode.catchexception.CatchException.catchException;
import static com.googlecode.catchexception.CatchException.caughtException;
import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

public class MicroserviceRequestTest {

    private MessagingClient client = new MessagingClient("testBaseUrl/service/sms/smsmessaging/", mock(Executor.class));

    @Test
    public void shouldThrowExceptionWhenSendRequestFails() throws IOException {
        when(client.getAuthorizedTemplate().execute(any(Request.class))).thenThrow(new IOException("qwe"));

        catchException(client).sendMessage(new Address(), new OutgoingMessageRequest());

        assertThat(caughtException()).isInstanceOf(SmsClientException.class);
        assertThat(caughtException()).hasMessage("Send sms request failure");
    }

    @Test
    public void shouldThrowExceptionWhenGetMessagesRequestFails() throws IOException {
        when(client.getAuthorizedTemplate().execute(any(Request.class))).thenThrow(IOException.class);

        catchException(client).getAllMessages(new Address());

        assertThat(caughtException()).isInstanceOf(SmsClientException.class);
        assertThat(caughtException()).hasMessage("Get sms messages failure");
    }

    @Test
    public void shouldThrowExceptionWhenGetMessageRequestFails() throws IOException {
        when(client.getAuthorizedTemplate().execute(any(Request.class))).thenThrow(IOException.class);

        catchException(client).getMessage(new Address(), "1");

        assertThat(caughtException()).isInstanceOf(SmsClientException.class);
        assertThat(caughtException()).hasMessage("Get sms message failure");
    }

    @Test
    public void shouldSendCorrectContentTypeInHeader() throws IOException {
        client.sendMessage(new Address(ICCID, "123"), new OutgoingMessageRequest());

        final ArgumentCaptor<Request> captor = ArgumentCaptor.forClass(Request.class);
        verify(client.getAuthorizedTemplate()).execute(captor.capture());

        assertThat(captor.getValue().toString()).isEqualTo("POST testBaseUrl/service/sms/smsmessaging/outbound/iccid:123/requests HTTP/1.1");
    }

    @Test
    @Ignore
    public void shouldUseSecureEndpoint() throws IOException {
        client.sendMessage(new Address(), new OutgoingMessageRequest());
        client.getMessage(new Address(), "");
        client.getAllMessages(new Address());

        ArgumentCaptor<Request> captor = ArgumentCaptor.forClass(Request.class);
        verify(client.getAuthorizedTemplate()).execute(captor.capture());
        assertThat(captor.getValue().toString()).isEqualTo("testBaseUrl/service/sms/smsmessaging/outbound/{senderAddress}/requests");

        verify(client.getAuthorizedTemplate()).execute(captor.capture());
        assertThat(captor.getValue().toString()).isEqualTo("testBaseUrl/service/sms/smsmessaging/inbound/registrations/{receiveAddress}/messages");

        verify(client.getAuthorizedTemplate()).execute(captor.capture());
        assertThat(captor.getValue().toString()).isEqualTo("testBaseUrl/service/sms/smsmessaging/inbound/registrations/{receiveAddress}/messages/{messageId}");
    }

    @Test
    public void shouldSerializeBodyCorrectly() throws IOException, NoSuchFieldException, IllegalAccessException {
        SendMessageRequest message = SendMessageRequest.builder()
                .withSender(Address.phoneNumber("123"))
                .withReceiver(Address.phoneNumber("245"))
                .withMessage("test text").build();
        client.sendMessage(Address.phoneNumber("123"), new OutgoingMessageRequest(message));

        final ArgumentCaptor<Request> captor = ArgumentCaptor.forClass(Request.class);
        verify(client.getAuthorizedTemplate()).execute(captor.capture());

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
