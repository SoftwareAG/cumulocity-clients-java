/*
 * Copyright 2012 Nokia Siemens Networks 
 */
package com.cumulocity.sdk.client.inventory;

import com.cumulocity.model.idtype.GId;
import com.cumulocity.rest.representation.inventory.InventoryMediaType;
import com.cumulocity.rest.representation.inventory.ManagedObjectReferenceCollectionRepresentation;
import com.cumulocity.rest.representation.inventory.ManagedObjectReferenceRepresentation;
import com.cumulocity.rest.representation.inventory.ManagedObjectRepresentation;
import com.cumulocity.sdk.client.PagedCollectionResource;
import com.cumulocity.sdk.client.PlatformParameters;
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
    public ManagedObjectRepresentation get() throws SDKException {
        return restConnector.get(url, InventoryMediaType.MANAGED_OBJECT, ManagedObjectRepresentation.class);
    }

    @Override
    public void delete() throws SDKException {
        restConnector.delete(url);
    }

    @Override
    public ManagedObjectRepresentation update(ManagedObjectRepresentation managedObjectRepresentation) throws SDKException {
        return restConnector.put(url, InventoryMediaType.MANAGED_OBJECT, managedObjectRepresentation);

    }

    private String createChildDevicePath(ManagedObjectRepresentation managedObjectRepresentation) throws SDKException {
        if (managedObjectRepresentation == null || managedObjectRepresentation.getChildDevices() == null) {
            throw new SDKException("Unable to get the child device URL");
        }

        return managedObjectRepresentation.getChildDevices().getSelf();

    }

    @Deprecated
    @Override
    public ManagedObjectReferenceCollectionRepresentation getAllChildDevices() throws SDKException {
        ManagedObjectRepresentation managedObjectRepresentation = get();
        return restConnector.get(createChildDevicePath(managedObjectRepresentation)+PAGE_SIZE_PARAM_WITH_MAX_VALUE,
                InventoryMediaType.MANAGED_OBJECT_REFERENCE_COLLECTION, ManagedObjectReferenceCollectionRepresentation.class);
    }

    @Override
    public PagedCollectionResource<ManagedObjectReferenceCollectionRepresentation> getChildDevices() throws SDKException {
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
    @Deprecated
    public ManagedObjectReferenceCollectionRepresentation getAllChildAssets() throws SDKException {
        ManagedObjectRepresentation managedObjectRepresentation = get();
        return restConnector.get(createChildAssetPath(managedObjectRepresentation) + PAGE_SIZE_PARAM_WITH_MAX_VALUE,
                InventoryMediaType.MANAGED_OBJECT_REFERENCE_COLLECTION,ManagedObjectReferenceCollectionRepresentation.class);
    }

    @Override
    public PagedCollectionResource<ManagedObjectReferenceCollectionRepresentation> getChildAssets() throws SDKException {
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
