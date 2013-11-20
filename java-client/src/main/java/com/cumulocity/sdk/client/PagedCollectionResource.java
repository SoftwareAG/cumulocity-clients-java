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

package com.cumulocity.sdk.client;

import com.cumulocity.rest.representation.BaseCollectionRepresentation;

public interface PagedCollectionResource<T extends BaseCollectionRepresentation> extends Iterable<T> {

    String PAGE_SIZE_KEY = "pageSize";

    String PAGE_NUMBER_KEY = "currentPage";


    /**
     * The method returns the first page.
     */
    T get(QueryParam... queryParams) throws SDKException;

    /**
     * The method returns the first page.
     *
     * @param pageSize                 - page size
     * @return BaseCollectionRepresentation type of BaseCollectionRepresentation.
     * @throws SDKException
     */
    T get(int pageSize, QueryParam... queryParams) throws SDKException;

    /**
     * The method returns the specified page number.
     *
     * @param collectionRepresentation It uses the BaseCollectionRepresentation.getSelf() URL to find the collection.
     * @param pageNumber               - page number
     * @return BaseCollectionRepresentation type of BaseCollectionRepresentation.
     * @throws SDKException
     */
    T getPage(BaseCollectionRepresentation collectionRepresentation, int pageNumber) throws SDKException;

    /**
     * The method returns the specified page number.
     *
     * @param collectionRepresentation It uses the BaseCollectionRepresentation.getSelf() URL to find the collection.
     * @param pageNumber               - page number
     * @param pageSize                 - page size
     * @return BaseCollectionRepresentation type of BaseCollectionRepresentation.
     * @throws SDKException
     */
    T getPage(BaseCollectionRepresentation collectionRepresentation, int pageNumber, int pageSize) throws SDKException;

    /**
     * The method returns the next page from the collection.
     *
     * @param collectionRepresentation It uses the BaseCollectionRepresentation.getNext() URL to find the collection.
     * @return collectionRepresentation type of BaseCollectionRepresentation.
     * @throws SDKException
     */
    T getNextPage(BaseCollectionRepresentation collectionRepresentation) throws SDKException;

    /**
     * This method returns the previous page in the collection.
     *
     * @param collectionRepresentation - It uses the BaseCollectionRepresentation.getPrevious() URL to find the collection.
     * @return BaseCollectionRepresentation type of BaseCollectionRepresentation.
     * @throws SDKException
     */
    T getPreviousPage(BaseCollectionRepresentation collectionRepresentation) throws SDKException;

}
