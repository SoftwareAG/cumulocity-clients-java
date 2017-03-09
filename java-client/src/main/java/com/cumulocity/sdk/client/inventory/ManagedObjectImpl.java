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

package com.cumulocity.sdk.client.inventory;

import com.cumulocity.model.idtype.GId;
import com.cumulocity.rest.representation.inventory.InventoryMediaType;
import com.cumulocity.rest.representation.inventory.ManagedObjectReferenceRepresentation;
import com.cumulocity.rest.representation.inventory.ManagedObjectRepresentation;
import com.cumulocity.sdk.client.RestConnector;
import com.cumulocity.sdk.client.SDKException;

public class ManagedObjectImpl implements ManagedObject {

    static final String PAGE_SIZE_PARAM_WITH_MAX_VALUE = "?pageSize=" + Short.MAX_VALUE;
    
    private final RestConnector restConnector;
    private final int pageSize;
    private volatile ManagedObjectRepresentation managedObject;

    final String url;

    public ManagedObjectImpl(RestConnector restConnector, String url, int pageSize) {
        this.restConnector = restConnector;
        this.url = url;
        this.pageSize = pageSize;
    }

    @Override
    @Deprecated
    public ManagedObjectRepresentation get() throws SDKException {
        return restConnector.get(url, InventoryMediaType.MANAGED_OBJECT, ManagedObjectRepresentation.class);
    }
    
    @Override
    @Deprecated
    public void delete() throws SDKException {
        restConnector.delete(url);
    }

    @Override
    @Deprecated
    public ManagedObjectRepresentation update(ManagedObjectRepresentation managedObjectRepresentation) throws SDKException {
        return restConnector.put(url, InventoryMediaType.MANAGED_OBJECT, managedObjectRepresentation);

    }

    private String createChildDevicePath() throws SDKException {
        ManagedObjectRepresentation managedObjectRepresentation = getInternal();
        if (managedObjectRepresentation == null || managedObjectRepresentation.getChildDevices() == null) {
            throw new SDKException("Unable to get the child device URL");
        }

        return managedObjectRepresentation.getChildDevices().getSelf();

    }

    @Override
    public ManagedObjectReferenceCollection getChildDevices() throws SDKException {
        String self = createChildDevicePath();
        return new ManagedObjectReferenceCollectionImpl(restConnector,self, pageSize);
    }

    @Override
    public ManagedObjectReferenceRepresentation addChildDevice(ManagedObjectReferenceRepresentation refrenceReprsentation)
            throws SDKException {
        return restConnector.post(this.createChildDevicePath(), InventoryMediaType.MANAGED_OBJECT_REFERENCE,
                refrenceReprsentation);
    }
    
    @Override
    public ManagedObjectReferenceRepresentation addChildDevice(GId childId)
            throws SDKException {
        return restConnector.post(this.createChildDevicePath(), InventoryMediaType.MANAGED_OBJECT_REFERENCE,
                createChildObject(childId));
    }

    @Override
    public ManagedObjectRepresentation addChildDevice(ManagedObjectRepresentation representation) throws SDKException {
        return restConnector.post(createChildDevicePath(), InventoryMediaType.MANAGED_OBJECT, representation);
    }

    @Override
    public ManagedObjectReferenceRepresentation getChildDevice(GId deviceId) throws SDKException {

        String path = createChildDevicePath() + "/" + deviceId.getValue();
        return restConnector.get(path, InventoryMediaType.MANAGED_OBJECT_REFERENCE, ManagedObjectReferenceRepresentation.class);
    }

    @Override
    public void deleteChildDevice(GId deviceId) throws SDKException {
        String path = createChildDevicePath() + "/" + deviceId.getValue();
        restConnector.delete(path);
    }

    @Override
    public ManagedObjectReferenceRepresentation addChildAssets(ManagedObjectReferenceRepresentation refrenceReprsentation)
            throws SDKException {
        return restConnector.post(createChildAssetPath(), InventoryMediaType.MANAGED_OBJECT_REFERENCE,
                refrenceReprsentation);
    }
    
