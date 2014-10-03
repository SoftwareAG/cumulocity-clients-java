package com.cumulocity.sdk.client.notification;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;

import javax.ws.rs.core.NewCookie;

import org.cometd.bayeux.Message;
import org.cometd.client.transport.HttpClientTransport.Cookie;
import org.cometd.client.transport.TransportListener;
import org.cometd.common.TransportException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.ClientResponse.Status;
import com.sun.jersey.api.client.async.FutureListener;

class MessageExchange implements FutureListener<ClientResponse>, Runnable {

    private static final int ASCII_SPACE = 0x20;

    private final Logger log = LoggerFactory.getLogger(MessageExchange.class);

    private final CumulocityLongPollingTransport transport;

    private final TransportListener listener;

    private final Message[] messages;

    private volatile Future<?> request;

    private volatile ClientResponse response;

    private final ConnectionHeartBeatWatcher watcher;

    private final ScheduledExecutorService executorService;

    MessageExchange(CumulocityLongPollingTransport transport, ScheduledExecutorService executorService, TransportListener listener,
            ConnectionHeartBeatWatcher watcher, Message... messages) {
        this.transport = transport;
        this.executorService = executorService;
        this.listener = listener;
        this.messages = messages;
        this.watcher = watcher;
        startWatcher();
    }

    private void startWatcher() {
        log.debug("starting heartbeat watcher {}", (Object) messages);
        watcher.start();
    }

    public void setRequest(Future<?> request) {
        this.request = request;
    }

    public void cancel() {
        log.debug("canceling {}", (Object) messages);
        listener.onConnectException(new RuntimeException("request cancelled"), messages);
        request.cancel(true);
        stopWatcher();
    }

    private void copyCookies(final ClientResponse clientResponse) {
        for (NewCookie cookie : clientResponse.getCookies()) {
            transport.setCookie(new Cookie(cookie.getName(), cookie.getValue(), cookie.getDomain(), cookie.getPath(), cookie.getMaxAge(),
                    cookie.isSecure(), cookie.getVersion(), cookie.getComment()));
        }
    }

    public void onComplete(Future<ClientResponse> f) throws InterruptedException {
        try {
            if (!f.isCancelled()) {
                log.debug("wait for response headers {}", (Object) messages);
                response = f.get();
                log.debug("recived response headers {} ", (Object) messages);
                request = executorService.submit(this);
            } else {
                throw new RuntimeException("request cancelled");
            }
        } catch (Exception e) {
            log.debug("connection failed", e);
            listener.onConnectException(e, messages);
            stopWatcher();
        }
    }

    private void heartBeatWatch(ClientResponse clientResponse) {
        try {
            if (isOk(clientResponse)) {
                if (isCanGetHeatBeats(clientResponse)) {
                    getHeartBeats(clientResponse);
                } else {
                    log.warn("unable to recive heart beats {}", (Object) messages);
                }
            }
        } catch (IOException e) {
            onConnectionFailed(e);
        }
    }

    private boolean isCanGetHeatBeats(final ClientResponse response) {
        return response.getEntityInputStream().markSupported();
    }

    private void getHeartBeats(final ClientResponse response) throws IOException {
        log.debug("getting heartbeants  {}", response);
        InputStream entityInputStream = response.getEntityInputStream();
        entityInputStream.mark(Integer.MAX_VALUE);
        int value = -1;
        while ((value = entityInputStream.read()) >= 0) {
            if (isHeartBeat(value)) {
                log.debug("recived heartbeat");
                notifyAboutHeartBeat();
                entityInputStream.mark(Integer.MAX_VALUE);
            } else {
                log.debug("new messages recived");
                entityInputStream.reset();
                break;
            }
        }
    }

    private boolean isHeartBeat(int value) {
        return value == ASCII_SPACE;
    }

    private void notifyAboutHeartBeat() {
        watcher.heatBeat();
    }

    private void getMessagesFromResponse(ClientResponse clientResponse) {
        if (!finish())
            return;
        copyCookies(clientResponse);
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

    private boolean isNullOrEmpty(String content) {
        return content == null || content.length() == 0;
    }

    private void handleContent(String content) throws ParseException {
        List<Message.Mutable> messages = transport.parseMessages(content);
        log.debug("Received messages {}", messages);
        listener.onMessages(messages);
    }

    private void onException(Exception x) {
        log.debug("request failed ", x);
        listener.onException(x, messages);
    }

    private boolean isOk(ClientResponse clientResponse) {
        return clientResponse.getClientResponseStatus() == Status.OK;
    }

    private void onTransportException(int code) {
        log.debug("request failed with code {}", code);
        Map<String, Object> failure = new HashMap<String, Object>(2);
        failure.put("httpCode", code);
        TransportException x = new TransportException(failure);
        listener.onException(x, messages);
    }

    private boolean finish() {
        return transport.removeExchange(this);
    }

    @Override
    public void run() {
        try {
            heartBeatWatch(response);
            getMessagesFromResponse(response);
        } catch (Exception e) {
            onConnectionFailed(e);
        } finally {
            stopWatcher();
        }
    }

    private void onConnectionFailed(Exception e) {
        log.debug("connection failed");
        listener.onConnectException(e, messages);
        stopWatcher();
    }

    private void stopWatcher() {
        log.debug("stopping heartbeat watcher {}", (Object) messages);
        watcher.stop();
    }

}
