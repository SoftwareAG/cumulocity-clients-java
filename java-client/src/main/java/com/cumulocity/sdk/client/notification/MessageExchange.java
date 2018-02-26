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

import com.sun.jersey.api.client.AsyncWebResource;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientRequest;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.ClientResponse.Status;
import com.sun.jersey.api.client.async.FutureListener;
import org.cometd.bayeux.Message;
import org.cometd.client.transport.TransportListener;
import org.cometd.common.TransportException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.text.ParseException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;

import static java.lang.Integer.MAX_VALUE;
import static java.util.concurrent.TimeUnit.SECONDS;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON_TYPE;

class MessageExchange {

    private static final int ASCII_SPACE = 0x20;

    /* wait time in millis before trying to reconnect again */
    long reconnectionWaitingTime = SECONDS.toMillis(30);

    private final Logger log = LoggerFactory.getLogger(MessageExchange.class);

    private final CumulocityLongPollingTransport transport;

    private final TransportListener listener;

    private final Message[] messages;

    private volatile Future<?> request;

    private final ConnectionHeartBeatWatcher watcher;

    private final UnauthorizedConnectionWatcher unauthorizedConnectionWatcher;

    private final ScheduledExecutorService executorService;

    private final Client client;

    private final List<MessageExchangeListener> listeners = new LinkedList<MessageExchangeListener>();

    MessageExchange(CumulocityLongPollingTransport transport, Client client, ScheduledExecutorService executorService,
                    TransportListener listener, ConnectionHeartBeatWatcher watcher,
                    UnauthorizedConnectionWatcher unauthorizedConnectionWatcher, Message... messages) {
        this.transport = transport;
        this.client = client;
        this.executorService = executorService;
        this.listener = listener;
        this.messages = messages;
        this.watcher = watcher;
        this.unauthorizedConnectionWatcher = unauthorizedConnectionWatcher;
    }

    public void execute(String url, String content) {
        startWatcher();
        final AsyncWebResource endpoint = client.asyncResource(url);
        request = endpoint.handle(createRequest(endpoint.getURI(), content), new ResponseHandler());
    }

    private void startWatcher() {
        log.debug("starting heartbeat watcher {}", (Object) messages);
        watcher.start();
    }

    public void cancel() {
        log.debug("canceling {}", (Object) messages);
        listener.onException(new RuntimeException("request cancelled"), messages);
        request.cancel(true);
        onFinish();
    }

    private ClientRequest createRequest(URI uri, final String content) {
        return request(uri).type(APPLICATION_JSON_TYPE).build(content);
    }

    private BayeuxRequestBuilder request(URI uri) {
        return new BayeuxRequestBuilder(uri);
    }

    private void onFinish() {
        for (MessageExchangeListener listener : listeners) {
            listener.onFinish();
        }

        log.debug("stopping heartbeat watcher {}", (Object) messages);
        watcher.stop();
    }

    public void addListener(MessageExchangeListener listener) {
        listeners.add(listener);
    }

    public void removeListener(MessageExchangeListener listener) {
        listeners.remove(listener);
    }

    final class ResponseConsumer implements Runnable {

        private final ClientResponse response;

        public ResponseConsumer(ClientResponse response) {
            this.response = response;
        }

        @Override
        public void run() {
            try {
                heartBeatWatch(response);
                getMessagesFromResponse(response);
            } catch (Exception e) {
                onConnectionFailed(e);
            } finally {
                try {
                    onFinish();
                } finally {
                    response.close();
                }
            }
        }

        private void heartBeatWatch(ClientResponse clientResponse) throws IOException {
            if (isOk(clientResponse)) {
                if (!isCanGetHeatBeats(clientResponse)) {
                    clientResponse.setEntityInputStream(new BufferedInputStream(response.getEntityInputStream()));
                }
                getHeartBeats(clientResponse);
            }
        }

        private boolean isOk(ClientResponse clientResponse) {
            return clientResponse.getClientResponseStatus() == Status.OK;
        }

        private boolean isCanGetHeatBeats(final ClientResponse response) {
            return response.getEntityInputStream().markSupported();
        }

        private void getHeartBeats(final ClientResponse response) throws IOException {
            log.debug("getting heartbeants  {}", response);
            InputStream entityInputStream = response.getEntityInputStream();
            entityInputStream.mark(MAX_VALUE);
            int value = -1;
            while ((value = entityInputStream.read()) >= 0) {
                if (isHeartBeat(value)) {
                    log.debug("recived heartbeat");
                    watcher.heartBeat();
                    entityInputStream.mark(MAX_VALUE);
                } else {
                    log.debug("new messages recived");
                    entityInputStream.reset();
                    break;
                }
            }
        }

        private boolean isNullOrEmpty(String content) {
            return content == null || content.length() == 0;
        }

        private boolean isHeartBeat(int value) {
            return value == ASCII_SPACE;
        }

        private void getMessagesFromResponse(ClientResponse clientResponse) {
            if (isOk(clientResponse)) {
                String content = clientResponse.getEntity(String.class);
                if (!isNullOrEmpty(content)) {
                    try {
                        handleContent(content);
                    } catch (ParseException x) {
                        onException(x);
                    }
                } else {
                    onTransportException(204);
                }
            } else {
                onTransportException(clientResponse.getStatus());
            }
        }

        private void onException(Exception x) {
            log.debug("request failed ", x);
            waitBeforeAnotherReconnect();
            listener.onException(x, messages);
        }

        private void onException(final int code) {
            Map<String, Object> failure = new HashMap<>(2);
            failure.put("httpCode", code);
            onException(new TransportException(failure));
        }

        private void waitBeforeAnotherReconnect() {
            try {
                Thread.sleep(reconnectionWaitingTime);
            } catch (InterruptedException e) {
                log.error("Problem occurred while waiting for another bayeux reconnect");
            }
        }

        private void onTransportException(int code) {
            log.debug("request failed with code {}", code);
            if (code == 401) {
                unauthorizedConnectionWatcher.unauthorizedAccess();
                if (unauthorizedConnectionWatcher.shouldRetry()) {
                    onException(code);
                }
            } else {
                onException(code); 
            }
        }

        private void onConnectionFailed(Exception e) {
            log.error("connection failed " + e.getMessage(), e);

            unauthorizedConnectionWatcher.resetCounter();
            listener.onConnectException(e, messages);
        }

        private void handleContent(String content) throws ParseException {
            List<Message.Mutable> messages = transport.parseMessages(content);
            log.debug("Received messages {}", messages);
            listener.onMessages(messages);
        }

    }

    final class ResponseHandler implements FutureListener<ClientResponse> {

        @Override
        public void onComplete(Future<ClientResponse> f) throws InterruptedException {
            try {
                if (!f.isCancelled()) {
                    log.debug("wait for response headers {}", (Object) messages);
                    ClientResponse response = f.get();
                    log.debug("recived response headers {} ", (Object) messages);
                    request = executorService.submit(new ResponseConsumer(response));
                }
            } catch (Exception e) {
                log.error("connection failed " + e.getMessage(), e);

                unauthorizedConnectionWatcher.resetCounter();
                listener.onConnectException(e, messages);
                onFinish();
            }
        }
    }

}
