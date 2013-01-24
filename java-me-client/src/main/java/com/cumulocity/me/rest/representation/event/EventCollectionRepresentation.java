package com.cumulocity.me.rest.representation.event;

import com.cumulocity.me.lang.List;
import com.cumulocity.me.rest.representation.BaseCollectionRepresentation;

public class EventCollectionRepresentation extends BaseCollectionRepresentation {

    private List events;

    public List getEvents() {
        return events;
    }

    public void setEvents(List events) {
        this.events = events;
    }

}
