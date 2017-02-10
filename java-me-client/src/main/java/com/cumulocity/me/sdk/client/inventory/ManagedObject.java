/*
 * Copyright (C) 2013 Cumulocity GmbH
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of 
 * this software and associated documentation files (the "Software"),
 * to deal in the Software without restriction, including without limitation the rights to use,
 * copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software,
 * and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
 * OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE,
 * ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
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

    /**
     * Adds a child addition to the ManagedObject.
     * @param refrenceReprsentation
     * @return ManagedObjectReferenceRepresentation with the id of th child addition.
     * @throws SDKException
     */
    ManagedObjectReferenceRepresentation addChildAdditions(ManagedObjectReferenceRepresentation refrenceReprsentation);

    /**
     * Returns all the child additions for the Managed Object in paged collection form
     * @return ManagedObjectReferenceCollectionRepresentation which contains all the child additions.
     * @throws SDKException
     */
    PagedCollectionResource getChildAdditions();

    /**
     * Returns the child addition with the given id. If it belongs to the ManagedObject.
     * @param additionId
     * @return ManagedObjectReferenceRepresentation of the child addition.
     * @throws SDKException
     */
    ManagedObjectReferenceRepresentation getChildAddition(GId additionId);

    /**
     * Deletes the child addition and its relation to the managed object.
     * @param additionId
     * @throws SDKException
     */
    void deleteChildAddition(GId additionId);
}
