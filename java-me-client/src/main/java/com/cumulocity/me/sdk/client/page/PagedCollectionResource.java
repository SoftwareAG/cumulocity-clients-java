/*
 * Copyright 2012 Nokia Siemens Networks 
 */
package com.cumulocity.me.sdk.client.page;

import com.cumulocity.me.rest.representation.BaseCollectionRepresentation;
import com.cumulocity.me.sdk.SDKException;
import com.cumulocity.me.sdk.client.GenericResource;

public interface PagedCollectionResource extends GenericResource {

    int DEFAULT_PAGE_SIZE = 5;

    /**
     * The method returns the specified page number.
     *
     * @param collectionRepresentation It uses the BaseCollectionRepresentation.getSelf() URL to find the collection.
     * @param pageNumber               - page number
     * @return BaseCollectionRepresentation type of BaseCollectionRepresentation.
     * @throws SDKException
     */
    Object getPage(BaseCollectionRepresentation collectionRepresentation, int pageNumber);

    /**
     * The method returns the next page from the collection.
     *
     * @param collectionRepresentation It uses the BaseCollectionRepresentation.getNext() URL to find the collection.
     * @return collectionRepresentation type of BaseCollectionRepresentation.
     * @throws SDKException
     */
    Object getNextPage(BaseCollectionRepresentation collectionRepresentation);

    /**
     * This method returns the previous page in the collection.
     *
     * @param collectionRepresentation - It uses the BaseCollectionRepresentation.getPrevious() URL to find the collection.
     * @return BaseCollectionRepresentation type of BaseCollectionRepresentation.
     * @throws SDKException
     */
    Object getPreviousPage(BaseCollectionRepresentation collectionRepresentation);
}
