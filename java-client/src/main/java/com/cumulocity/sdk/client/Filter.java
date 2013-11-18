package com.cumulocity.sdk.client;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public abstract class Filter {
    
    public Map<String, String> getQueryParams() {
        Map<String, String> params = new HashMap<String, String>();
        Class clazz = getClass();
        for (Field field : clazz.getDeclaredFields()) {
            field.setAccessible(true);
            String value = (String)safelyGetFieldValue(field, this);
            if (value != null) {
                params.put(field.getAnnotation(Name.class).value(), value);
            }
        }
        return params;
    }
    
    private Object safelyGetFieldValue(Field field, Filter filter) {
        try {
            return field.get(filter);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

}
