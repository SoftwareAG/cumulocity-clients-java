package com.cumulocity.me.rest.representation.inventory;

import com.cumulocity.me.rest.representation.BaseCumulocityResourceRepresentation;

/**
 * A Java Representation for the MediaType Inventory, making a compromise between
 * https://startups.jira.com/wiki/display/MTM/Inventory and the example in 
 * https://startups.jira.com/wiki/display/MTM/Requests+to+Inventory
 */
public class InventoryRepresentation extends BaseCumulocityResourceRepresentation {

    /** The URL to get Managed Objects by type */
    private String managedObjectsForType;

    /** The URL to get Managed Objects by fragment type */
    private String managedObjectsForFragmentType;

    /** The URL to get Managed Objects for a given list of ids */
    private String managedObjectsForListOfIds;

    /** The Managed Objects reference */
    private ManagedObjectReferenceCollectionRepresentation managedObjects;

    /**
     * Default constructor is needed for reflection based class instantiation.
     */
    public InventoryRepresentation() {
    }

    /**
     * @return the managedObjectsForType
     */
    public final String getManagedObjectsForType() {
        return managedObjectsForType;
    }

    /**
     * @param managedObjectsForType the managedObjectsForType to set
     */
    public final void setManagedObjectsForType(String managedObjectsForType) {
        this.managedObjectsForType = managedObjectsForType;
    }

    /**
     * @return the managedObjectsForFragmentType
     */
    public final String getManagedObjectsForFragmentType() {
        return managedObjectsForFragmentType;
    }

    /**
     * @param managedObjectsForFragmentType the managedObjectsForFragmentType to set
     */
    public final void setManagedObjectsForFragmentType(String managedObjectsForFragmentType) {
        this.managedObjectsForFragmentType = managedObjectsForFragmentType;
    }

    /**
     * @return the managedObjects
     */
    public final ManagedObjectReferenceCollectionRepresentation getManagedObjects() {
        return managedObjects;
    }

    /**
     * @param managedObjects the managedObjects to set
     */
    public final void setManagedObjects(ManagedObjectReferenceCollectionRepresentation managedObjects) {
        this.managedObjects = managedObjects;
    }

    /**
     * @return managedObjectsForListOfIds
     */
    public String getManagedObjectsForListOfIds() {
        return managedObjectsForListOfIds;
    }

    /**
     * @param managedObjectsForListOfIds to be set
     */
    public void setManagedObjectsForListOfIds(String managedObjectsForListOfIds) {
        this.managedObjectsForListOfIds = managedObjectsForListOfIds;
    }

}
