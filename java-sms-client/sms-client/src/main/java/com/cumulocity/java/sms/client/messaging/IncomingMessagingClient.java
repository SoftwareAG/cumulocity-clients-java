package com.cumulocity.java.sms.client.messaging;

import com.cumulocity.java.sms.client.messaging.model.IncomingMessages;
import com.cumulocity.model.sms.Address;

public class IncomingMessagingClient extends MessagingClient {

    public IncomingMessagingClient() {
        super();
    }
    //TODO implement return type
    public IncomingMessages receive(Address receiveAddress) {
        return microserviceRequest.getSmsMessages(receiveAddress);
    }
    
}
