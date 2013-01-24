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
