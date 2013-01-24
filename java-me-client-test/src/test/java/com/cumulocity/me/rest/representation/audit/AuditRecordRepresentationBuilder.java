package com.cumulocity.me.rest.representation.audit;

import com.cumulocity.me.lang.HashSet;
import com.cumulocity.me.lang.Iterator;
import com.cumulocity.me.lang.Set;
import com.nsn.cumulocity.model.builder.AbstractObjectBuilder;

public class AuditRecordRepresentationBuilder extends AbstractObjectBuilder<AuditRecordRepresentation> {

    private final Set dynamicProperties = new HashSet();

    public static final AuditRecordRepresentationBuilder aAuditRecordRepresentation() {
        return new AuditRecordRepresentationBuilder();
    }
    
    public AuditRecordRepresentationBuilder withUser(String user) {
        setObjectField("user", user);
        return this;
    }

    public AuditRecordRepresentationBuilder withApplication(String application) {
        setObjectField("application", application);
        return this;
    }

    public AuditRecordRepresentationBuilder withSeverity(String severity) {
        setObjectField("severity", severity);
        return this;
    }

    public AuditRecordRepresentationBuilder withActivity(String activity) {
        setObjectField("activity", activity);
        return this;
    }

    public AuditRecordRepresentationBuilder withChanges(HashSet changes) {
        setObjectField("changes", changes);
        return this;
    }

    @Override
    protected AuditRecordRepresentation createDomainObject() {
        AuditRecordRepresentation audit = new AuditRecordRepresentation();
        Iterator iterator = dynamicProperties.iterator();
        while(iterator.hasNext()) {
            audit.set(iterator.next());
        }
        return audit;
    }

    public AuditRecordRepresentationBuilder with(final Object object) {
        dynamicProperties.add(object);
        return this;
    }

}