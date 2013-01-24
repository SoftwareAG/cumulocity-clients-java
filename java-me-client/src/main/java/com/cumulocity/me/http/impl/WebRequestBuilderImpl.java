package com.cumulocity.me.http.impl;

import com.cumulocity.me.http.WebClient;
import com.cumulocity.me.http.WebMethod;
import com.cumulocity.me.http.WebRequest;
import com.cumulocity.me.http.WebRequestBuilder;
import com.cumulocity.me.http.WebRequestWriter;
import com.cumulocity.me.lang.HashMap;
import com.cumulocity.me.lang.Map;
import com.cumulocity.me.rest.representation.CumulocityMediaType;
import com.cumulocity.me.rest.representation.CumulocityResourceRepresentation;

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
        headers.put(name, value);
        return this;
    }
    
    public CumulocityResourceRepresentation get(int responseStatus, Class responseEntityType) {
        return client.handle(buildRequest(WebMethod.GET, null), responseStatus, responseEntityType);
    }

    public CumulocityResourceRepresentation post(CumulocityResourceRepresentation requestEntity, int responseStatus,
            Class responseEntityType) {
        return client.handle(buildRequest(WebMethod.POST, requestEntity), responseStatus, responseEntityType);
    }

    private WebRequest buildRequest(WebMethod method, CumulocityResourceRepresentation requestEntity) {
        if (requestEntity == null) {
            return new WebRequestImpl(method, path, headers, null);
        } else {
            byte[] data = writer.write(method, requestEntity);
            headers.put("Content-Lenght", Integer.toString(data.length));
            return new WebRequestImpl(method, path, headers, data);
        }
    }

    public CumulocityResourceRepresentation delete(int responseStatus, Class responseEntityType) {
        return client.handle(buildRequest(WebMethod.DELETE, null), responseStatus, responseEntityType);
    }

    public CumulocityResourceRepresentation put(CumulocityResourceRepresentation requestEntity, int responseStatus, Class responseEntityType) {
        return client.handle(buildRequest(WebMethod.PUT, requestEntity), responseStatus, responseEntityType);
    }
}