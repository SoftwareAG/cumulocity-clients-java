package com.cumulocity.me.rest.convert.event;

import com.cumulocity.me.lang.UnsupportedOperationException;
import com.cumulocity.me.rest.convert.base.BaseResourceRepresentationConverter;
import com.cumulocity.me.rest.json.JSONObject;
import com.cumulocity.me.rest.representation.BaseCumulocityResourceRepresentation;
import com.cumulocity.me.rest.representation.event.EventCollectionRepresentation;
import com.cumulocity.me.rest.representation.event.EventsApiRepresentation;

public class EventsApiRepresentationConverter extends BaseResourceRepresentationConverter {

    protected void instanceToJson(BaseCumulocityResourceRepresentation representation, JSONObject json) {
        throw new UnsupportedOperationException();
    }

    protected void instanceFromJson(JSONObject json, BaseCumulocityResourceRepresentation representation) {
        $(representation).setSelf(getString(json, "self"));
        $(representation).setEventsForType(getString(json, "eventsForType"));
        $(representation).setEventsForSource(getString(json, "eventsForSource"));
        $(representation).setEventsForSourceAndType(getString(json, "eventsForSourceAndType"));
        $(representation).setEventsForTime(getString(json, "eventsForTime"));
        $(representation).setEventsForSourceAndTime(getString(json, "eventsForSourceAndTime"));
        $(representation).setEventsForTimeAndType(getString(json, "eventsForTimeAndType"));
        $(representation).setEventsForSourceAndTimeAndType(getString(json, "eventsForSourceAndTimeAndType"));
        $(representation).setEventsForFragmentType(getString(json, "eventsForFragmentType"));
        $(representation).setEventsForSourceAndFragmentType(getString(json, "eventsForSourceAndFragmentType"));
        $(representation).setEventsForDateAndFragmentType(getString(json, "eventsForDateAndFragmentType"));
        $(representation).setEventsForFragmentTypeAndType(getString(json, "eventsForFragmentTypeAndType"));
        $(representation).setEventsForSourceAndDateAndFragmentType(getString(json, "eventsForSourceAndDateAndFragmentType"));
        $(representation).setEventsForSourceAndFragmentTypeAndType(getString(json, "eventsForSourceAndFragmentTypeAndType"));
        $(representation).setEventsForDateAndFragmentTypeAndType(getString(json, "eventsForDateAndFragmentTypeAndType"));
        $(representation).setEventsForSourceAndDateAndFragmentTypeAndType(getString(json, "eventsForSourceAndDateAndFragmentTypeAndType"));
        $(representation).setEvents((EventCollectionRepresentation) getObject(json, "events", EventCollectionRepresentation.class));
    }

    protected Class supportedRepresentationType() {
        return EventsApiRepresentation.class;
    }

    private EventsApiRepresentation $(BaseCumulocityResourceRepresentation baseRepresentation) {
        return (EventsApiRepresentation) baseRepresentation;
    }

}
