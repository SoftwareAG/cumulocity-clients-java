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

package com.cumulocity.sdk.client;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

public abstract class Filter {

    public static String encode(String value) {
        try {
            return URLEncoder.encode(value, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("UTF-8 is not supported!?", e);
        }
    }

    public Map<String, String> getQueryParams() {
        Map<String, String> params = new HashMap<>();
        Class clazz = getClass();
        for (Field field : clazz.getDeclaredFields()) {
            if(isTechnicalProxy(field)){
                continue;
            }
            field.setAccessible(true);
            String value = (String) safelyGetFieldValue(field, this);
            if (value != null) {
                params.put(getParamName(field), encode(value));
            }
        }
        return params;
    }

    private String getParamName(Field field) {
        if (field.getAnnotation(ParamSource.class).value().isEmpty()) {
            return field.getName();
        }
        return field.getAnnotation(ParamSource.class).value();
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

    private boolean isTechnicalProxy(Field field) {
        return field.getName().equals("$jacocoData");
    }
}
