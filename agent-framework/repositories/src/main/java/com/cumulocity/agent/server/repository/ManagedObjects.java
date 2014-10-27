package com.cumulocity.agent.server.repository;

import com.cumulocity.model.idtype.GId;
import com.cumulocity.rest.representation.inventory.ManagedObjectReferenceRepresentation;
import com.cumulocity.rest.representation.inventory.ManagedObjectRepresentation;
import com.google.common.base.Function;
import com.google.common.base.Predicate;

public class ManagedObjects {

    public static ManagedObjectRepresentation asManagedObject(GId id) {
        final ManagedObjectRepresentation managedObjectRepresentation = new ManagedObjectRepresentation();
        managedObjectRepresentation.setId(id);
        return managedObjectRepresentation;
    }

    public static Function<ManagedObjectRepresentation, GId> getId() {
        return new Function<ManagedObjectRepresentation, GId>() {
            @Override
            public GId apply(ManagedObjectRepresentation input) {
                return input.getId();
            }
        };
    }

    public static Function<ManagedObjectReferenceRepresentation, ManagedObjectRepresentation> fromManagedObjectReference() {
        return new Function<ManagedObjectReferenceRepresentation, ManagedObjectRepresentation>() {
            @Override
            public ManagedObjectRepresentation apply(ManagedObjectReferenceRepresentation input) {
                return input.getManagedObject();
            }
        };
    }

    public static Predicate<ManagedObjectRepresentation> nameEquals(final String name) {
        return new Predicate<ManagedObjectRepresentation>() {
            @Override
            public boolean apply(ManagedObjectRepresentation input) {
                return name.equals(input.getName());
            }
        };
    }
}
