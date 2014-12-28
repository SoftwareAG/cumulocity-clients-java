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
