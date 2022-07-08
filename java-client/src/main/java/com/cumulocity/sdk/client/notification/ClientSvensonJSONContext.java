package com.cumulocity.sdk.client.notification;

import java.util.Map;

import org.cometd.bayeux.Message;
import org.cometd.bayeux.Message.Mutable;
import org.cometd.common.HashMapMessage;
import org.cometd.common.JSONContext;
import org.svenson.JSON;
import org.svenson.JSONParser;

import com.cumulocity.model.JSONBase;

public class ClientSvensonJSONContext extends BaseSvensonJSONContext<Message.Mutable> implements JSONContext.Client {

    public ClientSvensonJSONContext() {
        super(JSONBase.getJSONGenerator(), JSONBase.getJSONParser());
    }

    public ClientSvensonJSONContext(Class<?> dataType) {
        super(JSONBase.getJSONGenerator(), JSONBase.getJSONParser(), dataType);
    }

    public ClientSvensonJSONContext(JSON jsonGenerator, JSONParser jsonParser) {
        super(jsonGenerator, jsonParser);
    }

    public ClientSvensonJSONContext(JSON jsonGenerator, JSONParser jsonParser, Class<?> dataType) {
        super(jsonGenerator, jsonParser, dataType);
    }

    @SuppressWarnings("rawtypes")
    @Override
    protected Class targetClass() {
        return HashMapMessage.class;
    }

    @Override
    protected Mutable toMessage(Map<String, Object> messageProperties) {
        final HashMapMessage message = new HashMapMessage();
        message.putAll(messageProperties);
        return message;
    }
}
