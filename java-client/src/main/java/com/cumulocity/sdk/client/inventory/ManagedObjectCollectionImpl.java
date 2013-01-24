/*
 * Copyright 2012 Nokia Siemens Networks 
 */
package com.cumulocity.sdk.client.inventory;

import com.cumulocity.rest.representation.CumulocityMediaType;
import com.cumulocity.rest.representation.inventory.InventoryMediaType;
import com.cumulocity.rest.representation.inventory.ManagedObjectCollectionRepresentation;
import com.cumulocity.sdk.client.PagedCollectionResource;
import com.cumulocity.sdk.client.PagedCollectionResourceImpl;
import com.cumulocity.sdk.client.RestConnector;

public class ManagedObjectCollectionImpl extends PagedCollectionResourceImpl<ManagedObjectCollectionRepresentation> implements
        PagedCollectionResource<ManagedObjectCollectionRepresentation> {

    @Deprecated
    public ManagedObjectCollectionImpl(RestConnector restConnector, String url) {
        super(restConnector, url);
    }

    public ManagedObjectCollectionImpl(RestConnector restConnector, String url, int pageSize) {
        super(restConnector, url, pageSize);
    }

    @Override
    protected CumulocityMediaType getMediaType() {
        return InventoryMediaType.MANAGED_OBJECT_COLLECTION;
    }

    @Override
    protected Class<ManagedObjectCollectionRepresentation> getResponseClass() {
        return ManagedObjectCollectionRepresentation.class;
    }
}
