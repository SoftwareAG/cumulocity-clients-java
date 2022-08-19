package com.cumulocity.rest.representation.builder;

import com.cumulocity.rest.representation.inventory.ManagedObjectReferenceRepresentation;

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
