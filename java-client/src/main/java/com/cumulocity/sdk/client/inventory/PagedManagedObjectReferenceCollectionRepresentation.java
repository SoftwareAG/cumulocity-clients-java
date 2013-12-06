package com.cumulocity.sdk.client.inventory;

import com.cumulocity.rest.representation.inventory.ManagedObjectReferenceCollectionRepresentation;
import com.cumulocity.rest.representation.inventory.ManagedObjectReferenceRepresentation;
import com.cumulocity.sdk.client.PagedCollectionIterable;
import com.cumulocity.sdk.client.PagedCollectionRepresentation;
import com.cumulocity.sdk.client.PagedCollectionResource;

public class PagedManagedObjectReferenceCollectionRepresentation extends ManagedObjectReferenceCollectionRepresentation
        implements PagedCollectionRepresentation<ManagedObjectReferenceRepresentation> {

    private final PagedCollectionResource<ManagedObjectReferenceRepresentation, ? extends ManagedObjectReferenceCollectionRepresentation>
            collectionResource;

    public PagedManagedObjectReferenceCollectionRepresentation(ManagedObjectReferenceCollectionRepresentation collection,
            PagedCollectionResource<ManagedObjectReferenceRepresentation, ? extends ManagedObjectReferenceCollectionRepresentation> collectionResource) {
        this.collectionResource = collectionResource;
        setReferences(collection.getReferences());
        setPageStatistics(collection.getPageStatistics());
        setSelf(collection.getSelf());
    }

    @Override
    public Iterable<ManagedObjectReferenceRepresentation> allPages() {
        return new PagedCollectionIterable<ManagedObjectReferenceRepresentation, ManagedObjectReferenceCollectionRepresentation>(
                collectionResource, this);
    }

    @Override
    public Iterable<ManagedObjectReferenceRepresentation> elements(int limit) {
        return new PagedCollectionIterable<ManagedObjectReferenceRepresentation, ManagedObjectReferenceCollectionRepresentation>(
                collectionResource, this, limit);
    }
}
