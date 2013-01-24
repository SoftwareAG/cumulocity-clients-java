/*
 * Copyright 2012 Nokia Siemens Networks 
 */
package com.cumulocity.sdk.client;

import com.cumulocity.rest.representation.BaseCumulocityResourceRepresentation;

/**
 * Generic Cumulocity operationCollection representation
 */
public interface GenericResource<T extends BaseCumulocityResourceRepresentation> {
    public T get() throws SDKException;
}
