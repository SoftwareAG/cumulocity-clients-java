package com.cumulocity.java.sms.client;

import org.springframework.web.client.RestTemplate;

import com.cumulocity.java.sms.client.messaging.IncomingMessagingClient;
import com.cumulocity.java.sms.client.messaging.OutgoingMessagingClient;
import com.cumulocity.java.sms.client.properties.Properties;
import com.cumulocity.model.sms.Address;
import com.cumulocity.model.sms.IncomingMessage;
import com.cumulocity.model.sms.IncomingMessages;
import com.cumulocity.model.sms.OutgoingMessageRequest;
import com.cumulocity.model.sms.SendMessageRequest;

public class SmsMessagingApiImpl implements SmsMessagingApi {

    private final OutgoingMessagingClient outgoingMessagingClient;
    private final IncomingMessagingClient incomingMessagingClient;
    private Properties properties = Properties.getInstance();
    
    public SmsMessagingApiImpl() {
        outgoingMessagingClient = new OutgoingMessagingClient();
        incomingMessagingClient = new IncomingMessagingClient();
    }
    
    public SmsMessagingApiImpl(String host, RestTemplate authorizedTemplate) {
        this();
        properties.setBaseUrl(host);
        properties.setAuthorizedTemplate(authorizedTemplate);
    }

    @Override
    public void sendMessage(Address senderAddress, SendMessageRequest messageRequest) {
        OutgoingMessageRequest outgoingMessageRequest = new OutgoingMessageRequest(messageRequest);
        outgoingMessagingClient.send(senderAddress, outgoingMessageRequest);
    }

    @Override
    public IncomingMessages getAllMessages(Address receiveAddress) {
        return incomingMessagingClient.getAllMessages(receiveAddress);
    }
    
    @Override
    public IncomingMessage getLastMessage(Address receiveAddress) {
        return incomingMessagingClient.getLastMessage(receiveAddress);
    }

    @Override
    public IncomingMessage getMessage(Address receiveAddress, String messageId) {
        return incomingMessagingClient.getMessage(receiveAddress, messageId);
        
    }

}
