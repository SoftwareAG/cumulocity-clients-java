package com.cumulocity.me.rest.representation;

import com.cumulocity.me.rest.representation.inventory.ManagedObjectRepresentation;
import com.cumulocity.me.rest.representation.inventory.ManagedObjectRepresentationBuilder;

public enum SampleManagedObjectRepresentation {

    MO_REPRESENTATION {
        @Override
        public ManagedObjectRepresentationBuilder builder() {
            //@formatter:off
            return RestRepresentationObjectBuilder.aManagedObjectRepresentation().withName(MANAGED_OBJECT_NAME).withType(
                    MANAGED_OBJECT_TYPE);
            //@formatter:on
        }
    };

    public static final String MANAGED_OBJECT_NAME = "ManagedObject Name #";

    public static final String MANAGED_OBJECT_TYPE = "ManagedObject Type #";

    public abstract ManagedObjectRepresentationBuilder builder();

    public ManagedObjectRepresentation build() {
        return builder().build();
    }

}
