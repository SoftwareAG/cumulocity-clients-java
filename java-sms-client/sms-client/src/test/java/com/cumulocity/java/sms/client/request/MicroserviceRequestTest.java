package com.cumulocity.java.sms.client.request;

import static org.junit.Assert.*;

import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.http.HttpEntity;
import org.springframework.web.client.RestTemplate;

import com.cumulocity.java.sms.client.properties.Properties;
import com.cumulocity.model.sms.Address;
import com.cumulocity.model.sms.IncomingMessage;
import com.cumulocity.model.sms.IncomingMessages;
import com.cumulocity.model.sms.OutgoingMessageRequest;
import com.cumulocity.model.sms.OutgoingMessageResponse;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

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
        when(authorizedTemplate.postForEntity(anyString(), (HttpEntity) any(), eq(OutgoingMessageResponse.class), (Address) any())).thenThrow(Exception.class);
        
        sendSmsRequest(new Address(), new OutgoingMessageRequest());
    }
    
    @Test(expected = SmsClientException.class)
    public void shouldThrowExceptionWhenGetMessagesRequestFails() {
        when(authorizedTemplate.getForObject(anyString(), eq(IncomingMessages.class), (Address) any())).thenThrow(Exception.class);
        
        getSmsMessages(new Address());
    }
    
    @Test(expected = SmsClientException.class)
    public void shouldThrowExceptionWhenGetMessageRequestFails() {
        when(authorizedTemplate.getForObject(anyString(), eq(IncomingMessage.class), (Address) any(), anyString())).thenThrow(Exception.class);
        
        getSmsMessage(new Address(), "1");
    }
    
    @Test
    public void shouldSendCorrectContentTypeInHeader() {
        sendSmsRequest(new Address(), new OutgoingMessageRequest());

        ArgumentCaptor<HttpEntity<OutgoingMessageRequest>> captor = new ArgumentCaptor<HttpEntity<OutgoingMessageRequest>>();
        verify(authorizedTemplate).postForEntity(anyString(), captor.capture(), eq(OutgoingMessageResponse.class), (Address) any());
        HttpEntity<OutgoingMessageRequest> actualHttpEntity = captor.getValue();
        HttpHeaders actualHeaders = actualHttpEntity.getHeaders();
        assertEquals(MediaType.APPLICATION_JSON, actualHeaders.getContentType());
    }

}
