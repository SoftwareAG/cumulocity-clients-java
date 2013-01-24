package com.cumulocity.me.rest.representation;

import com.cumulocity.me.rest.representation.alarm.AlarmRepresentationBuilder;
import com.cumulocity.me.rest.representation.inventory.ManagedObjectReferenceRepresentationBuilder;
import com.cumulocity.me.rest.representation.inventory.ManagedObjectRepresentationBuilder;

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