/*
 * Copyright 2012 Nokia Siemens Networks 
 */
package com.cumulocity.me.sdk.client.inventory;

import com.cumulocity.me.model.idtype.GId;
import com.cumulocity.me.rest.representation.inventory.InventoryMediaType;
import com.cumulocity.me.rest.representation.inventory.ManagedObjectReferenceRepresentation;
import com.cumulocity.me.rest.representation.inventory.ManagedObjectRepresentation;
import com.cumulocity.me.sdk.SDKException;
import com.cumulocity.me.sdk.client.http.RestConnector;
import com.cumulocity.me.sdk.client.page.PagedCollectionResource;

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

	public ManagedObjectRepresentation get() {
		return (ManagedObjectRepresentation) restConnector.get(url, InventoryMediaType.MANAGED_OBJECT, ManagedObjectRepresentation.class);
	}

	public void delete() {
		restConnector.delete(url);
	}

	public ManagedObjectRepresentation update(ManagedObjectRepresentation managedObjectRepresentation) {
		return (ManagedObjectRepresentation) restConnector.put(url, InventoryMediaType.MANAGED_OBJECT, managedObjectRepresentation);

	}

	private String createChildDevicePath(ManagedObjectRepresentation managedObjectRepresentation) {
		if (managedObjectRepresentation == null || managedObjectRepresentation.getChildDevices() == null) {
			throw new SDKException("Unable to get the child device URL");
		}

		return managedObjectRepresentation.getChildDevices().getSelf();

	}

	public PagedCollectionResource getChildDevices() {
		ManagedObjectRepresentation managedObjectRepresentation = get();
		String self = createChildDevicePath(managedObjectRepresentation);
		return new ManagedObjectReferenceCollectionImpl(restConnector, self, pageSize);
	}

	public ManagedObjectReferenceRepresentation addChildDevice(ManagedObjectReferenceRepresentation refrenceRepresentation)
			throws SDKException {

		ManagedObjectRepresentation managedObjectRepresentation = get();
		return (ManagedObjectReferenceRepresentation) restConnector.post(this.createChildDevicePath(managedObjectRepresentation),
				InventoryMediaType.MANAGED_OBJECT_REFERENCE, refrenceRepresentation);
	}

	public ManagedObjectReferenceRepresentation getChildDevice(GId deviceId) {

		ManagedObjectRepresentation managedObjectRepresentation = get();
		String path = createChildDevicePath(managedObjectRepresentation) + "/" + deviceId.getValue();
		return (ManagedObjectReferenceRepresentation) restConnector.get(path, InventoryMediaType.MANAGED_OBJECT_REFERENCE,
				ManagedObjectReferenceRepresentation.class);
	}

	public void deleteChildDevice(GId deviceId) {
		ManagedObjectRepresentation managedObjectRepresentation = get();
		String path = createChildDevicePath(managedObjectRepresentation) + "/" + deviceId.getValue();
		restConnector.delete(path);
	}

	public ManagedObjectReferenceRepresentation addChildAssets(ManagedObjectReferenceRepresentation refrenceReprsentation)
			throws SDKException {
		ManagedObjectRepresentation managedObjectRepresentation = get();
		return (ManagedObjectReferenceRepresentation) restConnector.post(createChildAssetPath(managedObjectRepresentation),
				InventoryMediaType.MANAGED_OBJECT_REFERENCE, refrenceReprsentation);
	}

	private String createChildAssetPath(ManagedObjectRepresentation managedObjectRepresentation) {
		if (managedObjectRepresentation == null || managedObjectRepresentation.getChildAssets() == null) {
			throw new SDKException("Unable to get the child device URL");
		}
		return managedObjectRepresentation.getChildAssets().getSelf();
	}

	public PagedCollectionResource getChildAssets() {
		ManagedObjectRepresentation managedObjectRepresentation = get();
		String self = createChildAssetPath(managedObjectRepresentation);
		return new ManagedObjectReferenceCollectionImpl(restConnector, self, pageSize);
	}

	public ManagedObjectReferenceRepresentation getChildAsset(GId assetId) {
		ManagedObjectRepresentation managedObjectRepresentation = get();
		String path = createChildAssetPath(managedObjectRepresentation) + "/" + assetId.getValue();
		return (ManagedObjectReferenceRepresentation) restConnector.get(path, InventoryMediaType.MANAGED_OBJECT_REFERENCE,
				ManagedObjectReferenceRepresentation.class);
	}

	public void deleteChildAsset(GId assetId) {
		ManagedObjectRepresentation managedObjectRepresentation = get();
		String path = createChildAssetPath(managedObjectRepresentation) + "/" + assetId.getValue();
		restConnector.delete(path);
	}
}
