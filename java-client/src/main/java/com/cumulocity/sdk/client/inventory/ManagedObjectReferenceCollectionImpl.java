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

import com.cumulocity.rest.representation.CumulocityMediaType;
import com.cumulocity.rest.representation.inventory.InventoryMediaType;
import com.cumulocity.rest.representation.inventory.ManagedObjectReferenceCollectionRepresentation;
import com.cumulocity.rest.representation.inventory.ManagedObjectReferenceRepresentation;
import com.cumulocity.sdk.client.PagedCollectionResourceImpl;
import com.cumulocity.sdk.client.RestConnector;

public class ManagedObjectReferenceCollectionImpl
        extends PagedCollectionResourceImpl<ManagedObjectReferenceRepresentation, ManagedObjectReferenceCollectionRepresentation, PagedManagedObjectReferenceCollectionRepresentation>
        implements ManagedObjectReferenceCollection {

    public ManagedObjectReferenceCollectionImpl(RestConnector restConnector, String url, int pageSize) {
        super(restConnector, url, pageSize);
    }

    @Override
    protected CumulocityMediaType getMediaType() {
        return InventoryMediaType.MANAGED_OBJECT_REFERENCE_COLLECTION;
    }

    @Override
    protected Class<ManagedObjectReferenceCollectionRepresentation> getResponseClass() {
        return ManagedObjectReferenceCollectionRepresentation.class;
    }

    @Override
    protected PagedManagedObjectReferenceCollectionRepresentation wrap(ManagedObjectReferenceCollectionRepresentation collection) {
        return new PagedManagedObjectReferenceCollectionRepresentation(collection, this);
    }
}
