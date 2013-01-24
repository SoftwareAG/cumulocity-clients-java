/*
 * Copyright 2012 Nokia Siemens Networks 
 */
package com.cumulocity.sdk.client;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;
import java.util.Map.Entry;

public class TemplateUrlParser {

    public String replacePlaceholdersWithParams(String templateUrl, Map<String, String> params) {
        for (Entry<String, String> entry : params.entrySet()) {
            templateUrl = templateUrl.replaceAll("\\{" + entry.getKey() + "\\}", encode(entry.getValue()));
        }
        return templateUrl;
    }

    public static String encode(String url) {
        try {
            return URLEncoder.encode(url, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("UTF-8 is unsupported???", e);
        }
    }

}
