package com.cumulocity.me.rest.representation.inventory;

import com.cumulocity.me.rest.representation.inventory.InventoryRepresentation;
import com.cumulocity.me.rest.representation.inventory.ManagedObjectReferenceCollectionRepresentation;

public class InventoryRepresentationBuilder {

    public static final InventoryRepresentationBuilder anInventoryRepresentation(String baseUrl) {
        return new InventoryRepresentationBuilder(baseUrl);
    }
    
    String baseUrl;
    ManagedObjectReferenceCollectionRepresentation managedObjects;
    
    public InventoryRepresentationBuilder(String baseUrl) {
        this.baseUrl = baseUrl;
    }
    
    public InventoryRepresentationBuilder withManagedObjects() {
        managedObjects = new ManagedObjectReferenceCollectionRepresentation();
        managedObjects.setSelf(baseUrl + "/managedObjects");
        return this;
    }
    
    public InventoryRepresentation build() {
        InventoryRepresentation representation = new InventoryRepresentation();
        representation.setManagedObjects(managedObjects);
        return representation;
    }
}
