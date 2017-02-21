package com.cumulocity.sms.client.request;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.cumulocity.model.sms.Address;
import com.cumulocity.model.sms.IncomingMessage;
import com.cumulocity.model.sms.IncomingMessages;
import com.cumulocity.model.sms.OutgoingMessageRequest;
import com.cumulocity.model.sms.SendMessageRequest;
import com.cumulocity.sms.client.properties.Properties;
import com.cumulocity.sms.client.request.MicroserviceRequest;
import com.cumulocity.sms.client.request.SmsClientException;

public class MicroserviceRequestTest extends MicroserviceRequest {

    private RestTemplate authorizedTemplate = mock(RestTemplate.class);;
    private static Properties properties = mock(Properties.class);
    
    public MicroserviceRequestTest() {
        super(properties);
    }

    @Before
    public void setup() {
        when(properties.getAuthorizedTemplate()).thenReturn(authorizedTemplate);
        when(properties.getBaseUrl()).thenReturn("testBaseUrl");
    }
    
    @Test(expected = SmsClientException.class)
    public void shouldThrowExceptionWhenSendRequestFails() {
        when(authorizedTemplate.postForEntity(anyString(), (HttpEntity) any(), eq(String.class), (Address) any())).thenThrow(RestClientException.class);
        
        sendSmsRequest(new Address(), new OutgoingMessageRequest());
    }
    
    @Test(expected = SmsClientException.class)
    public void shouldThrowExceptionWhenGetMessagesRequestFails() {
        when(authorizedTemplate.getForObject(anyString(), eq(IncomingMessages.class), (Address) any())).thenThrow(RestClientException.class);
        
        getSmsMessages(new Address());
    }
    
    @Test(expected = SmsClientException.class)
    public void shouldThrowExceptionWhenGetMessageRequestFails() {
        when(authorizedTemplate.getForObject(anyString(), eq(IncomingMessage.class), (Address) any(), anyString())).thenThrow(RestClientException.class);
        
        getSmsMessage(new Address(), "1");
    }
    
    @Test
    public void shouldSendCorrectContentTypeInHeader() {
        sendSmsRequest(new Address(), new OutgoingMessageRequest());

        ArgumentCaptor<HttpEntity<String>> captor = new ArgumentCaptor<HttpEntity<String>>();
        verify(authorizedTemplate).postForEntity(anyString(), captor.capture(), eq(String.class), (Address) any());
        HttpEntity<String> actualHttpEntity = captor.getValue();
        HttpHeaders actualHeaders = actualHttpEntity.getHeaders();
        assertEquals(MediaType.APPLICATION_JSON, actualHeaders.getContentType());
    }
    
    @Test
    public void shouldUseSecureEndpoint() {
        sendSmsRequest(new Address(), new OutgoingMessageRequest());
        getSmsMessage(new Address(), "");
        getSmsMessages(new Address());
        
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(authorizedTemplate).postForEntity(captor.capture(), (HttpEntity) any(), eq(String.class), (Address) any());
        String postEndpoint = captor.getValue();
        
        assertEquals("testBaseUrl" + "service/sms/smsmessaging/outbound/{senderAddress}/requests", postEndpoint);
        
        verify(authorizedTemplate).getForObject(captor.capture(), eq(IncomingMessages.class), (Address) any());
        String receiveAllEndpoint = captor.getValue();
        
        assertEquals("testBaseUrl" + "service/sms/smsmessaging/inbound/registrations/{receiveAddress}/messages", receiveAllEndpoint);
        
        verify(authorizedTemplate).getForObject(captor.capture(), eq(IncomingMessage.class), (Address) any(), anyString());
        String receiveSpecificEndpoint = captor.getValue();
        
        assertEquals("testBaseUrl" + "service/sms/smsmessaging/inbound/registrations/{receiveAddress}/messages/{messageId}", receiveSpecificEndpoint);
        
    }
    
    @Test
    public void shouldSerializeBodyCorrectly() {
        SendMessageRequest message = SendMessageRequest.builder()
                .withSender(Address.phoneNumber("123"))
                .withReceiver(Address.phoneNumber("245"))
                .withMessage("test text").build();
        sendSmsRequest(Address.phoneNumber("123"), new OutgoingMessageRequest(message));

        ArgumentCaptor<HttpEntity<String>> captor = new ArgumentCaptor<HttpEntity<String>>();
        verify(authorizedTemplate).postForEntity(anyString(), captor.capture(), eq(String.class), (Address) any());
        HttpEntity<String> actualHttpEntity = captor.getValue();
        String actualBody = actualHttpEntity.getBody();
        assertEquals(actualBody,
                "{\"outboundSMSMessageRequest\":{\"address\":[{\"number\":\"245\"," +
                "\"protocol\":\"MSISDN\"}],\"clientCorrelator\":null,\"deviceId\":null,\"outboundSMSTextMessage\":{\"message\":\"test text\"}," +
                "\"receiptRequest\":{\"callbackData\":null,\"notifyURL\":null},\"senderAddress\":{" +
                "\"number\":\"123\",\"protocol\":\"MSISDN\"},\"senderName\":null}}");
    }

}
