package com.cumulocity.java.sms.client.messaging;

import com.cumulocity.java.sms.client.request.MicroserviceRequest;

public class MessagingClient {
    
    protected final MicroserviceRequest microserviceRequest;
    
    public MessagingClient() {
        microserviceRequest = new MicroserviceRequest();
    }
    
    
}
