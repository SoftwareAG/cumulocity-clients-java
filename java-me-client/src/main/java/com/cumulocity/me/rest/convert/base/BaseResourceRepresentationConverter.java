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
package com.cumulocity.me.rest.convert.base;

import com.cumulocity.me.model.idtype.GId;
import com.cumulocity.me.rest.json.JSONObject;
import com.cumulocity.me.rest.representation.BaseResourceRepresentation;
import com.cumulocity.me.util.BeanUtils;

public abstract class BaseResourceRepresentationConverter extends BaseRepresentationConverter {
    
    public static final String PROP_ID = "id";
    public static final String PROP_SELF = "self";
    
    public JSONObject toJson(Object representation) {
        if (representation == null) {
            return null;
        }
        BaseResourceRepresentation baseRepresentation = (BaseResourceRepresentation) representation;
        JSONObject json = new JSONObject();
        basePropertiesToJson(baseRepresentation, json);
        instanceToJson(baseRepresentation, json);
        extraPropertiesToJson(baseRepresentation, json);
        return json;
    }
    
    protected abstract void instanceToJson(BaseResourceRepresentation representation, JSONObject json);
    
    protected void basePropertiesToJson(BaseResourceRepresentation representation, JSONObject json) {
        putString(json, PROP_SELF, representation.getSelf());
    }
    
    protected void extraPropertiesToJson(BaseResourceRepresentation representation, JSONObject json) {
    }
    
    protected BaseResourceRepresentation newRepresentation() {
        return (BaseResourceRepresentation) BeanUtils.newInstance(supportedRepresentationType());
    }
    
    public Object fromJson(JSONObject json) {
        if (json == null) {
            return null;
        }
        BaseResourceRepresentation representation = newRepresentation();
        basePropertiesFromJson(json, representation);
        instanceFromJson(json, representation);
        extraPropertiesFromJson(json, representation);
        return representation;
    }
    
    protected abstract void instanceFromJson(JSONObject json, BaseResourceRepresentation representation);

    protected void basePropertiesFromJson(JSONObject json, BaseResourceRepresentation representation) {
        representation.setSelf(getString(json, PROP_SELF));
    }
    
    protected void extraPropertiesFromJson(JSONObject json, BaseResourceRepresentation representation) {
    }

    protected void putGId(JSONObject json, GId id) {
        putGId(json, PROP_ID, id);
    }

    protected GId getGId(JSONObject json) {
        return getGId(json, PROP_ID);
    }
    
    protected GId getGId(JSONObject json, String propertyName) {
        GId id = new GId();
        id.setValue(getString(json, propertyName));
        return id;
    }

    protected void putGId(JSONObject json, String propertyName, GId id) {
        if (id == null) {
            return;
        }
        json.put(propertyName, id.getValue());
    }
}
