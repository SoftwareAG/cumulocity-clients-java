/*
 * Copyright 2012 Nokia Siemens Networks 
 */
package com.cumulocity.me.sdk.client.inventory;

import com.cumulocity.me.model.idtype.GId;
import com.cumulocity.me.rest.representation.inventory.ManagedObjectReferenceRepresentation;
import com.cumulocity.me.rest.representation.inventory.ManagedObjectRepresentation;
import com.cumulocity.me.sdk.SDKException;
import com.cumulocity.me.sdk.client.page.PagedCollectionResource;

/**
 * Java Interface to call the Cumulocity Inventory Rest API.
 */
public interface ManagedObject {

    /**
     * Returns the Managed Object of the Resource.
     * @return ManagedObjectRepresentation
     * @throws SDKException
     */
    ManagedObjectRepresentation get();

    /**
     * Deletes the Managed Object from the Cumulocity Server.
     * @throws SDKException
     */
    void delete();

    /**
     * This update the ManagedObject for the operationCollection. Cannot update the ID.
     * @param managedObjectRepresentation
     * @return ManagedObjectRepresentation updated ManagedObject.
     * @throws SDKException
     */
    ManagedObjectRepresentation update(ManagedObjectRepresentation managedObjectRepresentation);

    /**
     * Adds a child device to the ManagedObject.
     * @param refrenceReprsentation
     * @return ManagedObjectReferenceRepresentation with the id of th child device.
     * @throws SDKException
     */
    ManagedObjectReferenceRepresentation addChildDevice(ManagedObjectReferenceRepresentation refrenceReprsentation);


    /**
     * Returns all the child Devices for the Managed Object in paged collection form.
     * @return ManagedObjectReferenceCollectionRepresentation which contains all the child devices.
     * @throws SDKException
     */
    PagedCollectionResource getChildDevices();

    /**
     * Returns the child device with the given id. If it belongs to the ManagedObject.
     * @param deviceId
     * @return ManagedObjectReferenceRepresentation of the child device.
     * @throws SDKException
     */
    ManagedObjectReferenceRepresentation getChildDevice(GId deviceId);

    /**
     * Deletes the child device  and its relation to the managed object.
     * @param deviceId
     * @throws SDKException
     */
    void deleteChildDevice(GId deviceId);

    /**
     * Adds a child device to the ManagedObject.
     * @param refrenceReprsentation
     * @return ManagedObjectReferenceRepresentation with the id of th child device.
     * @throws SDKException
     */
    ManagedObjectReferenceRepresentation addChildAssets(ManagedObjectReferenceRepresentation refrenceReprsentation);

    /**
     * Returns all the child Assets for the Managed Object  in paged collection form
     * @return ManagedObjectReferenceCollectionRepresentation which contains all the child devices.
     * @throws SDKException
     */
    PagedCollectionResource getChildAssets();

    /**
     * Returns the child Asset with the given id. If it belongs to the ManagedObject.
     * @param assetId
     * @return ManagedObjectReferenceRepresentation of the child device.
     * @throws SDKException
     */
    ManagedObjectReferenceRepresentation getChildAsset(GId assetId);

    /**
     * Deletes the child Asset  and its relation to the managed object.
     * @param assetId
     * @throws SDKException
     */
    void deleteChildAsset(GId assetId);
}
