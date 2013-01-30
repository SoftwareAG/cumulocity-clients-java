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
