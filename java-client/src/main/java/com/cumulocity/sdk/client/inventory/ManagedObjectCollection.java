package com.cumulocity.sdk.client.inventory;

import com.cumulocity.rest.representation.inventory.ManagedObjectCollectionRepresentation;
import com.cumulocity.sdk.client.PagedCollectionResource;
import com.cumulocity.sdk.client.QueryParam;
import com.cumulocity.sdk.client.SDKException;

public interface ManagedObjectCollection extends PagedCollectionResource<ManagedObjectCollectionRepresentation> {
	
    /**
     * The method returns the first page. An InventoryParam can be included to the request.
	 */
	ManagedObjectCollectionRepresentation get(QueryParam... queryParams) throws SDKException;
}
