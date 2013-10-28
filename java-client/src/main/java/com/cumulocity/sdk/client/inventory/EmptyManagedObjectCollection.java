package com.cumulocity.sdk.client.inventory;

import com.cumulocity.rest.representation.BaseCollectionRepresentation;
import com.cumulocity.rest.representation.inventory.ManagedObjectCollectionRepresentation;
import com.cumulocity.sdk.client.EmptyPagedCollectionResource;
import com.cumulocity.sdk.client.SDKException;

public class EmptyManagedObjectCollection implements ManagedObjectCollection {
	
	private final EmptyPagedCollectionResource<ManagedObjectCollectionRepresentation> target;

	public EmptyManagedObjectCollection() {
		target = new EmptyPagedCollectionResource<ManagedObjectCollectionRepresentation>(ManagedObjectCollectionRepresentation.class);
	}

	@Override
	public ManagedObjectCollectionRepresentation get() throws SDKException {
		return target.get();
	}

	@Override
	public ManagedObjectCollectionRepresentation get(int pageSize) throws SDKException {
		return target.get(pageSize);
	}
	
	@Override
	public ManagedObjectCollectionRepresentation getWithParents() throws SDKException {
		return get();
	}

	@Override
	public ManagedObjectCollectionRepresentation getWithParents(int pageSize) throws SDKException {
		return get(pageSize);
	}

	@Override
	public ManagedObjectCollectionRepresentation getPage(BaseCollectionRepresentation collectionRepresentation, int pageNumber) throws SDKException {
		return target.getPage(collectionRepresentation, pageNumber);
	}

	@Override
	public ManagedObjectCollectionRepresentation getPage(BaseCollectionRepresentation collectionRepresentation, int pageNumber, int pageSize) throws SDKException {
		return target.getPage(collectionRepresentation, pageNumber, pageSize);
	}

	@Override
	public ManagedObjectCollectionRepresentation getNextPage(BaseCollectionRepresentation collectionRepresentation) throws SDKException {
		return target.getNextPage(collectionRepresentation);
	}

	@Override
	public ManagedObjectCollectionRepresentation getPreviousPage(BaseCollectionRepresentation collectionRepresentation) throws SDKException {
		return target.getPreviousPage(collectionRepresentation);
	}

}
