/*
 * Copyright 2012 Nokia Siemens Networks 
 */
package com.cumulocity.sdk.client;

import com.cumulocity.rest.representation.BaseCumulocityResourceRepresentation;
import com.cumulocity.rest.representation.CumulocityMediaType;

public abstract class GenericResourceImpl<T extends BaseCumulocityResourceRepresentation> implements GenericResource<T> {

    @Deprecated
    /**
     * Don't use this field outside package. Scope of this field will be reduced.
     */
    public final String url;

    protected final RestConnector restConnector;

    protected abstract CumulocityMediaType getMediaType();

    protected abstract Class<T> getResponseClass();

    public GenericResourceImpl(RestConnector restConnector, String url) {
        this.restConnector = restConnector;
        this.url = url;
    }

    @Override
    public T get() throws SDKException {
        return restConnector.get(url, getMediaType(), getResponseClass());
    }
}
