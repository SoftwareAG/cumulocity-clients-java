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
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class TemplateUrlParser {

    public static String encode(String value) {
        try {
            return URLEncoder.encode(value, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("UTF-8 is unsupported???", e);
        }
    }

    public String replacePlaceholdersWithParams(String template, Map<String, String> params) {
        for (Entry<String, String> entry : params.entrySet()) {
            template = replaceAll(template, asPattern(entry.getKey()), encode(entry.getValue()));
        }
        return template;
    }

    private String replaceAll(String template, String key, String value) {
        String[] tokens = template.split("\\?");
        boolean hasQuery = tokens.length > 1;
        return replaceAllInPath(tokens[0], key, value) + (hasQuery ? replaceAllInQuery(tokens[1], key, value) : "");
    }

    private String replaceAllInPath(String template, String key, String value) {
        return template.replaceAll(key, value.replaceAll("\\+", "%20"));
    }

    private String replaceAllInQuery(String template, String key, String value) {
        return "?" + template.replaceAll(key, value);
    }

    private String asPattern(String key) {
        return "\\{" + key + "\\}";
    }
}
