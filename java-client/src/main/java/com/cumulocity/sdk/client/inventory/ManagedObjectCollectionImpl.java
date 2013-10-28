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

package com.cumulocity.sdk.client.inventory;

import java.util.Map;

import com.cumulocity.rest.representation.CumulocityMediaType;
import com.cumulocity.rest.representation.inventory.InventoryMediaType;
import com.cumulocity.rest.representation.inventory.ManagedObjectCollectionRepresentation;
import com.cumulocity.sdk.client.PagedCollectionResourceImpl;
import com.cumulocity.sdk.client.RestConnector;
import com.cumulocity.sdk.client.SDKException;

public class ManagedObjectCollectionImpl extends PagedCollectionResourceImpl<ManagedObjectCollectionRepresentation> implements
	ManagedObjectCollection {
	
    @Deprecated
    public ManagedObjectCollectionImpl(RestConnector restConnector, String url) {
        super(restConnector, url);
    }

    public ManagedObjectCollectionImpl(RestConnector restConnector, String url, int pageSize) {
        super(restConnector, url, pageSize);
    }

    @Override
    protected CumulocityMediaType getMediaType() {
        return InventoryMediaType.MANAGED_OBJECT_COLLECTION;
    }

    @Override
    protected Class<ManagedObjectCollectionRepresentation> getResponseClass() {
        return ManagedObjectCollectionRepresentation.class;
    }

	@Override
	public ManagedObjectCollectionRepresentation getWithParents() throws SDKException {
		return getWithParents(pageSize);
	}

	@Override
	public ManagedObjectCollectionRepresentation getWithParents(int pageSize) throws SDKException {
		Map<String, String> params = prepareGetParams(pageSize);
		params.put(WITH_PARENTS_KEYS, String.valueOf(true));
		return get(params);
	}
    
    
}
