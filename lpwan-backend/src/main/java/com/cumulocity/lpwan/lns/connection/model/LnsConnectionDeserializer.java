/*
 * Copyright (c) 2012-2020 Cumulocity GmbH
 * Copyright (c) 2020-2022 Software AG, Darmstadt, Germany and/or Software AG USA Inc., Reston, VA, USA, and/or its subsidiaries and/or its affiliates and/or their licensors.
 *
 * Use, reproduction, transfer, publication or disclosure is prohibited except as specifically provided for in your License Agreement with Software AG.
 */

package com.cumulocity.lpwan.lns.connection.model;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;

public class LnsConnectionDeserializer extends StdDeserializer<LnsConnection> {

    private static Class<? extends LnsConnection> lnsConnectionClass;
    private static String agentName;

    protected LnsConnectionDeserializer() {
        super(LnsConnection.class);
    }

    public static void registerLnsConnectionConcreteClass(String agentName, Class<? extends LnsConnection> lnsConnectionClass) {
        com.cumulocity.lpwan.lns.connection.model.LnsConnectionDeserializer.agentName = agentName;
        com.cumulocity.lpwan.lns.connection.model.LnsConnectionDeserializer.lnsConnectionClass = lnsConnectionClass;
    }

    public static String getRegisteredAgentName() {
        return agentName;
    }

    @Override
    public LnsConnection deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        return jsonParser.getCodec().treeToValue(jsonParser.readValueAsTree(), lnsConnectionClass);
    }
}
