package com.cumulocity.me.rest.convert.event;

import com.cumulocity.me.rest.convert.base.BaseCollectionRepresentationConverter;
import com.cumulocity.me.rest.json.JSONObject;
import com.cumulocity.me.rest.representation.BaseCumulocityResourceRepresentation;
import com.cumulocity.me.rest.representation.event.EventCollectionRepresentation;
import com.cumulocity.me.rest.representation.event.EventRepresentation;

public class EventCollectionRepresentationConverter extends BaseCollectionRepresentationConverter {

    private static final String PROP_EVENTS = "events";
    
    protected void instanceToJson(BaseCumulocityResourceRepresentation representation, JSONObject json) {
        putList(json, PROP_EVENTS, $(representation).getEvents());
    }

    protected void instanceFromJson(JSONObject json, BaseCumulocityResourceRepresentation representation) {
        $(representation).setEvents(getList(json, PROP_EVENTS, EventRepresentation.class));
    }

    protected Class supportedRepresentationType() {
        return EventCollectionRepresentation.class;
    }
    
    private EventCollectionRepresentation $(BaseCumulocityResourceRepresentation representation) {
        return (EventCollectionRepresentation) representation;
    }

}
