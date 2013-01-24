/*
 * Copyright 2012 Nokia Siemens Networks 
 */
package com.cumulocity.me.sdk.client.http;

import java.io.UnsupportedEncodingException;

import com.cumulocity.me.http.WebResponse;
import com.cumulocity.me.http.WebResponseReader;
import com.cumulocity.me.model.CumulocityCharset;
import com.cumulocity.me.rest.convert.JsonConversionService;
import com.cumulocity.me.rest.json.JSONObject;
import com.cumulocity.me.rest.representation.CumulocityResourceRepresentation;
import com.cumulocity.me.sdk.SDKException;

public class JsonResponseReaderImpl implements WebResponseReader {

    private final JsonConversionService conversionService;

    public JsonResponseReaderImpl(JsonConversionService conversionService) {
        this.conversionService = conversionService;
    }

    public CumulocityResourceRepresentation read(WebResponse response, int expectedStatus, Class expectedEntityType) {
        checkStatus(response, expectedStatus);
        return getEntity(response, expectedEntityType);
    }

    private CumulocityResourceRepresentation getEntity(WebResponse response, Class entityType) {
        if (response.getData() == null || entityType == null) {
            return null;
        }
        JSONObject json = new JSONObject(getJsonSource(response));
        return (CumulocityResourceRepresentation) conversionService.fromJson(json, entityType);
    }

    private String getJsonSource(WebResponse response) {
        try {
            return new String(response.getData(), CumulocityCharset.CHARSET);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public static void checkStatus(WebResponse response, int expectedStatusCode) throws SDKException {
        int status = response.getStatus();
        if (status != expectedStatusCode) {
            throw new SDKException(status, "Http status code: " + status);
        }
    }
}
