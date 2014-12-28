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
package com.cumulocity.me.rest.representation;

import com.cumulocity.me.lang.Collections;
import com.cumulocity.me.lang.HashMap;
import com.cumulocity.me.lang.Iterator;
import com.cumulocity.me.lang.Map;
import com.cumulocity.me.lang.Map.Entry;

public class BaseMediaType {
    
    /** The value of a type or subtype wildcard: "*" */
    public static final String MEDIA_TYPE_WILDCARD = "*";

    private String type;
    private String subtype;
    private Map parameters;
    
	public BaseMediaType(String type, String subtype, Map parameters) {
		this.type = type == null ? MEDIA_TYPE_WILDCARD : type;
		this.subtype = subtype == null ? MEDIA_TYPE_WILDCARD : subtype;
		if (parameters == null) {
			this.parameters = Collections.emptyMap();
		} else {
			Map map = new HashMap();
			Iterator iterator = parameters.entrySet().iterator();
			while (iterator.hasNext()) {
				Map.Entry e = (Entry) iterator.next();
				map.put(((String) e.getKey()).toLowerCase(), e.getValue());
			}
			this.parameters = map;
		}
	}
    
    public BaseMediaType(String type, String subtype) {
        this(type, subtype, Collections.emptyMap());
    }

    /**
     * Creates a new instance of MediaType, both type and subtype are wildcards.
     * Consider using the constant {@link #WILDCARD_TYPE} instead.
     */
    public BaseMediaType() {
        this(MEDIA_TYPE_WILDCARD, MEDIA_TYPE_WILDCARD);
    }

    public String getType() {
        return this.type;
    }
    
    public String getSubtype() {
        return this.subtype;
    }
    
    public Map getParameters() {
        return parameters;
    }
}
