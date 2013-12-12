package com.cumulocity.sdk.client.inventory;

import com.cumulocity.rest.representation.inventory.ManagedObjectCollectionRepresentation;
import com.cumulocity.rest.representation.inventory.ManagedObjectRepresentation;
import com.cumulocity.sdk.client.PagedCollectionIterable;
import com.cumulocity.sdk.client.PagedCollectionRepresentation;
import com.cumulocity.sdk.client.PagedCollectionResource;

public class PagedManagedObjectCollectionRepresentation extends ManagedObjectCollectionRepresentation
        implements PagedCollectionRepresentation<ManagedObjectRepresentation> {

    private final PagedCollectionResource<ManagedObjectRepresentation, ? extends ManagedObjectCollectionRepresentation>
            collectionResource;

    public PagedManagedObjectCollectionRepresentation(ManagedObjectCollectionRepresentation collection,
            PagedCollectionResource<ManagedObjectRepresentation, ? extends ManagedObjectCollectionRepresentation> collectionResource) {
        setManagedObjects(collection.getManagedObjects());
        setPageStatistics(collection.getPageStatistics());
        setSelf(collection.getSelf());
        setNext(collection.getNext());
        setPrev(collection.getPrev());
        this.collectionResource = collectionResource;
    }

    @Override
    public Iterable<ManagedObjectRepresentation> allPages() {
        return new PagedCollectionIterable<ManagedObjectRepresentation, ManagedObjectCollectionRepresentation>(
                collectionResource, this);
    }

    @Override
    public Iterable<ManagedObjectRepresentation> elements(int limit) {
        return new PagedCollectionIterable<ManagedObjectRepresentation, ManagedObjectCollectionRepresentation>(
                collectionResource, this, limit);
    }
}
