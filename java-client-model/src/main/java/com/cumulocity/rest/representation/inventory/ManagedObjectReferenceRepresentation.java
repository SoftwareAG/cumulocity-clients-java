package com.cumulocity.rest.representation.inventory;

import com.cumulocity.rest.representation.AbstractExtensibleRepresentation;

public class ManagedObjectReferenceRepresentation extends AbstractExtensibleRepresentation {

    private ManagedObjectRepresentation managedObject;

    public ManagedObjectRepresentation getManagedObject() {
        return managedObject;
    }

    public void setManagedObject(ManagedObjectRepresentation managedObject) {
        this.managedObject = managedObject;
    }

}
