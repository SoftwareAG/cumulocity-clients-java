package com.cumulocity.me.rest.representation;

import com.cumulocity.me.rest.representation.alarm.AlarmRepresentationBuilder;
import com.cumulocity.me.rest.representation.inventory.ManagedObjectReferenceRepresentationBuilder;
import com.cumulocity.me.rest.representation.inventory.ManagedObjectRepresentationBuilder;
import com.cumulocity.me.rest.representation.SampleAlarmRepresentation;

public class RestRepresentationObjectMother {

    public static ManagedObjectRepresentationBuilder anMoRepresentationLike(
            final SampleManagedObjectRepresentation sampleManagedObjectRepresentation) {
        return sampleManagedObjectRepresentation.builder();
    }

    public static ManagedObjectReferenceRepresentationBuilder anMoRefRepresentationLike(
            final SampleManagedObjectReferenceRepresentation sample) {
        return sample.builder();
    }

    public static AlarmRepresentationBuilder anAlarmRepresentationLike(final SampleAlarmRepresentation sample) {
        return sample.builder();
    }

}
