package com.cumulocity.sms.client.request;

import com.cumulocity.model.sms.Address;
import com.cumulocity.model.sms.OutgoingMessageRequest;
import com.cumulocity.model.sms.SendMessageRequest;
import com.cumulocity.sms.client.messaging.MessagingClient;
import com.cumulocity.sms.client.messaging.SmsClientException;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.fluent.Executor;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.fluent.Response;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.skyscreamer.jsonassert.JSONCompareMode;

import java.io.IOException;
import java.lang.reflect.Field;

import static com.cumulocity.model.sms.Protocol.ICCID;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;
import static org.skyscreamer.jsonassert.JSONAssert.assertEquals;

public class MicroserviceRequestTest {

    private MessagingClient client = new MessagingClient("testBaseUrl/service/sms/smsmessaging/", mock(Executor.class));

    @Test(expected = SmsClientException.class)
    public void shouldThrowExceptionWhenSendRequestFails() throws IOException {
        when(client.getAuthorizedTemplate().execute(any(Request.class))).thenThrow(new IOException("qwe"));

        try {
            client.sendMessage(new Address(), new OutgoingMessageRequest());
        } catch (SmsClientException ex) {
            assert (ex.getMessage().equals("Send sms request failure"));
            throw  new SmsClientException(ex.getMessage());
        }
    }

    @Test(expected = SmsClientException.class)
    public void shouldThrowExceptionWhenGetMessagesRequestFails() throws IOException {
        when(client.getAuthorizedTemplate().execute(any(Request.class))).thenThrow(IOException.class);

        try {
            client.getAllMessages(new Address());
        } catch (SmsClientException ex) {
            assert (ex.getMessage().equals("Get sms messages failure"));
            throw  new SmsClientException(ex.getMessage());
        }
    }

    @Test(expected = SmsClientException.class)
    public void shouldThrowExceptionWhenGetMessageRequestFails() throws IOException {
        when(client.getAuthorizedTemplate().execute(any(Request.class))).thenThrow(IOException.class);

        try {
            client.getMessage(new Address(), "1");
        } catch (SmsClientException ex) {
            assert (ex.getMessage().equals("Get sms message failure"));
            throw  new SmsClientException(ex.getMessage());
        }
    }

    @Test
    public void shouldSendCorrectContentTypeInHeader() throws IOException {
        client.sendMessage(new Address(ICCID, "123"), new OutgoingMessageRequest());

        final ArgumentCaptor<Request> captor = ArgumentCaptor.forClass(Request.class);
        verify(client.getAuthorizedTemplate()).execute(captor.capture());

        assert(captor.getValue().toString().equals("POST testBaseUrl/service/sms/smsmessaging/outbound/iccid:123/requests HTTP/1.1"));
    }

    @Test(expected = SmsClientException.class)
    public void shouldThrowExceptionWhenResponseStatusIsIncorrect() throws IOException {
        final Response response = mock(Response.class);
        final HttpResponse httpResponse = mock(HttpResponse.class);
        final StatusLine statusLine = mock(StatusLine.class);
        when(statusLine.getStatusCode()).thenReturn(404);
        when(httpResponse.getStatusLine()).thenReturn(statusLine);
        when(response.returnResponse()).thenReturn(httpResponse);
        when(client.getAuthorizedTemplate().execute(any(Request.class))).thenReturn(response);

        try {
            client.sendMessage(new Address(ICCID, "123"), new OutgoingMessageRequest());
        } catch (SmsClientException ex) {
            assert(ex.getMessage().equals("Incorrect response: 404"));
            throw  new SmsClientException(ex.getMessage());
        }
    }

    @Test
    public void shouldSerializeBodyCorrectly() throws IOException, NoSuchFieldException, IllegalAccessException, JSONException {
        SendMessageRequest message = SendMessageRequest.builder()
                .withSender(Address.phoneNumber("123"))
                .withReceiver(Address.phoneNumber("245"))
                .withMessage("test text").build();
        client.sendMessage(Address.phoneNumber("123"), new OutgoingMessageRequest(message));

        final ArgumentCaptor<Request> captor = ArgumentCaptor.forClass(Request.class);
        verify(client.getAuthorizedTemplate()).execute(captor.capture());

        final HttpEntityEnclosingRequest actualRequest = getFieldValue(captor.getValue());

        String expected = "{\"outboundSMSMessageRequest\":{" +
                "\"senderAddress\":{\"number\":\"123\",\"protocol\":\"MSISDN\"}," +
                "\"clientCorrelator\":null," +
                "\"senderName\":null," +
                "\"outboundSMSTextMessage\":{\"message\":\"test text\"}," +
                "\"address\":[{\"number\":\"245\",\"protocol\":\"MSISDN\"}]," +
                "\"receiptRequest\":{\"notifyURL\":null,\"callbackData\":null}," +
                "\"deviceId\":null}}";

        assertEquals(expected, EntityUtils.toString(actualRequest.getEntity()), JSONCompareMode.NON_EXTENSIBLE);
    }

    @Test
    @Ignore
    public void shouldUseSecureEndpoint() throws IOException {
        client.sendMessage(new Address(), new OutgoingMessageRequest());
        client.getMessage(new Address(), "");
        client.getAllMessages(new Address());

        ArgumentCaptor<Request> captor = ArgumentCaptor.forClass(Request.class);
        verify(client.getAuthorizedTemplate()).execute(captor.capture());
        assert(captor.getValue().toString().equals("testBaseUrl/service/sms/smsmessaging/outbound/{senderAddress}/requests"));

        verify(client.getAuthorizedTemplate()).execute(captor.capture());
        assert(captor.getValue().toString().equals("testBaseUrl/service/sms/smsmessaging/inbound/registrations/{receiveAddress}/messages"));

        verify(client.getAuthorizedTemplate()).execute(captor.capture());
        assert(captor.getValue().toString().equals("testBaseUrl/service/sms/smsmessaging/inbound/registrations/{receiveAddress}/messages/{messageId}"));
    }

    private HttpEntityEnclosingRequest getFieldValue(Request value) throws NoSuchFieldException, IllegalAccessException {
        final Field field = Request.class.getDeclaredField("request");
        field.setAccessible(true);
        return (HttpEntityEnclosingRequest) field.get(value);
    }

}
