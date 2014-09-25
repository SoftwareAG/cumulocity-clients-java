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

import static javax.ws.rs.core.HttpHeaders.COOKIE;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON_TYPE;

import java.net.URI;
import java.text.ParseException;
import java.util.*;
import java.util.concurrent.*;

import org.cometd.bayeux.Message;
import org.cometd.bayeux.Message.Mutable;
import org.cometd.client.transport.HttpClientTransport;
import org.cometd.client.transport.TransportListener;

import com.cumulocity.sdk.client.PlatformParameters;
import com.cumulocity.sdk.client.RestConnector;
import com.sun.jersey.api.client.*;
import com.sun.jersey.api.client.filter.ClientFilter;
import com.sun.jersey.client.impl.ClientRequestImpl;

class CumulocityLongPollingTransport extends HttpClientTransport {

    public static final String NAME = "long-polling";

    public static final String PREFIX = "long-polling.json";

    private final PlatformParameters paramters;

    private final Client httpClient;

    final List<MessageExchange> exchanges = new LinkedList<MessageExchange>();

    final ScheduledExecutorService executorService = Executors
            .newSingleThreadScheduledExecutor(new CumulocityLongPollingTransportThreadFactory());

    private volatile boolean _aborted;

    private AsyncWebResource endpoint;

    CumulocityLongPollingTransport(Map<String, Object> options, Client httpClient, PlatformParameters paramters) {
        super(NAME, null, options);
        this.httpClient = httpClient;
        setOptionPrefix(PREFIX);
        this.paramters = paramters;
    }

    public boolean accept(String bayeuxVersion) {
        return true;
    }

    @Override
    public void init() {
        super.init();
        _aborted = false;
        endpoint = httpClient.asyncResource(getURL());
        endpoint.addFilter(new ClientFilter() {
            @Override
            public ClientResponse handle(ClientRequest cr) throws ClientHandlerException {
                addCookieHeader(cr);
                return getNext().handle(cr);
            }
        });
    }

    @Override
    public void abort() {

        List<MessageExchange> exchanges = new ArrayList<MessageExchange>();
        synchronized (this) {
            _aborted = true;
            exchanges.addAll(this.exchanges);
            this.exchanges.clear();
        }
        for (MessageExchange exchange : exchanges) {
            exchange.cancel();
        }
        executorService.shutdownNow();
    }

    @Override
    protected List<Mutable> parseMessages(String content) throws ParseException {
        return super.parseMessages(content);
    }


    @Override
    protected void setCookie(Cookie cookie) {
        super.setCookie(cookie);
    }

    @Override
    public void send(final TransportListener listener, Message.Mutable... messages) {
        debug("sending messages {} ", (Object)messages);
        final String content = generateJSON(messages);
        try {
            synchronized (this) {
                verifyState();
                createMessageExchange(listener, content, messages);
            }

        } catch (Exception x) {
            listener.onException(x, messages);
        }
    }

    private void verifyState() {
        if (_aborted)
            throw new IllegalStateException("Aborted");
    }

    private void createMessageExchange(final TransportListener listener, final String content, Message.Mutable... messages) {
        final MessageExchange exchange = new MessageExchange(this, executorService, listener, messages);
        listener.onSending(messages);
        final Future<ClientResponse> request = endpoint.handle(createRequest(content), exchange);
        exchange.setRequest(request);
        exchanges.add(exchange);
    }

    private ClientRequest createRequest(final String content) {
        return addApplicationKeyHeader(request().type(APPLICATION_JSON_TYPE)).build(content);
    }

    private BayeuxRequestBuilder request() {
        return new BayeuxRequestBuilder(endpoint.getURI());
    }

    @Override
    public void terminate() {
        super.terminate();
        executorService.shutdownNow();
    }

    protected void addCookieHeader(ClientRequest exchange) {
        CookieProvider cookieProvider = getCookieProvider();
        if (cookieProvider != null) {
            StringBuilder builder = new StringBuilder();
            for (Cookie cookie : cookieProvider.getCookies()) {
                if (builder.length() > 0)
                    builder.append("; ");
                builder.append(cookie.asString());
            }
            if (builder.length() > 0) {
                exchange.getHeaders().add(COOKIE, builder.toString());
            }
        }
    }

    private <T extends RequestBuilder<T>> T addApplicationKeyHeader(T builder) {
        if (paramters.getApplicationKey() != null) {
            builder.header(RestConnector.X_CUMULOCITY_APPLICATION_KEY, paramters.getApplicationKey());
        }
        return builder;
    }

    private class BayeuxRequestBuilder extends PartialRequestBuilder<BayeuxRequestBuilder> {

        private final URI uri;

        public BayeuxRequestBuilder(URI uri) {
            this.uri = uri;
        }

        public ClientRequest build(Object e) {
            ClientRequest ro = new ClientRequestImpl(uri, "POST", e, metadata);
            entity = null;
            metadata = null;
            return ro;
        }
    }

    private static final class CumulocityLongPollingTransportThreadFactory implements ThreadFactory {
        private int counter = 0;

        @Override
        public Thread newThread(Runnable r) {
            final Thread thread = new Thread(r);
            thread.setDaemon(true);
            thread.setName("CumulocityLongPollingTransport-scheduler-" + counter++);
            return thread;
        }
    }

    protected synchronized boolean removeExchange(MessageExchange messageExchange) {
        return exchanges.remove(messageExchange);
    }

}
