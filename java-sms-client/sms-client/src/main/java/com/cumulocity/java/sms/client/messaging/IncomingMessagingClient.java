package com.cumulocity.java.sms.client.messaging;

import com.cumulocity.model.sms.Address;
import com.cumulocity.model.sms.IncomingMessage;
import com.cumulocity.model.sms.IncomingMessages;

public class IncomingMessagingClient extends MessagingClient {

    public IncomingMessagingClient() {
        super();
    }
    
    public IncomingMessages getAllMessages(Address receiveAddress) {
        return microserviceRequest.getSmsMessages(receiveAddress);
    }
    
    public IncomingMessage getLastMessage(Address receiveAddress) {
        IncomingMessages messages = microserviceRequest.getSmsMessages(receiveAddress);
        return messages.getLastByDateTime();
    }
    
    public IncomingMessage getMessage(Address receiveAddress, String messageId) {
        return microserviceRequest.getSmsMessage(receiveAddress, messageId);
    }
    
}
