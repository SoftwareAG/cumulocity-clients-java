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
