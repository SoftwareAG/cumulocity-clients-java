package com.cumulocity.me.rest.representation.alarm;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import com.cumulocity.me.rest.representation.alarm.AlarmRepresentation;
import com.cumulocity.rest.representation.audit.AuditRecordCollectionRepresentation;
import com.cumulocity.me.rest.representation.inventory.ManagedObjectRepresentation;
import com.nsn.cumulocity.model.builder.AbstractObjectBuilder;

public class AlarmRepresentationBuilder extends AbstractObjectBuilder<AlarmRepresentation> {

    private final Set<Object> dynamicProperties = new HashSet<Object>();

    public AlarmRepresentationBuilder withStatus(String value) {
        setObjectField("status", value);
        return this;
    }

    public AlarmRepresentationBuilder withSeverity(String value) {
        setObjectField("severity", value);
        return this;
    }

    public AlarmRepresentationBuilder withHistory(AuditRecordCollectionRepresentation value) {
        setObjectField("history", value);
        return this;
    }

    public AlarmRepresentationBuilder withText(String value) {
        setObjectField("text", value);
        return this;
    }

    public AlarmRepresentationBuilder withSource(ManagedObjectRepresentation value) {
        setObjectField("managedObject", value);
        return this;
    }

    public AlarmRepresentationBuilder withType(String value) {
        setObjectField("type", value);
        return this;
    }

    public AlarmRepresentationBuilder withTime(Date value) {
        setObjectField("time", value);
        return this;
    }

    public AlarmRepresentationBuilder withCreationTime(Date value) {
        setObjectField("creationTime", value);
        return this;
    }

    @Override
    protected AlarmRepresentation createDomainObject() {
        AlarmRepresentation alarm = new AlarmRepresentation();
        for (Object object : dynamicProperties) {
            alarm.set(object);
        }
        return alarm;
    }

    public AlarmRepresentationBuilder with(final Object object) {
        dynamicProperties.add(object);
        return this;
    }

}
