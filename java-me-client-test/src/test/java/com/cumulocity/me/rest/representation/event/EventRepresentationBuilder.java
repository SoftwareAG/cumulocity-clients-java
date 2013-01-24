package com.cumulocity.me.rest.representation.event;

import java.util.Date;

import com.cumulocity.me.rest.representation.BaseRepresentationBuilder;
import com.cumulocity.me.rest.representation.inventory.ManagedObjectRepresentation;

public class EventRepresentationBuilder extends BaseRepresentationBuilder<EventRepresentation, EventRepresentationBuilder> {

    public static final EventRepresentationBuilder aEventRepresentation() {
        return new EventRepresentationBuilder();
    }
    
    public EventRepresentationBuilder withType(String type) {
        setObjectField("type", type);
        return this;
    }
    
    public EventRepresentationBuilder withTime(Date time) {
        setObjectField("time", time);
        return this;
    }
    
    public EventRepresentationBuilder withCreationTime(Date creationTime) {
        setObjectField("creationTime", creationTime);
        return this;
    }
    
    public EventRepresentationBuilder withText(String text) {
        setObjectField("text", text);
        return this;
    }
    
    public EventRepresentationBuilder withSource(ManagedObjectRepresentation source) {
        setObjectField("managedObject", source);
        return this;
    }
    
    @Override
    protected EventRepresentation createDomainObject() {
        return new EventRepresentation();
    }
}
