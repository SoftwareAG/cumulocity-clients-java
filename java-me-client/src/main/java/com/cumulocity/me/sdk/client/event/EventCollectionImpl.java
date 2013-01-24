/*
 * Copyright 2012 Nokia Siemens Networks 
 */
package com.cumulocity.me.sdk.client.event;

import com.cumulocity.me.rest.representation.CumulocityMediaType;
import com.cumulocity.me.rest.representation.event.EventCollectionRepresentation;
import com.cumulocity.me.rest.representation.event.EventMediaType;
import com.cumulocity.me.sdk.client.http.RestConnector;
import com.cumulocity.me.sdk.client.page.PagedCollectionResource;
import com.cumulocity.me.sdk.client.page.PagedCollectionResourceImpl;

public class EventCollectionImpl extends PagedCollectionResourceImpl implements
        PagedCollectionResource {

    public EventCollectionImpl(RestConnector restConnector, String url, int pageSize) {
        super(restConnector, url, pageSize);
    }

    protected CumulocityMediaType getMediaType() {
        return EventMediaType.EVENT_COLLECTION;
    }

    protected Class getResponseClass() {
        return EventCollectionRepresentation.class;
    }
}
