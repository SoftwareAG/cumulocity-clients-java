package com.cumulocity.java.sms.client;

import com.cumulocity.model.sms.Address;
import com.cumulocity.model.sms.OutgoingMessageRequest;

public interface SmsMessagingApi {

    public void sendMessage(Address senderAddress, OutgoingMessageRequest request);
    public void receiveMessages(Address receiveAddress);
    
}
