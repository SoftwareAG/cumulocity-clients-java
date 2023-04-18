package com.cumulocity.mqtt.connect.client.websocket;

import com.cumulocity.mqtt.connect.client.GenericMqttMessageListener;
import com.cumulocity.mqtt.connect.client.converter.GenericMqttMessageConverter;
import com.cumulocity.mqtt.connect.client.model.GenericMqttMessage;
import lombok.extern.slf4j.Slf4j;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.util.Optional;

@Slf4j
class GenericMqttWebSocketClient extends WebSocketClient {

    private final GenericMqttMessageConverter genericMqttMessageConverter = new GenericMqttMessageConverter();

    private final GenericMqttMessageListener listener;

    public GenericMqttWebSocketClient(URI serverUri) {
        this(serverUri, new EmptyGenericMqttMessageListener());
    }

    public GenericMqttWebSocketClient(URI serverUri, GenericMqttMessageListener listener) {
        super(serverUri);
        this.listener = listener;
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

        final GenericMqttMessage genericMqttMessage = genericMqttMessageConverter.decode(avroPayload);

        listener.onMessage(genericMqttMessage);

        ackHeader.ifPresent(this::send);
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        log.debug("Web socket connection closed for '{}' with core '{}' reason '{}' by {}", uri, code, reason, remote ? "client" : "server");
    }

    @Override
    public void onError(Exception e) {
        listener.onError(e);
    }

    private static class EmptyGenericMqttMessageListener implements GenericMqttMessageListener {
        @Override
        public void onMessage(GenericMqttMessage message) {
            // Do Nothing
        }

        @Override
        public void onError(Throwable t) {
            // Do Nothing
        }
    }
}
