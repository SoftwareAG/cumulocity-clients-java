package com.cumulocity.sms.client;

import com.cumulocity.model.sms.*;
import com.cumulocity.sms.client.messaging.MessagingClient;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.fluent.Executor;
import org.apache.http.impl.client.BasicCredentialsProvider;

import java.net.URI;

import static com.cumulocity.sms.client.messaging.MessagingClient.addLeadingSlash;
import static org.apache.http.client.fluent.Executor.newInstance;

@Slf4j
@Getter
public class SmsMessagingApiImpl implements SmsMessagingApi {

    // used directly by tracker agent
    public static abstract class SmsCredentialsProvider {
        public abstract String getTenant();

        public abstract String getUsername();

        public abstract String getPassword();
    }

    private final MessagingClient messagingClient;

    public SmsMessagingApiImpl(final String host, Executor executor) {
        final String url = concat(host, "service/messaging/smsmessaging/v1");
        messagingClient = new MessagingClient(url, executor);
    }

    // used directly by tracker agent
    public SmsMessagingApiImpl(final String host, String contextPath, SmsCredentialsProvider executor) {
        final String url = concat(host, contextPath);
        messagingClient = new MessagingClient(url, createExecutor(url, executor));
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

    private String concat(String host, String contextPath) {
        return addLeadingSlash(host) + contextPath;
    }

    private static Executor createExecutor(final String url, final SmsCredentialsProvider auth) {
        return newInstance()
                .use(new BasicCredentialsProvider() {
                    public Credentials getCredentials(AuthScope authscope) {
                        return new UsernamePasswordCredentials(auth.getTenant() + "/" + auth.getUsername(), auth.getPassword());
                    }
                })
                .authPreemptive(getHost(url));
    }

    private static HttpHost getHost(String host) {
        final URI uri = URI.create(host);
        return new HttpHost(uri.getHost(), uri.getPort());
    }
}
