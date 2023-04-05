package com.cumulocity.generic.mqtt.client.websocket;

import com.cumulocity.generic.mqtt.client.GenericMqttConfiguration;

public class GenericMqttWebSocketPropertyConstants extends GenericMqttConfiguration {

    private static final String WS_NAMESPACE = ROOT_PROPERTIES_NAMESPACE + "ws.";

    public static final String WS_CONNECTION_TIMEOUT = WS_NAMESPACE + "connection.timeout";

}
