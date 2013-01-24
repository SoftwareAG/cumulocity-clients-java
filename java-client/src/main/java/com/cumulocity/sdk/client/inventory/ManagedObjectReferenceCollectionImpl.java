/*
 * Copyright 2012 Nokia Siemens Networks 
 */
package com.cumulocity.sdk.client.inventory;

import com.cumulocity.rest.representation.CumulocityMediaType;
import com.cumulocity.rest.representation.inventory.InventoryMediaType;
import com.cumulocity.sdk.client.PagedCollectionResource;
import com.cumulocity.sdk.client.PagedCollectionResourceImpl;
import com.cumulocity.sdk.client.RestConnector;
import com.cumulocity.rest.representation.inventory.*;

public class ManagedObjectReferenceCollectionImpl extends PagedCollectionResourceImpl<ManagedObjectReferenceCollectionRepresentation> implements
        PagedCollectionResource<ManagedObjectReferenceCollectionRepresentation> {

    public ManagedObjectReferenceCollectionImpl(RestConnector restConnector, String url, int pageSize) {
        super(restConnector, url, pageSize);
    }

    @Override
    protected CumulocityMediaType getMediaType() {
        return InventoryMediaType.MANAGED_OBJECT_REFERENCE_COLLECTION;
    }

    @Override
    protected Class<ManagedObjectReferenceCollectionRepresentation> getResponseClass() {
        return ManagedObjectReferenceCollectionRepresentation.class;
    }
}
