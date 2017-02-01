package com.cumulocity.java.sms.client.messaging;

import com.cumulocity.java.sms.client.request.MicroserviceRequest;

public class MessagingClient {
    protected MicroserviceRequest microserviceRequest;
    
    public void MessagingClient() {
        microserviceRequest = new MicroserviceRequest();
    }
    
    
}
