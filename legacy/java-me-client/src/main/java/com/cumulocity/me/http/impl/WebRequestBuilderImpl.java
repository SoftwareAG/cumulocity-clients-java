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
package com.cumulocity.me.http.impl;

import com.cumulocity.me.http.WebClient;
import com.cumulocity.me.http.WebMethod;
import com.cumulocity.me.http.WebRequest;
import com.cumulocity.me.http.WebRequestBuilder;
import com.cumulocity.me.http.WebRequestWriter;
import com.cumulocity.me.lang.HashMap;
import com.cumulocity.me.lang.Map;
import com.cumulocity.me.rest.representation.CumulocityMediaType;
import com.cumulocity.me.rest.representation.ResourceRepresentation;

public class WebRequestBuilderImpl implements WebRequestBuilder {
    
    private final WebClient client;
    private final WebRequestWriter writer;
    private final String path;
    private final Map headers;

    public WebRequestBuilderImpl(WebClient client, WebRequestWriter writer, String path) {
        this.client = client;
        this.writer = writer;
        this.path = path;
        this.headers = new HashMap();
    }
    
    public WebRequestBuilder type(CumulocityMediaType mediaType) {
        headers.put("Content-Type", mediaType.getTypeString());
        return this;
    }
    
    public WebRequestBuilder accept(CumulocityMediaType mediaType) {
        headers.put("Accept", mediaType.getTypeString());
        return this;
    }
    
    public WebRequestBuilder header(String name, Object value) {
        if (name != null && value != null) {
            headers.put(name, value);
        }
        return this;
    }
    
    public ResourceRepresentation get(int responseStatus, Class responseEntityType) {
        return client.handle(buildRequest(WebMethod.GET, null), responseStatus, responseEntityType);
    }

    public ResourceRepresentation post(ResourceRepresentation requestEntity, int responseStatus,
            Class responseEntityType) {
        return client.handle(buildRequest(WebMethod.POST, requestEntity), responseStatus, responseEntityType);
    }

    private WebRequest buildRequest(WebMethod method, ResourceRepresentation requestEntity) {
        if (requestEntity == null) {
            return new WebRequestImpl(method, path, headers, null);
        } else {
            byte[] data = writer.write(method, requestEntity);
            headers.put("Content-Lenght", Integer.toString(data.length));
            return new WebRequestImpl(method, path, headers, data);
        }
    }

    public ResourceRepresentation delete(int responseStatus, Class responseEntityType) {
        return client.handle(buildRequest(WebMethod.DELETE, null), responseStatus, responseEntityType);
    }

    public ResourceRepresentation put(ResourceRepresentation requestEntity, int responseStatus, Class responseEntityType) {
        return client.handle(buildRequest(WebMethod.PUT, requestEntity), responseStatus, responseEntityType);
    }
}