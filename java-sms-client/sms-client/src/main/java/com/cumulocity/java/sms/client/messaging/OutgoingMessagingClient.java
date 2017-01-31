package com.cumulocity.java.sms.client.messaging;

import com.cumulocity.model.sms.Address;
import com.cumulocity.model.sms.OutgoingMessageRequest;

public class OutgoingMessagingClient extends MessagingClient {

    public void send(Address senderAddress, OutgoingMessageRequest outgoingMessageRequest) {
        microserviceRequest.sendSmsRequest(senderAddress, outgoingMessageRequest);
    }
}
