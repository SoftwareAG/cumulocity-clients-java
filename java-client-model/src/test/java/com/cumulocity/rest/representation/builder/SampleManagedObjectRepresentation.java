package com.cumulocity.rest.representation.builder;

import com.cumulocity.rest.representation.inventory.ManagedObjectRepresentation;

public enum SampleManagedObjectRepresentation {

    MO_REPRESENTATION {
        @Override
        public ManagedObjectRepresentationBuilder builder() {
            //@formatter:off
            return RestRepresentationObjectBuilder.aManagedObjectRepresentation()
                    .withName(MANAGED_OBJECT_NAME)
                    .withType(MANAGED_OBJECT_TYPE);
            //@formatter:on
        }
    };

    public static final String MANAGED_OBJECT_NAME = "ManagedObject Name #";

    public static final String MANAGED_OBJECT_TYPE = "ManagedObject Type #";
    
    public static final String MANAGED_OBJECT_OWNER = "ManagedObject Owner #";

    public abstract ManagedObjectRepresentationBuilder builder();

    public ManagedObjectRepresentation build() {
        return builder().build();
    }

}
