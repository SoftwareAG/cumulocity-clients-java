package com.cumulocity.generic.mqtt.client.websocket;

import com.cumulocity.generic.mqtt.client.GenericMqttMessageListener;
import com.cumulocity.generic.mqtt.client.converter.GenericMqttMessageConverter;
import com.cumulocity.generic.mqtt.client.model.GenericMqttMessage;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.util.Optional;

class GenericMqttWebSocketClient extends WebSocketClient {

    private final GenericMqttMessageConverter genericMqttMessageConverter = new GenericMqttMessageConverter();

    private GenericMqttMessageListener genericMqttMessageListener;

    public GenericMqttWebSocketClient(URI serverUri) {
        super(serverUri);
    }

    public GenericMqttWebSocketClient(URI serverUri, GenericMqttMessageListener genericMqttMessageListener) {
        super(serverUri);
        this.genericMqttMessageListener = genericMqttMessageListener;
    }

    @Override
    public void onOpen(ServerHandshake serverHandshake) {
        if (genericMqttMessageListener == null) {
            return;
        }

        genericMqttMessageListener.onOpen(this.uri);
    }

    @Override
    public void onMessage(String message) {
        if (genericMqttMessageListener == null) {
            return;
        }

        final WebSocketMessage webSocketMessage = WebSocketMessage.parse(message);

        final Optional<String> ackHeader = webSocketMessage.getAckHeader();
        final byte[] avroPayload = webSocketMessage.getAvroPayload();

        final GenericMqttMessage genericMqttMessage = genericMqttMessageConverter.decode(avroPayload);

        genericMqttMessageListener.onMessage(genericMqttMessage);

        if (ackHeader.isPresent()) {
            this.send(ackHeader.get());
        }
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        if (genericMqttMessageListener == null) {
            return;
        }

        genericMqttMessageListener.onClose(code, reason, remote);
    }

    @Override
    public void onError(Exception e) {
        if (genericMqttMessageListener == null) {
            return;
        }

        genericMqttMessageListener.onError(e);
    }
}
