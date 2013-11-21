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

package com.cumulocity.sdk.client.identity;

import com.cumulocity.rest.representation.CumulocityMediaType;
import com.cumulocity.rest.representation.identity.ExternalIDCollectionRepresentation;
import com.cumulocity.rest.representation.identity.IdentityMediaType;
import com.cumulocity.sdk.client.PagedCollectionResource;
import com.cumulocity.sdk.client.PagedCollectionResourceImpl;
import com.cumulocity.sdk.client.RestConnector;

public class ExternalIDCollectionImpl extends PagedCollectionResourceImpl<ExternalIDCollectionRepresentation> implements
        PagedCollectionResource<ExternalIDCollectionRepresentation> {

    public ExternalIDCollectionImpl(RestConnector restConnector, String url, int pageSize) {
        super(restConnector, url, pageSize);
    }

    @Override
    protected CumulocityMediaType getMediaType() {
        return IdentityMediaType.EXTERNAL_ID_COLLECTION;
    }

    @Override
    protected Class<ExternalIDCollectionRepresentation> getResponseClass() {
        return ExternalIDCollectionRepresentation.class;
    }
}
