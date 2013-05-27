package com.cumulocity.sdk.client.notification;

import java.util.Map;

import org.cometd.client.transport.LongPollingTransport;
import org.eclipse.jetty.client.ContentExchange;
import org.eclipse.jetty.client.HttpClient;

import com.cumulocity.sdk.client.RestConnector;

public class CumulocityLongPollingTransport extends LongPollingTransport {
    private final String applicationKey;

    CumulocityLongPollingTransport(Map<String, Object> options, HttpClient httpClient, String applicationKey) {
        super(options, httpClient);
        this.applicationKey = applicationKey;
    }

    @Override
    protected void customize(ContentExchange exchange) {
        super.customize(exchange);
        exchange.addRequestHeader(RestConnector.X_CUMULOCITY_APPLICATION_KEY, applicationKey);
    }
}