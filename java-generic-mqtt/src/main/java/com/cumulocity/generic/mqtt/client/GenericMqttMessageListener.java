package com.cumulocity.generic.mqtt.client;

import com.cumulocity.generic.mqtt.client.model.GenericMqttMessage;
import org.java_websocket.framing.CloseFrame;

import java.net.URI;

/**
 * Implement this interface to handle events from the websocket server after {@link GenericMqttSubscriber#subscribe(GenericMqttMessageListener)}.
 */
public interface GenericMqttMessageListener {

    /**
     * Called after an opening handshake has been performed and the given websocket is ready to be
     * written on.
     *
     * @param serverUri the server URI the websocket is connected to.
     */
    void onOpen(URI serverUri);

    /**
     * Callback for GenericMqttMessage messages received from the remote host. The messages will be acknowledged if no exception is raised.
     *
     * @param genericMqttMessage The {@link GenericMqttMessage} that was received.
     */
    void onMessage(GenericMqttMessage genericMqttMessage);

    /**
     * Called when errors occurs. If an error causes the websocket connection to fail {@link
     * #onClose(int, String, boolean)} will be called additionally.<br> This method will be called
     * primarily because of IO or protocol errors.<br> If the given exception is an RuntimeException
     * that probably means that you encountered a bug.<br>
     *
     * @param t The {@code Throwable} class that is causing this error.
     */
    void onError(Throwable t);

    /**
     * Called after the websocket connection has been closed.
     *
     * @param code   The codes can be looked up here: {@link CloseFrame}
     * @param reason Additional information string
     * @param remote Returns whether the closing of the connection was initiated by the remote
     *               host.
     */
    void onClose(int code, String reason, boolean remote);

}
