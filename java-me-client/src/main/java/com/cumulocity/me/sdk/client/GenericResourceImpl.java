/*
 * Copyright 2012 Nokia Siemens Networks 
 */
package com.cumulocity.me.sdk.client;

import com.cumulocity.me.rest.representation.CumulocityMediaType;
import com.cumulocity.me.rest.representation.CumulocityResourceRepresentation;
import com.cumulocity.me.sdk.client.http.RestConnector;

public abstract class GenericResourceImpl implements GenericResource {

    protected final RestConnector restConnector;
    
    protected final String url;

    public GenericResourceImpl(RestConnector restConnector, String url) {
        this.restConnector = restConnector;
        this.url = url;
    }
    
    abstract protected CumulocityMediaType getMediaType();
    
    abstract protected Class getResponseClass();

    public CumulocityResourceRepresentation get() {
        return restConnector.get(url, getMediaType(), getResponseClass());
    }
}
