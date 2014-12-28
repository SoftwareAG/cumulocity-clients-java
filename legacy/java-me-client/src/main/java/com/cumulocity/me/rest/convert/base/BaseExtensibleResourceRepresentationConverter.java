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

import java.util.Date;
import java.util.Enumeration;
import java.util.Stack;

import com.cumulocity.me.lang.ArrayList;
import com.cumulocity.me.lang.Iterator;
import com.cumulocity.me.lang.List;
import com.cumulocity.me.lang.Map;
import com.cumulocity.me.lang.Map.Entry;
import com.cumulocity.me.lang.Set;
import com.cumulocity.me.model.idtype.GId;
import com.cumulocity.me.model.util.ExtensibilityConverter;
import com.cumulocity.me.rest.json.JSONObject;
import com.cumulocity.me.rest.representation.AbstractExtensibleRepresentation;
import com.cumulocity.me.rest.representation.BaseResourceRepresentation;

public abstract class BaseExtensibleResourceRepresentationConverter extends BaseResourceRepresentationConverter {

    protected void basePropertiesToJson(BaseResourceRepresentation representation, JSONObject json) {
        super.basePropertiesToJson(representation, json);
        AbstractExtensibleRepresentation r = (AbstractExtensibleRepresentation) representation;
        Map attrs = r.getAttrs();
        if (!attrs.isEmpty()) {
            Iterator i = attrs.entrySet().iterator();
            while (i.hasNext()) {
                Entry entry = (Entry) i.next();
                String propertyName = (String) entry.getKey();
                Object propertyValue = entry.getValue();
                putObject(json, propertyName, propertyValue);
            }
        }
    }
    
    private final Stack parseContext = new Stack();
    
    protected void basePropertiesFromJson(JSONObject json, BaseResourceRepresentation representation) {
        ArrayList parsed = new ArrayList();
        parsed.add(PROP_SELF);
        parseContext.push(parsed);
        
        super.basePropertiesFromJson(json, representation);
    }
    
    protected void extraPropertiesFromJson(JSONObject json, BaseResourceRepresentation representation) {
        super.extraPropertiesFromJson(json, representation);
        
        AbstractExtensibleRepresentation r = (AbstractExtensibleRepresentation) representation;
        
        Enumeration en = json.keys();
        while (en.hasMoreElements()) {
            String propertyName = (String) en.nextElement();
            if (!wasParsed(propertyName)) {
                try {
                    Class propertyType = ExtensibilityConverter.classFromExtensibilityString(propertyName);
                    r.setProperty(propertyName, getObject(json, propertyName, propertyType));
                } catch (ClassNotFoundException e) {
                    r.setProperty(propertyName, getString(json, propertyName));
                }
            }
        }
        
        parseContext.pop();
    }
    
    private boolean wasParsed(String key) {
        return ((List) parseContext.peek()).contains(key);
    }

    protected int getInt(JSONObject json, String propertyName) {
        return super.getInt(json, marked(propertyName));
    }
    
    protected Date getDate(JSONObject json, String propertyName) {
        return super.getDate(json, marked(propertyName));
    }
    
    protected String getString(JSONObject json, String propertyName) {
        return super.getString(json, marked(propertyName));
    }
    
    protected GId getGId(JSONObject json) {
        return getGId(json, PROP_ID);
    }
    
    protected GId getGId(JSONObject json, String propertyName) {
        return super.getGId(json, marked(propertyName));
    }
    
    protected Object getObject(JSONObject json, String propertyName, Class propertyType) {
        return super.getObject(json, marked(propertyName), propertyType);
    }
    
    protected Set getSet(JSONObject json, String propertyName, Class propertyElementType) {
        return super.getSet(json, marked(propertyName), propertyElementType);
    }
    
    protected List getList(JSONObject json, String propertyName, Class propertyElementType) {
        return super.getList(json, marked(propertyName), propertyElementType);
    }

    private String marked(String propertyName) {
        ((List) parseContext.peek()).add(propertyName);
        return propertyName;
    }
}
