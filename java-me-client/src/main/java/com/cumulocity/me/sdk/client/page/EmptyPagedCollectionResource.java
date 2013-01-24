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
import com.cumulocity.me.rest.representation.CumulocityResourceRepresentation;
import com.cumulocity.me.rest.representation.PageStatisticsRepresentation;
import com.cumulocity.me.sdk.SDKException;

public class EmptyPagedCollectionResource implements PagedCollectionResource {

    private final Class type;

    public EmptyPagedCollectionResource(Class type) {
        this.type = type;
    }

    public CumulocityResourceRepresentation get() {
    	BaseCollectionRepresentation collectionRepresentaton = (BaseCollectionRepresentation) newCollectionRepresentationInstance();
    	collectionRepresentaton.setPageStatistics(new PageStatisticsRepresentation());
        return collectionRepresentaton;
    }

    public Object getPage(BaseCollectionRepresentation collectionRepresentation, int pageNumber) {
        return pageNumber == 1 ? get() : null;
    }

    public Object getNextPage(BaseCollectionRepresentation collectionRepresentation) {
        return null;
    }

    public Object getPreviousPage(BaseCollectionRepresentation collectionRepresentation) {
        return null;
    }

    private Object newCollectionRepresentationInstance() {
        try {
            return type.newInstance();
        } catch (Exception ex) {
            throw new SDKException("internal error", ex);
        }
    }
}
