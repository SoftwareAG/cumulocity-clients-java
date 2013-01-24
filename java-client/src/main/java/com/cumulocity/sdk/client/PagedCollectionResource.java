/*
 * Copyright 2012 Nokia Siemens Networks 
 */
package com.cumulocity.sdk.client;

import com.cumulocity.rest.representation.BaseCollectionRepresentation;

public interface PagedCollectionResource<T extends BaseCollectionRepresentation> extends GenericResource<T> {

    /**
     * The method returns the specified page number.
     *
     * @param collectionRepresentation It uses the BaseCollectionRepresentation.getSelf() URL to find the collection.
     * @param pageNumber               - page number
     * @return BaseCollectionRepresentation type of BaseCollectionRepresentation.
     * @throws SDKException
     */
    public T getPage(BaseCollectionRepresentation collectionRepresentation, int pageNumber) throws SDKException;

    /**
     * The method returns the next page from the collection.
     *
     * @param collectionRepresentation It uses the BaseCollectionRepresentation.getNext() URL to find the collection.
     * @return collectionRepresentation type of BaseCollectionRepresentation.
     * @throws SDKException
     */
    public T getNextPage(BaseCollectionRepresentation collectionRepresentation) throws SDKException;

    /**
     * This method returns the previous page in the collection.
     *
     * @param collectionRepresentation - It uses the BaseCollectionRepresentation.getPrevious() URL to find the collection.
     * @return BaseCollectionRepresentation type of BaseCollectionRepresentation.
     * @throws SDKException
     */
    public T getPreviousPage(BaseCollectionRepresentation collectionRepresentation) throws SDKException;

}
