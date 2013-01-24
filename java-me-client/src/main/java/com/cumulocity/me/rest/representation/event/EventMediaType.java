package com.cumulocity.me.rest.representation.event;

import com.cumulocity.me.rest.representation.BaseCumulocityMediaType;

public class EventMediaType extends BaseCumulocityMediaType {

    public static final EventMediaType EVENT = new EventMediaType("event");

    public static final String EVENT_TYPE = APPLICATION_VND_COM_NSN_CUMULOCITY + "event+json;" + VND_COM_NSN_CUMULOCITY_PARAMS;

    public static final EventMediaType EVENT_COLLECTION = new EventMediaType("eventCollection");

    public static final String EVENT_COLLECTION_TYPE = APPLICATION_VND_COM_NSN_CUMULOCITY + "eventCollection+json;" + VND_COM_NSN_CUMULOCITY_PARAMS;

    public static final EventMediaType EVENT_API = new EventMediaType("eventApi");

    public static final String EVENTS_API_TYPE = APPLICATION_VND_COM_NSN_CUMULOCITY + "eventApi+json;" + VND_COM_NSN_CUMULOCITY_PARAMS;

    public EventMediaType(String string) {
        super(string);
    }
}

