package com.cumulocity.sdk.client.identity;

import com.cumulocity.rest.representation.identity.ExternalIDCollectionRepresentation;
import com.cumulocity.rest.representation.identity.ExternalIDRepresentation;
import com.cumulocity.sdk.client.PagedCollectionIterable;
import com.cumulocity.sdk.client.PagedCollectionRepresentation;
import com.cumulocity.sdk.client.PagedCollectionResource;

public class PagedExternalIDCollectionRepresentation extends ExternalIDCollectionRepresentation
        implements PagedCollectionRepresentation<ExternalIDRepresentation> {

    private final PagedCollectionResource<ExternalIDRepresentation, ? extends ExternalIDCollectionRepresentation>
            collectionResource;

    public PagedExternalIDCollectionRepresentation(ExternalIDCollectionRepresentation collection,
            PagedCollectionResource<ExternalIDRepresentation, ? extends ExternalIDCollectionRepresentation> collectionResource) {
        this.collectionResource = collectionResource;
        setExternalIds(collection.getExternalIds());
        setPageStatistics(collection.getPageStatistics());
        setSelf(collection.getSelf());
    }

    @Override
    public Iterable<ExternalIDRepresentation> allPages() {
        return new PagedCollectionIterable<ExternalIDRepresentation, ExternalIDCollectionRepresentation>(
                collectionResource, this);
    }

    @Override
    public Iterable<ExternalIDRepresentation> elements(int limit) {
        return new PagedCollectionIterable<ExternalIDRepresentation, ExternalIDCollectionRepresentation>(
                collectionResource, this, limit);
    }
}
