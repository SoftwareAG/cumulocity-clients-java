/*
 * Copyright 2012 Nokia Siemens Networks 
 */
package com.cumulocity.me.sdk.client;

import com.cumulocity.me.rest.representation.CumulocityResourceRepresentation;

/**
 * Generic Cumulocity operationCollection representation
 */
public interface GenericResource {
    
    CumulocityResourceRepresentation get();
    
}
