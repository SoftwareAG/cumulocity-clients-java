package com.cumulocity.rest.representation.event;

import java.util.Iterator;
import java.util.List;

import org.svenson.JSONProperty;
import org.svenson.JSONTypeHint;

import com.cumulocity.rest.representation.BaseCollectionRepresentation;

public class EventCollectionRepresentation extends BaseCollectionRepresentation<EventRepresentation> {

    private List<EventRepresentation> events;

    public List<EventRepresentation> getEvents() {
        return events;
    }

    @JSONTypeHint(EventRepresentation.class)
    public void setEvents(List<EventRepresentation> events) {
        this.events = events;
    }

    @Override
    @JSONProperty(ignore = true)
    public Iterator<EventRepresentation> iterator() {
        return events.iterator();
    }
}
