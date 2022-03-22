package com.cumulocity.lpwan.lns.instance.model;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;

public class LnsInstanceDeserializer extends StdDeserializer<LnsInstance> {

    private static volatile Class<? extends LnsInstance> lnsInstanceClass;

    protected LnsInstanceDeserializer() {
        super(LnsInstance.class);
    }

    public static void registerLnsInstancePojoClass(String subLnsInstanceClass){
        try {
            lnsInstanceClass = (Class<? extends LnsInstance>) Class.forName(subLnsInstanceClass);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public LnsInstance deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException{
        return jsonParser.getCodec().treeToValue(jsonParser.readValueAsTree(), lnsInstanceClass);
    }
}
