package com.cumulocity.sdk.client.event;

import com.cumulocity.rest.representation.event.EventCollectionRepresentation;
import com.cumulocity.rest.representation.event.EventRepresentation;
import com.cumulocity.sdk.client.PagedCollectionIterable;
import com.cumulocity.sdk.client.PagedCollectionRepresentation;
import com.cumulocity.sdk.client.PagedCollectionResource;

public class PagedEventCollectionRepresentation extends EventCollectionRepresentation
        implements PagedCollectionRepresentation<EventRepresentation> {

    private final PagedCollectionResource<EventRepresentation, ? extends EventCollectionRepresentation> collectionResource;

    public PagedEventCollectionRepresentation(EventCollectionRepresentation collection,
            PagedCollectionResource<EventRepresentation, ? extends EventCollectionRepresentation> collectionResource) {
        setEvents(collection.getEvents());
        setPageStatistics(collection.getPageStatistics());
        setSelf(collection.getSelf());
        this.collectionResource = collectionResource;
    }

    @Override
    public Iterable<EventRepresentation> allPages() {
        return new PagedCollectionIterable<EventRepresentation, EventCollectionRepresentation>(
                collectionResource, this);
    }

    public Iterable<EventRepresentation> elements(int limit) {
        return new PagedCollectionIterable<EventRepresentation, EventCollectionRepresentation>(
                collectionResource, this, limit);
    }

}
