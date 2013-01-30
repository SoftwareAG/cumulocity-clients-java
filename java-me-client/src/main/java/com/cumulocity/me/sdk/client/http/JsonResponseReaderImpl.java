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
