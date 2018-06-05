package com.cumulocity.sms.client;

import com.cumulocity.model.sms.*;
import com.cumulocity.sms.client.messaging.MessagingClient;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.fluent.Executor;

import static com.cumulocity.sms.client.messaging.MessagingClient.addLeadingSlash;

@Slf4j
@Getter
public class SmsMessagingApiImpl implements SmsMessagingApi {

    private final MessagingClient messagingClient;

    public SmsMessagingApiImpl(final String host, Executor executor) {
        final String url = addLeadingSlash(host) + "service/messaging/smsmessaging/v1";
        messagingClient = new MessagingClient(url, executor);
    }

    @Override
    public void sendMessage(SendMessageRequest messageRequest) {
        if (messageRequest == null || messageRequest.getSenderAddress() == null) {
            throw new NullPointerException("Message request and its sender address can not be null");
        }
        final OutgoingMessageRequest outgoingMessageRequest = new OutgoingMessageRequest(messageRequest);
        messagingClient.sendMessage(messageRequest.getSenderAddress(), outgoingMessageRequest);
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
