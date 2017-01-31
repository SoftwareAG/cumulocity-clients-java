package com.cumulocity.java.sms.client;

import com.cumulocity.java.sms.client.messaging.IncomingMessagingClient;
import com.cumulocity.java.sms.client.messaging.OutgoingMessagingClient;
import com.cumulocity.model.sms.Address;
import com.cumulocity.model.sms.OutgoingMessageRequest;

public class SmsMessagingApiImpl implements SmsMessagingApi {

    private final OutgoingMessagingClient outgoingMessagingClient;
    private final IncomingMessagingClient incomingMessagingClient;
    
    public SmsMessagingApiImpl() {
        outgoingMessagingClient = new OutgoingMessagingClient();
        incomingMessagingClient = new IncomingMessagingClient();
    }
    
    @Override
    public void sendMessage(Address senderAddress, OutgoingMessageRequest outgoingMessageRequest) {
        outgoingMessagingClient.send(senderAddress, outgoingMessageRequest);
    }

    @Override
    public void receiveMessages(Address receiveAddress) {
        incomingMessagingClient.receive(receiveAddress);
    }

}
