package com.cumulocity.java.sms.client.messaging;

import com.cumulocity.java.sms.client.properties.Properties;
import com.cumulocity.java.sms.client.request.MicroserviceRequest;
import com.cumulocity.model.sms.Address;
import com.cumulocity.model.sms.IncomingMessage;
import com.cumulocity.model.sms.IncomingMessages;
import com.cumulocity.model.sms.OutgoingMessageRequest;

public class MessagingClient {
    
    private MicroserviceRequest microserviceRequest;
    
    public MessagingClient(Properties properties) {
        microserviceRequest = new MicroserviceRequest(properties);
    }
    
    public void send(Address senderAddress, OutgoingMessageRequest outgoingMessageRequest) {
        microserviceRequest.sendSmsRequest(senderAddress, outgoingMessageRequest);
    }
    
    public IncomingMessages getAllMessages(Address receiveAddress) {
        return microserviceRequest.getSmsMessages(receiveAddress);
    }
    
    public IncomingMessage getMessage(Address receiveAddress, String messageId) {
        return microserviceRequest.getSmsMessage(receiveAddress, messageId);
    }
    
}
