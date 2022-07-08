package com.cumulocity.rest.representation.builder;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import com.cumulocity.model.idtype.GId;
import com.cumulocity.rest.representation.alarm.AlarmRepresentation;
import com.cumulocity.rest.representation.audit.AuditRecordCollectionRepresentation;
import com.cumulocity.rest.representation.inventory.ManagedObjectRepresentation;
import com.nsn.cumulocity.model.builder.AbstractObjectBuilder;
import org.joda.time.DateTime;

public class AlarmRepresentationBuilder extends AbstractObjectBuilder<AlarmRepresentation> {
    
    private final Set<Object> dynamicProperties = new HashSet<Object>();

    public AlarmRepresentationBuilder withId(GId value) {
        setFieldValue("id", value);
        return this;
    }

    public AlarmRepresentationBuilder withStatus(String value) {
        setFieldValue("status", value);
        return this;
    }

    public AlarmRepresentationBuilder withSeverity(String value) {
        setFieldValue("severity", value);
        return this;
    }

    public AlarmRepresentationBuilder withHistory(AuditRecordCollectionRepresentation value) {
        setFieldValue("history", value);
        return this;
    }

    public AlarmRepresentationBuilder withText(String value) {
        setFieldValue("text", value);
        return this;
    }

    public AlarmRepresentationBuilder withSource(ManagedObjectRepresentation value) {
        setFieldValue("managedObject", value);
        return this;
    }

    public AlarmRepresentationBuilder withType(String value) {
        setFieldValue("type", value);
        return this;
    }

    @Deprecated
    public AlarmRepresentationBuilder withTime(Date value) {
        setFieldValue("time", value);
        return this;
    }

    public AlarmRepresentationBuilder withDateTime(DateTime value) {
        setFieldValue("time", value);
        return this;
    }

    @Deprecated
    public AlarmRepresentationBuilder withCreationTime(Date value) {
        setFieldValue("creationTime", value);
        return this;
    }

    public AlarmRepresentationBuilder withCreationDateTime(DateTime value) {
        setFieldValue("creationTime", value);
        return this;
    }

    public AlarmRepresentationBuilder withCount(Long value) {
        setFieldValue("count", value);
        return this;
    }

    public AlarmRepresentationBuilder withSelf(String value) {
        setFieldValue("self", value);
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
