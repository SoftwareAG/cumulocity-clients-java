package com.cumulocity.java.sms.client;

import org.springframework.web.client.RestTemplate;

import com.cumulocity.java.sms.client.messaging.IncomingMessagingClient;
import com.cumulocity.java.sms.client.messaging.OutgoingMessagingClient;
import com.cumulocity.java.sms.client.properties.Properties;
import com.cumulocity.java.sms.client.request.SmsClientException;
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
    public void sendMessage(SendMessageRequest messageRequest) {
        if (messageRequest == null || messageRequest.getSenderAddress() == null) {
            throw new SmsClientException("Message request and its sender address can not be null");
        }
        OutgoingMessageRequest outgoingMessageRequest = new OutgoingMessageRequest(messageRequest);
        outgoingMessagingClient.send(messageRequest.getSenderAddress(), outgoingMessageRequest);
    }

    @Override
    public IncomingMessages getAllMessages(Address receiveAddress) {
        if (receiveAddress == null) {
            throw new SmsClientException("Receive address can not be null");
        }
        return incomingMessagingClient.getAllMessages(receiveAddress);
    }
    
    @Override
    public IncomingMessage getLastMessage(Address receiveAddress) {
        if (receiveAddress == null) {
            throw new SmsClientException("Receive address can not be null");
        }
        return incomingMessagingClient.getLastMessage(receiveAddress);
    }

    @Override
    public IncomingMessage getMessage(Address receiveAddress, String messageId) {
        if (receiveAddress == null || messageId == null) {
            throw new SmsClientException("Receive address and message id can not be null");
        }
        return incomingMessagingClient.getMessage(receiveAddress, messageId);
        
    }

}
