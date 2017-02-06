package com.cumulocity.sms.client;

import org.springframework.web.client.RestTemplate;

import com.cumulocity.model.sms.Address;
import com.cumulocity.model.sms.IncomingMessage;
import com.cumulocity.model.sms.IncomingMessages;
import com.cumulocity.model.sms.OutgoingMessageRequest;
import com.cumulocity.model.sms.SendMessageRequest;
import com.cumulocity.sms.client.messaging.MessagingClient;
import com.cumulocity.sms.client.properties.Properties;

public class SmsMessagingApiImpl implements SmsMessagingApi {

    private final MessagingClient messagingClient;
    
    public SmsMessagingApiImpl(String host, RestTemplate authorizedTemplate) {
        Properties properties = new Properties();
        properties.setBaseUrl(host);
        properties.setAuthorizedTemplate(authorizedTemplate);
        
        messagingClient = new MessagingClient(properties);
    }

    @Override
    public void sendMessage(SendMessageRequest messageRequest) {
        if (messageRequest == null || messageRequest.getSenderAddress() == null) {
            throw new NullPointerException("Message request and its sender address can not be null");
        }
        OutgoingMessageRequest outgoingMessageRequest = new OutgoingMessageRequest(messageRequest);
        messagingClient.send(messageRequest.getSenderAddress(), outgoingMessageRequest);
    }

    @Override
    public IncomingMessages getAllMessages(Address receiveAddress) {
        if (receiveAddress == null) {
            throw new NullPointerException("Receive address can not be null");
        }
        return messagingClient.getAllMessages(receiveAddress);
    }

    @Override
    public IncomingMessage getMessage(Address receiveAddress, String messageId) {
        if (receiveAddress == null || messageId == null) {
            throw new NullPointerException("Receive address and message id can not be null");
        }
        return messagingClient.getMessage(receiveAddress, messageId);
        
    }

}
