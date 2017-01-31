package com.cumulocity.java.sms.client.request;

import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.cumulocity.java.sms.client.properties.Properties;
import com.cumulocity.model.sms.Address;
import com.cumulocity.model.sms.OutgoingMessageRequest;
import com.cumulocity.model.sms.OutgoingMessageResponse;


public class MicroserviceRequest {
    
    private final RestTemplate restTemplate;
    private Properties properties;
    public MicroserviceRequest() {
        this.restTemplate = new RestTemplate();
        this.properties = Properties.getInstance();
    }
    
    public OutgoingMessageResponse sendSmsRequest(Address senderAddress, OutgoingMessageRequest outgoingMessageRequest) {
        ResponseEntity<OutgoingMessageResponse> response = restTemplate.postForEntity(sendEndpoint() + "{senderAddress}/requests" , outgoingMessageRequest, OutgoingMessageResponse.class, senderAddress);
        return response.getBody();
    }
    
    //TODO implement return type of the function
    public void getSmsMessages(Address receiveAddress) {
        restTemplate.getForObject(receiveEndpoint() + "{receiveAddress}/messages", OutgoingMessageResponse.class, receiveAddress);
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
        return properties.getBaseUrl() + "/service/sms/smsmessaging/v1/";
    }
}
