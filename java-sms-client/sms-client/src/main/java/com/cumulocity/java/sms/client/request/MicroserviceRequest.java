package com.cumulocity.java.sms.client.request;

import org.springframework.http.ResponseEntity;

import com.cumulocity.java.sms.client.properties.Properties;
import com.cumulocity.model.sms.Address;
import com.cumulocity.model.sms.OutgoingMessageRequest;
import com.cumulocity.model.sms.OutgoingMessageResponse;


public class MicroserviceRequest {
    
    private Properties properties = Properties.getInstance();
    
    public OutgoingMessageResponse sendSmsRequest(Address senderAddress, OutgoingMessageRequest outgoingMessageRequest) {
        /*
        ResponseEntity<OutgoingMessageResponse> response = 
                properties.getAuthorizedTemplate()
                .postForEntity(sendEndpoint() + "{senderAddress}/requests" , outgoingMessageRequest, OutgoingMessageResponse.class, senderAddress);
        return response.getBody();
        */
        ResponseEntity<String> response = 
                properties.getAuthorizedTemplate()
                .postForEntity(sendEndpoint() + "{senderAddress}/requests" , "{}", String.class, senderAddress);
        return null;
    }
    
    //TODO implement return type of the function
    public void getSmsMessages(Address receiveAddress) {
        properties.getAuthorizedTemplate().getForObject(receiveEndpoint() + "{receiveAddress}/messages", OutgoingMessageResponse.class, receiveAddress);
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
