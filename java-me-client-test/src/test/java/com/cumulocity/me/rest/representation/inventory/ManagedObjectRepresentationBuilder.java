package com.cumulocity.me.rest.representation.inventory;

import java.util.Date;

import com.cumulocity.me.rest.representation.BaseRepresentationBuilder;
import com.cumulocity.model.builder.IDBuilder;

public class ManagedObjectRepresentationBuilder extends BaseRepresentationBuilder<ManagedObjectRepresentation, ManagedObjectRepresentationBuilder> {

    public ManagedObjectRepresentationBuilder withName(final String value) {
        setObjectField("name", value);
        return this;
    }

    public ManagedObjectRepresentationBuilder withID(final IDBuilder id) {
        setObjectFieldBuilder("id", id);
        return this;
    }

    public ManagedObjectRepresentationBuilder withType(final String type) {
        setObjectField("type", type);
        return this;
    }

    public ManagedObjectRepresentationBuilder withChildAssets(final ManagedObjectReferenceCollectionRepresentation childAssets) {
        setObjectField("childAssets", childAssets);
        return this;
    }

    public ManagedObjectRepresentationBuilder withChildDevices(final ManagedObjectReferenceCollectionRepresentation childDevices) {
        setObjectField("childDevices", childDevices);
        return this;
    }

    public ManagedObjectRepresentationBuilder withParents(final ManagedObjectReferenceCollectionRepresentation parents) {
        setObjectField("parents", parents);
        return this;
    }

    public ManagedObjectRepresentationBuilder withLastUpdated(final Date lastUpdated) {
        setObjectField("lastUpdated", lastUpdated);
        return this;
    }

    @Override
    protected ManagedObjectRepresentation createDomainObject() {
        return new ManagedObjectRepresentation();
    }
}
