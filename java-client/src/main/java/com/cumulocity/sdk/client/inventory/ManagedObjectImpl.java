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

    private String createChildDevicePath(ManagedObjectRepresentation managedObjectRepresentation) throws SDKException {
        if (managedObjectRepresentation == null || managedObjectRepresentation.getChildDevices() == null) {
            throw new SDKException("Unable to get the child device URL");
        }

        return managedObjectRepresentation.getChildDevices().getSelf();

    }

    @Override
    public ManagedObjectReferenceCollection getChildDevices() throws SDKException {
        ManagedObjectRepresentation managedObjectRepresentation = get();
        String self = createChildDevicePath(managedObjectRepresentation);
        return new ManagedObjectReferenceCollectionImpl(restConnector,self, pageSize);
    }

    @Override
    public ManagedObjectReferenceRepresentation addChildDevice(ManagedObjectReferenceRepresentation refrenceReprsentation)
            throws SDKException {

        ManagedObjectRepresentation managedObjectRepresentation = get();
        return restConnector.post(this.createChildDevicePath(managedObjectRepresentation), InventoryMediaType.MANAGED_OBJECT_REFERENCE,
                refrenceReprsentation);
    }

    @Override
    public ManagedObjectReferenceRepresentation getChildDevice(GId deviceId) throws SDKException {

        ManagedObjectRepresentation managedObjectRepresentation = get();
        String path = createChildDevicePath(managedObjectRepresentation) + "/" + deviceId.getValue();
        return restConnector.get(path, InventoryMediaType.MANAGED_OBJECT_REFERENCE, ManagedObjectReferenceRepresentation.class);
    }

    @Override
    public void deleteChildDevice(GId deviceId) throws SDKException {
        ManagedObjectRepresentation managedObjectRepresentation = get();
        String path = createChildDevicePath(managedObjectRepresentation) + "/" + deviceId.getValue();
        restConnector.delete(path);
    }

    @Override
    public ManagedObjectReferenceRepresentation addChildAssets(ManagedObjectReferenceRepresentation refrenceReprsentation)
            throws SDKException {
        ManagedObjectRepresentation managedObjectRepresentation = get();
        return restConnector.post(createChildAssetPath(managedObjectRepresentation), InventoryMediaType.MANAGED_OBJECT_REFERENCE,
                refrenceReprsentation);
    }

    private String createChildAssetPath(ManagedObjectRepresentation managedObjectRepresentation) throws SDKException {
        if (managedObjectRepresentation == null || managedObjectRepresentation.getChildAssets() == null) {
            throw new SDKException("Unable to get the child device URL");
        }
        return managedObjectRepresentation.getChildAssets().getSelf();
    }

    @Override
    public ManagedObjectReferenceCollection getChildAssets() throws SDKException {
        ManagedObjectRepresentation managedObjectRepresentation = get();
        String self = createChildAssetPath(managedObjectRepresentation);
        return new ManagedObjectReferenceCollectionImpl(restConnector,self, pageSize);
    }

    @Override
    public ManagedObjectReferenceRepresentation getChildAsset(GId assetId) throws SDKException {
        ManagedObjectRepresentation managedObjectRepresentation = get();
        String path = createChildAssetPath(managedObjectRepresentation) + "/" + assetId.getValue();
        return restConnector.get(path, InventoryMediaType.MANAGED_OBJECT_REFERENCE, ManagedObjectReferenceRepresentation.class);
    }

    @Override
    public void deleteChildAsset(GId assetId) throws SDKException {
        ManagedObjectRepresentation managedObjectRepresentation = get();
        String path = createChildAssetPath(managedObjectRepresentation) + "/" + assetId.getValue();
        restConnector.delete(path);
    }
}
