package com.cumulocity.me.rest.convert;

import java.util.ArrayList;
import java.util.List;

import com.cumulocity.me.rest.json.JSONArray;
import com.nsn.cumulocity.model.builder.AbstractObjectBuilder;

public class JSONArrayBuilder extends AbstractObjectBuilder<JSONArray> {

    private final List<Object> values = new ArrayList<Object>();
    private final List<AbstractObjectBuilder<?>> builders = new ArrayList<AbstractObjectBuilder<?>>();

    public static final JSONArrayBuilder aJSONArray() {
        return new JSONArrayBuilder();
    }
    
    private JSONArrayBuilder() {
    }
    
    public JSONArrayBuilder withProperty(Object value) {
        values.add(value);
        return this;
    }
    
    public JSONArrayBuilder withPropertyBuilder(AbstractObjectBuilder<?> value) {
        builders.add(value);
        return this;
    }
    
    @Override
    protected JSONArray createDomainObject() {
        JSONArray json = new JSONArray();

        int i = 0;
        for (Object object : values) {
            json.put(i, object);
            i++;
        }

        for (AbstractObjectBuilder<?> builder : builders) {
            json.put(i, builder.build());
            i++;
        }
        return json;
    }
}
