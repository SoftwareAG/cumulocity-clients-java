package com.cumulocity.java.sms.client.request;

import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.cumulocity.java.sms.client.messaging.model.IncomingMessages;
import com.cumulocity.java.sms.client.properties.Properties;
import com.cumulocity.model.sms.Address;
import com.cumulocity.model.sms.OutgoingMessageRequest;
import com.cumulocity.model.sms.OutgoingMessageResponse;


public class MicroserviceRequest {
    
    private Properties properties = Properties.getInstance();
    
    public OutgoingMessageResponse sendSmsRequest(Address senderAddress, OutgoingMessageRequest outgoingMessageRequest) {

        MultiValueMap<String, String> headers = new LinkedMultiValueMap<String, String>();
        headers.add("Content-Type", "application/json");
        HttpEntity<OutgoingMessageRequest> request = new HttpEntity<OutgoingMessageRequest>(outgoingMessageRequest, headers);
        
        ResponseEntity<OutgoingMessageResponse> response = 
                properties.getAuthorizedTemplate()
                .postForEntity(sendEndpoint() + "{senderAddress}/requests", request, OutgoingMessageResponse.class, senderAddress);
        return response.getBody();
    }
    
    public IncomingMessages getSmsMessages(Address receiveAddress) {
        IncomingMessages messages = properties.getAuthorizedTemplate().getForObject(receiveEndpoint() + "{receiveAddress}/messages", IncomingMessages.class, receiveAddress);
        return messages;
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
        return properties.getBaseUrl() + "service/sms/smsmessaging/v1/";
    }
}
