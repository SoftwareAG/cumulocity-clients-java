package com.cumulocity.me.rest.representation;

import com.cumulocity.me.rest.representation.inventory.ManagedObjectReferenceRepresentation;
import com.cumulocity.me.rest.representation.inventory.ManagedObjectReferenceRepresentationBuilder;

public enum SampleManagedObjectReferenceRepresentation {

    MO_REF_REPRESENTATION {
        @Override
        public ManagedObjectReferenceRepresentationBuilder builder() {
            return RestRepresentationObjectBuilder.aManagedObjectReferenceRepresentation().withSelf(SELF);
        }
    };

    public static final String SELF = "SELF_LINK";

    public abstract ManagedObjectReferenceRepresentationBuilder builder();

    public ManagedObjectReferenceRepresentation build() {
        return builder().build();
    }

}
