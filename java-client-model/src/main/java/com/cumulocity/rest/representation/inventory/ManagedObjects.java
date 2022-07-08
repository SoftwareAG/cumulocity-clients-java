package com.cumulocity.rest.representation.inventory;

import com.cumulocity.model.idtype.GId;

public class ManagedObjects {
    
    public static ManagedObjectRepresentation asManagedObject(GId id) {
        final ManagedObjectRepresentation managedObjectRepresentation = new ManagedObjectRepresentation();
        managedObjectRepresentation.setId(id);
        return managedObjectRepresentation;
    }


}
