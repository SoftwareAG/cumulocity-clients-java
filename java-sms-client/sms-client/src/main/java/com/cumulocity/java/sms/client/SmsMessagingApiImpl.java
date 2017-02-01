package com.cumulocity.java.sms.client;

import org.springframework.web.client.RestTemplate;

import com.cumulocity.java.sms.client.messaging.IncomingMessagingClient;
import com.cumulocity.java.sms.client.messaging.OutgoingMessagingClient;
import com.cumulocity.java.sms.client.properties.Properties;
import com.cumulocity.model.sms.Address;
import com.cumulocity.model.sms.OutgoingMessageRequest;

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
    public void sendMessage(Address senderAddress, OutgoingMessageRequest outgoingMessageRequest) {
        outgoingMessagingClient.send(senderAddress, outgoingMessageRequest);
    }

    @Override
    public void receiveMessages(Address receiveAddress) {
        incomingMessagingClient.receive(receiveAddress);
    }

}