    @Override
    public ManagedObjectReferenceRepresentation addChildAssets(GId childId)
            throws SDKException {
        return restConnector.post(createChildAssetPath(), InventoryMediaType.MANAGED_OBJECT_REFERENCE,
                createChildObject(childId));
    }

    @Override
    public ManagedObjectRepresentation addChildAsset(ManagedObjectRepresentation representation) throws SDKException {
        return restConnector.post(createChildAssetPath(), InventoryMediaType.MANAGED_OBJECT, representation);
    }

    private ManagedObjectReferenceRepresentation createChildObject(GId childId) {
        ManagedObjectReferenceRepresentation morr = new ManagedObjectReferenceRepresentation();
        ManagedObjectRepresentation mor = new ManagedObjectRepresentation();
        mor.setId(childId);
        morr.setManagedObject(mor);
        return morr;
    }

    private String createChildAssetPath() throws SDKException {
        ManagedObjectRepresentation managedObjectRepresentation = getInternal();
        if (managedObjectRepresentation == null || managedObjectRepresentation.getChildAssets() == null) {
            throw new SDKException("Unable to get the child device URL");
        }
        return managedObjectRepresentation.getChildAssets().getSelf();
    }

    @Override
    public ManagedObjectReferenceCollection getChildAssets() throws SDKException {
        String self = createChildAssetPath();
        return new ManagedObjectReferenceCollectionImpl(restConnector,self, pageSize);
    }

    @Override
    public ManagedObjectReferenceRepresentation getChildAsset(GId assetId) throws SDKException {
        String path = createChildAssetPath() + "/" + assetId.getValue();
        return restConnector.get(path, InventoryMediaType.MANAGED_OBJECT_REFERENCE, ManagedObjectReferenceRepresentation.class);
    }

    @Override
    public void deleteChildAsset(GId assetId) throws SDKException {
        String path = createChildAssetPath() + "/" + assetId.getValue();
        restConnector.delete(path);
    }

    @Override
    public ManagedObjectReferenceRepresentation addChildAdditions(ManagedObjectReferenceRepresentation refrenceReprsentation)
            throws SDKException {
        return restConnector.post(createChildAdditionPath(), InventoryMediaType.MANAGED_OBJECT_REFERENCE,
                refrenceReprsentation);
    }

    @Override
    public ManagedObjectReferenceRepresentation addChildAdditions(GId childId)
            throws SDKException {
        return restConnector.post(createChildAdditionPath(), InventoryMediaType.MANAGED_OBJECT_REFERENCE,
                createChildObject(childId));
    }

    @Override
    public ManagedObjectRepresentation addChildAddition(ManagedObjectRepresentation representation) throws SDKException {
        return restConnector.post(createChildAdditionPath(), InventoryMediaType.MANAGED_OBJECT, representation);
    }

    private String createChildAdditionPath() throws SDKException {
        ManagedObjectRepresentation managedObjectRepresentation = getInternal();
        if (managedObjectRepresentation == null || managedObjectRepresentation.getChildAdditions() == null) {
            throw new SDKException("Unable to get the child device URL");
        }
        return managedObjectRepresentation.getChildAdditions().getSelf();
    }

    @Override
    public ManagedObjectReferenceCollection getChildAdditions() throws SDKException {
        String self = createChildAdditionPath();
        return new ManagedObjectReferenceCollectionImpl(restConnector,self, pageSize);
    }

    @Override
    public ManagedObjectReferenceRepresentation getChildAddition(GId additionId) throws SDKException {
        String path = createChildAdditionPath() + "/" + additionId.getValue();
        return restConnector.get(path, InventoryMediaType.MANAGED_OBJECT_REFERENCE, ManagedObjectReferenceRepresentation.class);
    }

    @Override
    public void deleteChildAddition(GId additionId) throws SDKException {
        String path = createChildAdditionPath() + "/" + additionId.getValue();
        restConnector.delete(path);
    }

    private ManagedObjectRepresentation getInternal() throws SDKException {
        if (managedObject == null) {
            managedObject = restConnector.get(url, InventoryMediaType.MANAGED_OBJECT, ManagedObjectRepresentation.class);
        }
        return managedObject;
    }
}
