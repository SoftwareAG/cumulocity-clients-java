/*
 * Copyright (C) 2013 Cumulocity GmbH
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of 
 * this software and associated documentation files (the "Software"),
 * to deal in the Software without restriction, including without limitation the rights to use,
 * copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software,
 * and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
 * OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE,
 * ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
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
