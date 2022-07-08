package com.cumulocity.rest.representation.builder;


/**
 * Domain object builder pattern.
 */
public class RestRepresentationObjectBuilder {

    public static ManagedObjectRepresentationBuilder aManagedObjectRepresentation() {
        return new ManagedObjectRepresentationBuilder();
    }

    public static ManagedObjectReferenceRepresentationBuilder aManagedObjectReferenceRepresentation() {
        return new ManagedObjectReferenceRepresentationBuilder();
    }

    public static AlarmRepresentationBuilder anAlarmRepresentation() {
        return new AlarmRepresentationBuilder();
    }

}
