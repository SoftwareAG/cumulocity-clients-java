/*
 * Copyright (C) 2013 Cumulocity GmbH
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of 
 * this software and associated documentation files (the "Software"),
 * to deal in the Software without restriction, including without limitation the rights to use,
 * copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software,
 * and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
 * OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE,
 * ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.cumulocity.sdk.client.notification;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.cometd.bayeux.client.ClientSession;
import org.cometd.client.BayeuxClient;
import org.cometd.client.BayeuxClient.State;
import org.cometd.client.transport.ClientTransport;
import org.cometd.client.transport.LongPollingTransport;
import org.eclipse.jetty.client.HttpClient;

import com.cumulocity.common.notification.ClientSvensonJSONContext;
import com.cumulocity.sdk.client.PlatformParameters;
import com.cumulocity.sdk.client.SDKException;

class DefaultBayeuxClientProvider implements BayeuxSessionProvider {

    private static final int CONNECTED_STATE_TIMEOUT = 30;

    final PlatformParameters paramters;

    private String endpoint;

    private HttpClientProvider httpClientProvider;

    private final Class<?> endpointDataType;

    public static BayeuxSessionProvider createProvider(String endpoint, PlatformParameters paramters, Class<?> endpointDataType) {
        return createProvider(endpoint, paramters, endpointDataType, DefaultHttpClientProvider.createProvider(paramters));
    }

    public static BayeuxSessionProvider createProvider(final String endpoint, final PlatformParameters paramters,
            Class<?> endpointDataType, final HttpClientProvider httpClientProvider) {
        return new DefaultBayeuxClientProvider(endpoint, paramters, endpointDataType, httpClientProvider);
    }

    public DefaultBayeuxClientProvider(String endpoint, PlatformParameters paramters, Class<?> endpointDataType,
            HttpClientProvider httpClientProvider) {
        this.paramters = paramters;
        this.endpoint = endpoint;
        this.httpClientProvider = httpClientProvider;
        this.endpointDataType = endpointDataType;
    }

    @Override
    public ClientSession get() throws SDKException {
        return openSession(createSession());
    }

    private BayeuxClient createSession() throws SDKException {
        return new BayeuxClient(buildUrl(), createTransport(createHttpClient()));
    }

    private BayeuxClient openSession(final BayeuxClient bayeuxClient) throws SDKException {
        bayeuxClient.handshake();
        boolean handshake = bayeuxClient.waitFor(TimeUnit.SECONDS.toMillis(CONNECTED_STATE_TIMEOUT), State.CONNECTED);
        if (!handshake) {
            throw new SDKException("unable to connect to server");
        }
        return bayeuxClient;
    }

    private String buildUrl() {
        final String host = paramters.getHost();
        return (host.endsWith("/") ? host : host + "/") + endpoint;
    }

    private HttpClient createHttpClient() throws SDKException {
        return httpClientProvider.get();
    }

    private LongPollingTransport createTransport(final HttpClient httpClient) {
        LongPollingTransport transport = new CumulocityLongPollingTransport(createTransportOptions(), httpClient, paramters);
        if (!httpClient.isStarted()) {
            try {
                httpClient.start();
            } catch (Exception x) {
                throw new RuntimeException(x);
            }
        }
        return transport;
    }

    private Map<String, Object> createTransportOptions() {
        final Map<String, Object> options = new HashMap<String, Object>();
        options.put(ClientTransport.JSON_CONTEXT, new ClientSvensonJSONContext(endpointDataType));
        return options;
    }

}
