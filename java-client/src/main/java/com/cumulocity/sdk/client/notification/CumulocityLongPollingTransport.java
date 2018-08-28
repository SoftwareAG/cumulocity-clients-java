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

import com.cumulocity.sdk.client.PlatformParameters;
import com.cumulocity.sdk.client.RestConnector;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientHandlerException;
import com.sun.jersey.api.client.ClientRequest;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.filter.ClientFilter;
import org.cometd.bayeux.Message;
import org.cometd.bayeux.Message.Mutable;
import org.cometd.client.transport.HttpClientTransport;
import org.cometd.client.transport.TransportListener;

import javax.ws.rs.core.NewCookie;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

import static java.util.concurrent.Executors.newScheduledThreadPool;
import static javax.ws.rs.core.HttpHeaders.COOKIE;

class CumulocityLongPollingTransport extends HttpClientTransport {

    private static final int WORKERS = 4;

    public static final String NAME = "long-polling";

    public static final String PREFIX = "long-polling.json";

    private final PlatformParameters paramters;

    private final Client httpClient;

    final List<MessageExchange> exchanges = new LinkedList<MessageExchange>();

    final ScheduledExecutorService executorService = newScheduledThreadPool(WORKERS, new CumulocityLongPollingTransportThreadFactory());

    private volatile boolean _aborted;

    private UnauthorizedConnectionWatcher unauthorizedConnectionWatcher;

    CumulocityLongPollingTransport(Map<String, Object> options, Provider<Client> httpClient, PlatformParameters paramters,
                                   UnauthorizedConnectionWatcher unauthorizedConnectionWatcher) {
        super(NAME, null, options);
        this.httpClient = new ManagedHttpClient(httpClient).get();
        setOptionPrefix(PREFIX);
        this.paramters = paramters;
        this.unauthorizedConnectionWatcher = unauthorizedConnectionWatcher;
    }

    public boolean accept(String bayeuxVersion) {
        return true;
    }

    @Override
    public void init() {
        super.init();
        _aborted = false;

    }

    @Override
    public void abort() {
        List<MessageExchange> exchanges = new ArrayList<MessageExchange>();
        synchronized (this.exchanges) {
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
    public void send(final TransportListener listener, Message.Mutable... messages) {
        debug("sending messages {} ", (Object) messages);
        final String content = generateJSON(messages);
        try {
            synchronized (exchanges) {
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
        final ConnectionHeartBeatWatcher watcher = new ConnectionHeartBeatWatcher(executorService, resolveHeartbeatInterval());
        final MessageExchange exchange = new MessageExchange(this, httpClient, executorService, listener, watcher, unauthorizedConnectionWatcher, messages);
        watcher.addConnectionListener(new ConnectionIdleListener() {
            @Override
            public void onConnectionIdle() {
                exchange.cancel();
            }
        });
        exchange.execute(getURL(), content);
        exchange.addListener(new MessageExchangeListener() {
            @Override
            public void onFinish() {
                synchronized (exchanges) {
                    exchanges.remove(exchange);
                }
            }

        });
        exchanges.add(exchange);
    }

    private long resolveHeartbeatInterval() {
        final long heartbeat = Long.getLong(CumulocityLongPollingTransport.class.getName()+ ".long-poll.heartbeat-interval", TimeUnit.MINUTES.toSeconds(12));
        logger.debug("Long poll heartbeat interval resolved to {} seconds", heartbeat);
        return heartbeat;
    }

    private ClientResponse copyCookies(final ClientResponse clientResponse) {
        for (NewCookie cookie : clientResponse.getCookies()) {
            setCookie(new Cookie(cookie.getName(), cookie.getValue(), cookie.getDomain(), cookie.getPath(), cookie.getMaxAge(),
                    cookie.isSecure(), cookie.getVersion(), cookie.getComment()));
        }
        return clientResponse;
    }

    @Override
    public void terminate() {
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

    private void addApplicationKeyHeader(ClientRequest request) {
        if (paramters.getApplicationKey() != null) {
            request.getHeaders().putSingle(RestConnector.X_CUMULOCITY_APPLICATION_KEY, paramters.getApplicationKey());
        }
    }

    public final class ManagedHttpClient implements Provider<Client> {

        private final Provider<Client> httpClient;

        public ManagedHttpClient(Provider<Client> httpClient) {
            this.httpClient = httpClient;
        }

        @Override
        public Client get() {
            final Client client = httpClient.get();
            client.setExecutorService(executorService);
            client.addFilter(new ClientFilter() {
                @Override
                public ClientResponse handle(ClientRequest cr) throws ClientHandlerException {
                    addCookieHeader(cr);
                    addApplicationKeyHeader(cr);
                    return copyCookies(getNext().handle(cr));
                }
            });
            return client;
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

}
