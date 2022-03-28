/*
 * Copyright (c) 2012-2020 Cumulocity GmbH
 * Copyright (c) 2020-2022 Software AG, Darmstadt, Germany and/or Software AG USA Inc., Reston, VA, USA, and/or its subsidiaries and/or its affiliates and/or their licensors.
 *
 * Use, reproduction, transfer, publication or disclosure is prohibited except as specifically provided for in your License Agreement with Software AG.
 */

package com.cumulocity.lpwan.lns.instance.model;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;

public class LnsInstanceDeserializer extends StdDeserializer<LnsInstance> {

    public static volatile Class<? extends LnsInstance> lnsInstanceClass;

    protected LnsInstanceDeserializer() {
        super(LnsInstance.class);
    }

    public static void registerLnsInstanceConcreteClass(Class<? extends LnsInstance> lnsInstanceClass) {
        LnsInstanceDeserializer.lnsInstanceClass = lnsInstanceClass;
    }

    @Override
    public LnsInstance deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        return jsonParser.getCodec().treeToValue(jsonParser.readValueAsTree(), lnsInstanceClass);
    }
}
