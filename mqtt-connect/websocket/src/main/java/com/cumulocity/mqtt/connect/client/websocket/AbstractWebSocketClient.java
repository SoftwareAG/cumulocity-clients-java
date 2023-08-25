package com.cumulocity.mqtt.connect.client.websocket;

import lombok.extern.slf4j.Slf4j;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.util.concurrent.TimeUnit;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

@Slf4j
abstract class AbstractWebSocketClient extends WebSocketClient {

    private String connectionError;

    AbstractWebSocketClient(final URI serverUri) {
        super(serverUri);
    }

    @Override
    public boolean connectBlocking(final long timeout, final TimeUnit timeUnit) throws InterruptedException {
        if (!super.connectBlocking(timeout, timeUnit)) {
            throw new RuntimeException(isNotBlank(connectionError) ? connectionError : "Cannot connect: unknown error");
        }
        return true;
    }

    @Override
    public void onOpen(ServerHandshake handshake) {
        log.debug("Web socket connection open for '{}' with status '{}' and message '{}'", uri, handshake.getHttpStatus(), handshake.getHttpStatusMessage());
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        if (!super.isOpen()) {
            connectionError = String.format("Cannot connect: %s %s", reason, code);
        }
        log.debug("Web socket connection closed for '{}' with core '{}' reason '{}' by {}", uri, code, reason, remote ? "client" : "server");
    }

    @Override
    public void onError(Exception e) {
        if (!super.isOpen()) {
            connectionError = String.format("Cannot connect: %s", e.getMessage());
        }
        log.warn("Web socket error", e);
    }

}
