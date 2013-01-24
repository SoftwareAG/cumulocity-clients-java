package com.cumulocity.me.rest.representation.inventory;

import com.cumulocity.me.lang.ArrayList;
import com.cumulocity.me.lang.List;
import com.cumulocity.me.rest.representation.BaseCollectionRepresentation;

public class ManagedObjectCollectionRepresentation extends BaseCollectionRepresentation {

    private List managedObjects;
    
    public ManagedObjectCollectionRepresentation() {
        managedObjects = new ArrayList();
    }
    
    public List getManagedObjects() {
        return managedObjects;
    }
    public void setManagedObjects(List managedObjects) {
        this.managedObjects = managedObjects;
    }
}
