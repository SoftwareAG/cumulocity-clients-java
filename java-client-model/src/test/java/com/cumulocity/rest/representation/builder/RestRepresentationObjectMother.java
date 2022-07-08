package com.cumulocity.rest.representation.builder;

public class RestRepresentationObjectMother {

    public static ManagedObjectRepresentationBuilder anMoRepresentationLike(
            final SampleManagedObjectRepresentation sampleManagedObjectRepresentation) {
        return sampleManagedObjectRepresentation.builder();
    }

    public static ManagedObjectReferenceRepresentationBuilder anMoRefRepresentationLike(
            final SampleManagedObjectReferenceRepresentation sample) {
        return sample.builder();
    }

    public static AlarmRepresentationBuilder anAlarmRepresentationLike(
            final SampleAlarmRepresentation sample) {
        return sample.builder();
    }

}
