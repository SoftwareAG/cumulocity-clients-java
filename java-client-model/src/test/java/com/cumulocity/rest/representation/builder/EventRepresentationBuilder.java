package com.cumulocity.rest.representation.builder;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import com.cumulocity.rest.representation.event.EventRepresentation;
import com.cumulocity.rest.representation.inventory.ManagedObjectRepresentation;
import com.nsn.cumulocity.model.builder.AbstractObjectBuilder;
import org.joda.time.DateTime;

public class EventRepresentationBuilder extends AbstractObjectBuilder<EventRepresentation> {

    private final Set<Object> dynamicProperties = new HashSet<Object>();

    public EventRepresentationBuilder withText(String value) {
        setFieldValue("text", value);
        return this;
    }

    public EventRepresentationBuilder withSource(ManagedObjectRepresentation value) {
        setFieldValue("managedObject", value);
        return this;
    }

    public EventRepresentationBuilder withType(String value) {
        setFieldValue("type", value);
        return this;
    }

    @Deprecated
    public EventRepresentationBuilder withTime(Date value) {
        setFieldValue("time", value);
        return this;
    }

    public EventRepresentationBuilder withDateTime(DateTime value) {
        setFieldValue("time", value);
        return this;
    }

    @Deprecated
    public EventRepresentationBuilder withCreationTime(Date value) {
        setFieldValue("creationTime", value);
        return this;
    }

    public EventRepresentationBuilder withCreationDateTime(DateTime value) {
        setFieldValue("creationTime", value);
        return this;
    }

    @Override
    protected EventRepresentation createDomainObject() {
        EventRepresentation builder = new EventRepresentation();
        for (Object object : dynamicProperties) {
            builder.set(object);
        }
        return builder;
    }

    public EventRepresentationBuilder with(final Object object) {
        dynamicProperties.add(object);
        return this;
    }

}
