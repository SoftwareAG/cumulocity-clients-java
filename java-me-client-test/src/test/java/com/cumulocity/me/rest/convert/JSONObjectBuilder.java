package com.cumulocity.me.rest.convert;

import java.util.Map;

import com.cumulocity.me.rest.json.JSONObject;
import com.nsn.cumulocity.model.builder.AbstractObjectBuilder;

public class JSONObjectBuilder extends AbstractObjectBuilder<JSONObject> {

    public static final JSONObjectBuilder aJSONObject() {
        return new JSONObjectBuilder();
    }
    
    private JSONObjectBuilder() {
    }
    
    public JSONObjectBuilder withProperty(String key, Object value) {
        setObjectField(key, value);
        return this;
    }
    
    public JSONObjectBuilder withPropertyBuilder(String key, AbstractObjectBuilder<?> value) {
        setObjectFieldBuilder(key, value);
        return this;
    }
    
    @Override
    protected JSONObject createDomainObject() {
        return new JSONObject();
    }
    
    protected void fillInValues(JSONObject domainObject) {
        for (Map.Entry<String, Object> entry : super.values.entrySet()) {
            domainObject.put(entry.getKey(), entry.getValue());
        }
    }
    
    protected void fillInBuilderValues(JSONObject domainObject) {
        for (Map.Entry<String, AbstractObjectBuilder<?>> entry : builders.entrySet()) {
            domainObject.put(entry.getKey(), entry.getValue().build());
        }
    }
}
