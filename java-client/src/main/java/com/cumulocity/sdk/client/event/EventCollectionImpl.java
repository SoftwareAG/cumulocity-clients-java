/*
 * Copyright 2012 Nokia Siemens Networks 
 */
package com.cumulocity.sdk.client.event;

import com.cumulocity.rest.representation.CumulocityMediaType;
import com.cumulocity.rest.representation.event.EventCollectionRepresentation;
import com.cumulocity.rest.representation.event.EventMediaType;
import com.cumulocity.sdk.client.PagedCollectionResource;
import com.cumulocity.sdk.client.PagedCollectionResourceImpl;
import com.cumulocity.sdk.client.RestConnector;

public class EventCollectionImpl extends PagedCollectionResourceImpl<EventCollectionRepresentation> implements
        PagedCollectionResource<EventCollectionRepresentation> {

    @Deprecated
    public EventCollectionImpl(RestConnector restConnector, String url) {
        super(restConnector, url);
    }

    public EventCollectionImpl(RestConnector restConnector, String url, int pageSize) {
        super(restConnector, url, pageSize);
    }

    @Override
    protected CumulocityMediaType getMediaType() {
        return EventMediaType.EVENT_COLLECTION;
    }

    @Override
    protected Class<EventCollectionRepresentation> getResponseClass() {
        return EventCollectionRepresentation.class;
    }
}
