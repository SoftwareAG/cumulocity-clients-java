package com.cumulocity.java.sms.client.messaging;

import com.cumulocity.model.sms.Address;

public class IncomingMessagingClient extends MessagingClient {

    //TODO implement return type
    public void receive(Address receiveAddress) {
        microserviceRequest.getSmsMessages(receiveAddress);
    }
    
}
