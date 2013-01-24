package com.cumulocity.me.rest.representation.inventory;

import com.cumulocity.me.rest.representation.BaseCumulocityResourceRepresentation;

public class ManagedObjectReferenceRepresentation extends BaseCumulocityResourceRepresentation {

    private ManagedObjectRepresentation managedObject;
    
    public ManagedObjectRepresentation getManagedObject() {
        return managedObject;
    }
    public void setManagedObject(ManagedObjectRepresentation managedObject) {
        this.managedObject = managedObject;
    }

}
