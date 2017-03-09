package com.cumulocity.sms.client;

import com.cumulocity.sms.client.messaging.MessagingClient;
import com.cumulocity.sms.client.model.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Wither;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.fluent.Executor;
import org.apache.http.impl.client.BasicCredentialsProvider;

import static com.cumulocity.sms.client.model.Protocol.ICCID;
import static org.apache.http.client.fluent.Executor.newInstance;

public class SmsMessagingApiImpl implements SmsMessagingApi {

    @Data
    @Wither
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SmsCredentialsProvider {
        private String tenant;
        private String username;
        private String password;
    }

    private final MessagingClient messagingClient;

    public SmsMessagingApiImpl(final String host, final SmsCredentialsProvider auth) {
        this(normalize(host), newInstance().use(new BasicCredentialsProvider() {
            public Credentials getCredentials(AuthScope authscope) {
                return new UsernamePasswordCredentials(auth.getTenant() + "/" + auth.getUsername(), auth.getPassword());
            }
        }));
    }

    public SmsMessagingApiImpl(final String host, Executor executor) {
        messagingClient = new MessagingClient(host, executor);
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

    private static String normalize(String host) {
        if (host.charAt(host.length() - 1) != '/') {
            host = host + "/";
        }
        return host;
    }
}
