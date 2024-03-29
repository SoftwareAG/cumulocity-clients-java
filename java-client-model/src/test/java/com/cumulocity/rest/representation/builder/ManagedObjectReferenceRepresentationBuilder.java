package com.cumulocity.rest.representation.builder;

import com.cumulocity.rest.representation.inventory.ManagedObjectReferenceRepresentation;
import com.cumulocity.rest.representation.inventory.ManagedObjectRepresentation;

public class ManagedObjectReferenceRepresentationBuilder {

    private ManagedObjectRepresentation mo;

    private String self;

    public ManagedObjectReferenceRepresentationBuilder withMo(ManagedObjectRepresentation mo) {
        this.mo = mo;
        return this;
    }

    public ManagedObjectReferenceRepresentationBuilder withSelf(String self) {
        this.self = self;
        return this;
    }

    public ManagedObjectReferenceRepresentation build() {
        ManagedObjectReferenceRepresentation ref = new ManagedObjectReferenceRepresentation();
        ref.setManagedObject(mo);
        ref.setSelf(self);
        return ref;
    }
}
