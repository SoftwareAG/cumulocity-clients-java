package com.cumulocity.java.sms.client.request;

import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;

import com.cumulocity.java.sms.client.properties.Properties;
import com.cumulocity.model.sms.Address;
import com.cumulocity.model.sms.IncomingMessage;
import com.cumulocity.model.sms.IncomingMessages;
import com.cumulocity.model.sms.OutgoingMessageRequest;
import com.cumulocity.model.sms.OutgoingMessageResponse;


public class MicroserviceRequest {
    
    private final Properties properties;
    
    public MicroserviceRequest(Properties properties) {
        this.properties = properties;
    }
    
    public OutgoingMessageResponse sendSmsRequest(Address senderAddress, OutgoingMessageRequest outgoingMessageRequest) {

        MultiValueMap<String, String> headers = new LinkedMultiValueMap<String, String>();
        headers.add("Content-Type", "application/json");
        HttpEntity<OutgoingMessageRequest> request = new HttpEntity<OutgoingMessageRequest>(outgoingMessageRequest, headers);
        
        try {
            ResponseEntity<OutgoingMessageResponse> response = 
                    properties.getAuthorizedTemplate()
                    .postForEntity(sendEndpoint() + "{senderAddress}/requests", request, OutgoingMessageResponse.class, senderAddress);
            
            if (response == null || response.getBody() == null) {
                return null;
            } else {
                return response.getBody();
            }
        
        } catch (RestClientException e) {
            throw new SmsClientException("Send sms request failure", e);
        }
    }
    
    public IncomingMessages getSmsMessages(Address receiveAddress) {
        try {
            IncomingMessages messages = properties.getAuthorizedTemplate().getForObject(receiveEndpoint() + "{receiveAddress}/messages", IncomingMessages.class, receiveAddress);
            return messages;
        } catch(RestClientException e) {
            throw new SmsClientException("Get sms messages failure", e);
        }
    }
    
    public IncomingMessage getSmsMessage(Address receiveAddress, String messageId) {
        try {
            IncomingMessage message = properties.getAuthorizedTemplate().getForObject(receiveEndpoint() + "{receiveAddress}/messages/{messageId}", IncomingMessage.class, receiveAddress, messageId);
            return message;
        } catch(RestClientException e) {
            throw new SmsClientException("Get sms message failure", e);
        }
    }
    
    private String sendEndpoint() {
        String endpoint = microserviceApiEndpoint() + "outbound/";
        return endpoint;
    }
    
    private String receiveEndpoint() {
        String endpoint = microserviceApiEndpoint() + "inbound/registrations/";
        return endpoint;
    }
    
    private String microserviceApiEndpoint() {
        return properties.getBaseUrl() + "service/sms/smsmessaging/";
    }
}
