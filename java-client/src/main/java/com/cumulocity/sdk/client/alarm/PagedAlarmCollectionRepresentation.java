package com.cumulocity.sdk.client.alarm;

import com.cumulocity.rest.representation.alarm.AlarmCollectionRepresentation;
import com.cumulocity.rest.representation.alarm.AlarmRepresentation;
import com.cumulocity.sdk.client.PagedCollectionIterable;
import com.cumulocity.sdk.client.PagedCollectionRepresentation;
import com.cumulocity.sdk.client.PagedCollectionResource;

public class PagedAlarmCollectionRepresentation extends AlarmCollectionRepresentation
        implements PagedCollectionRepresentation<AlarmRepresentation> {

    private final PagedCollectionResource<AlarmRepresentation, ? extends AlarmCollectionRepresentation> collectionResource;

    public PagedAlarmCollectionRepresentation(AlarmCollectionRepresentation collection,
            PagedCollectionResource<AlarmRepresentation, ? extends AlarmCollectionRepresentation> collectionResource) {
        setAlarms(collection.getAlarms());
        setPageStatistics(collection.getPageStatistics());
        setSelf(collection.getSelf());
        this.collectionResource = collectionResource;
    }

    @Override
    public Iterable<AlarmRepresentation> allPages() {
        return new PagedCollectionIterable<AlarmRepresentation, AlarmCollectionRepresentation>(
                collectionResource, this);
    }

    @Override
    public Iterable<AlarmRepresentation> elements(int limit) {
        return new PagedCollectionIterable<AlarmRepresentation, AlarmCollectionRepresentation>(
                collectionResource, this, limit);
    }
}
