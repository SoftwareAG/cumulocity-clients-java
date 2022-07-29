package com.cumulocity.rest.representation.inventory;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.svenson.JSONProperty;
import org.svenson.JSONTypeHint;

import com.cumulocity.rest.representation.BaseCollectionRepresentation;

public class ManagedObjectCollectionRepresentation extends BaseCollectionRepresentation<ManagedObjectRepresentation> {

    private List<ManagedObjectRepresentation> managedObjects;
    
    public ManagedObjectCollectionRepresentation() {
        managedObjects = new ArrayList<>();
    }
    
    @JSONTypeHint(ManagedObjectRepresentation.class)
    public List<ManagedObjectRepresentation> getManagedObjects() {
        return managedObjects;
    }
    public void setManagedObjects(List<ManagedObjectRepresentation> managedObjects) {
        this.managedObjects = managedObjects;
    }

    @Override
    @JSONProperty(ignore = true)
    public Iterator<ManagedObjectRepresentation> iterator() {
        return managedObjects.iterator();
    }
}
