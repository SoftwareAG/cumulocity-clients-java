package com.cumulocity.me.rest.convert.deviceControl;

import com.cumulocity.me.rest.convert.base.BaseRepresentationConverter;
import com.cumulocity.me.rest.json.JSONObject;
import com.cumulocity.model.Agent;

public class AgentConverter extends BaseRepresentationConverter {

    @Override
    protected Class<?> supportedRepresentationType() {
        return Agent.class;
    }

    @Override
    public JSONObject toJson(Object representation) {
        return new JSONObject();
    }

    @Override
    public Object fromJson(JSONObject json) {
        return new Agent();
    }
}
