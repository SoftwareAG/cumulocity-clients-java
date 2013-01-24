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

import com.cumulocity.me.lang.ArrayList;
import com.cumulocity.me.lang.Collection;
import com.cumulocity.me.lang.Iterator;
import com.cumulocity.me.rest.json.JSONObject;

public class JsonConversionServiceImpl implements JsonConversionService {

    private final Collection converters = new ArrayList();
    
    public JsonConversionServiceImpl() {
    }
    
    public JsonConversionServiceImpl(Collection collection) {
		Iterator iterator = collection.iterator();
        while (iterator.hasNext()) {
            Object converter = iterator.next();
            if (!(converter instanceof JsonConverter)) {
                throw new IllegalArgumentException(converter + " must be a JsonConverter!");
            }
            register((JsonConverter) converter);
        }
    }

    public void register(JsonConverter converter) {
        if (converter instanceof JsonConversionServiceAware) {
            ((JsonConversionServiceAware) converter).setJsonConversionService(this);
        }
        this.converters.add(converter);
    }
    
    public JSONObject toJson(Object representation) {
        if (representation == null) {
            return null;
        }
        return findConverter(representation.getClass()).toJson(representation);
    }

    public Object fromJson(JSONObject json, Class expectedType) {
        if (json == null) {
            return null;
        }
        return findConverter(expectedType).fromJson(json);
    }
    
    private JsonConverter findConverter(Class representationType) {
    	Iterator iterator = converters.iterator();
        while (iterator.hasNext()) {
            JsonConverter jsonConverter = ((JsonConverter) iterator.next());
            if (jsonConverter.supports(representationType)) {
                return jsonConverter;
            }
        }
        throw new ConversionException("Unsupported representation: " + representationType);
    }
}
