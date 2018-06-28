package com.cumulocity.sdk.client.option;

import com.cumulocity.rest.representation.tenant.OptionCollectionRepresentation;
import com.cumulocity.rest.representation.tenant.OptionRepresentation;
import com.cumulocity.sdk.client.PagedCollectionIterable;
import com.cumulocity.sdk.client.PagedCollectionRepresentation;
import com.cumulocity.sdk.client.PagedCollectionResource;

public class PagedTenantOptionCollectionRepresentation extends OptionCollectionRepresentation
        implements PagedCollectionRepresentation<OptionRepresentation> {
    
    private final PagedCollectionResource<OptionRepresentation, ? extends OptionCollectionRepresentation> collectionResource;

    public PagedTenantOptionCollectionRepresentation(OptionCollectionRepresentation collection,
                                              PagedCollectionResource<OptionRepresentation, ? extends OptionCollectionRepresentation> collectionResource) {
        setOptions(collection.getOptions());
        setPageStatistics(collection.getPageStatistics());
        setSelf(collection.getSelf());
        setNext(collection.getNext());
        setPrev(collection.getPrev());
        this.collectionResource = collectionResource;
    }

    @Override
    public Iterable<OptionRepresentation> allPages() {
        return new PagedCollectionIterable<>(
                collectionResource, this);
    }

    @Override
    public Iterable<OptionRepresentation> elements(int limit) {
        return new PagedCollectionIterable<>(
                collectionResource, this, limit);
    }
}
