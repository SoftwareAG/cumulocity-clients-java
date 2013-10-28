package com.cumulocity.sdk.client.inventory;

import com.cumulocity.rest.representation.inventory.ManagedObjectCollectionRepresentation;
import com.cumulocity.sdk.client.PagedCollectionResource;
import com.cumulocity.sdk.client.SDKException;

public interface ManagedObjectCollection extends PagedCollectionResource<ManagedObjectCollectionRepresentation> {
	
	String WITH_PARENTS_KEYS = "withParents";
	
    /**
     * The method returns the first page fetching parent managed objects.
     */
    ManagedObjectCollectionRepresentation getWithParents() throws SDKException;
    
    /**
     * The method returns the first page fetching parent managed objects.
     *
     * @param pageSize                 - page size
     * @return BaseCollectionRepresentation type of BaseCollectionRepresentation.
     * @throws SDKException
     */
    ManagedObjectCollectionRepresentation getWithParents(int pageSize) throws SDKException;

	
	

}
