package com.cumulocity.mqtt.connect.client.websocket;

import com.cumulocity.mqtt.connect.client.MqttMessageListener;
import com.cumulocity.mqtt.connect.client.model.MqttMessage;
import lombok.extern.slf4j.Slf4j;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

@Slf4j
class MqttWebSocketClient extends WebSocketClient {

    private static final MqttMessageListener EMPTY_LISTENER = new MqttMessageListener() {
        @Override
        public void onMessage(final MqttMessage message) {
            // Do Nothing
        }

        @Override
        public void onError(final Throwable t) {
            // Do Nothing
        }
    };

    private final MqttMessageListener listener;

    private String connectionError;

    public MqttWebSocketClient(URI serverUri) {
        this(serverUri, EMPTY_LISTENER);
    }

    public MqttWebSocketClient(URI serverUri, MqttMessageListener listener) {
        super(serverUri);
        this.listener = listener;
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
    public void onMessage(String message) {
        final WebSocketMessage webSocketMessage = WebSocketMessage.parse(message);

        final Optional<String> ackHeader = webSocketMessage.getAckHeader();
        final byte[] avroPayload = webSocketMessage.getAvroPayload();

        listener.onMessage(MqttMessageConverter.decode(avroPayload));

        ackHeader.ifPresent(this::send);
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
        listener.onError(e);
    }

}
