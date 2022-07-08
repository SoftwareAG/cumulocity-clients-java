package com.cumulocity.rest.representation.builder;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import com.cumulocity.model.ID;
import com.cumulocity.model.builder.IDBuilder;
import com.cumulocity.rest.representation.inventory.ManagedObjectReferenceCollectionRepresentation;
import com.cumulocity.rest.representation.inventory.ManagedObjectRepresentation;
import com.nsn.cumulocity.model.builder.AbstractObjectBuilder;
import org.joda.time.DateTime;

public class ManagedObjectRepresentationBuilder extends AbstractObjectBuilder<ManagedObjectRepresentation> {

    Set dynamicProperties = new HashSet();

    public ManagedObjectRepresentationBuilder withName(final String value) {
        setFieldValue("name", value);
        return this;
    }

    public ManagedObjectRepresentationBuilder withID(final ID id) {
        setFieldValue("id", id);
        return this;
    }

    public ManagedObjectRepresentationBuilder withID(final IDBuilder id) {
        setFieldValueBuilder("id", id);
        return this;
    }

    public ManagedObjectRepresentationBuilder withType(final String type) {
        setFieldValue("type", type);
        return this;
    }

    @Deprecated
    public ManagedObjectRepresentationBuilder withLastUpdated(final Date lastUpdated) {
        setFieldValue("lastUpdated", lastUpdated);
        return this;
    }

    public ManagedObjectRepresentationBuilder with(final Object object) {
        dynamicProperties.add(object);
        return this;
    }

    @Override
    protected ManagedObjectRepresentation createDomainObject() {
        ManagedObjectRepresentation rep = new ManagedObjectRepresentation();
        for (Object object : dynamicProperties) {
            rep.set(object);
        }
        return rep;
    }

}
