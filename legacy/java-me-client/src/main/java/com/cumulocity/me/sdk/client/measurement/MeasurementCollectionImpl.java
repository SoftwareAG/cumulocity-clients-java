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

package com.cumulocity.me.sdk.client.measurement;

import com.cumulocity.me.rest.representation.CumulocityMediaType;
import com.cumulocity.me.rest.representation.measurement.MeasurementCollectionRepresentation;
import com.cumulocity.me.rest.representation.measurement.MeasurementMediaType;
import com.cumulocity.me.sdk.client.http.RestConnector;
import com.cumulocity.me.sdk.client.page.PagedCollectionResource;
import com.cumulocity.me.sdk.client.page.PagedCollectionResourceImpl;

public class MeasurementCollectionImpl extends PagedCollectionResourceImpl implements PagedCollectionResource {

    public MeasurementCollectionImpl(RestConnector restConnector, String url, int pageSize) {
        super(restConnector, url, pageSize);
    }

    protected CumulocityMediaType getMediaType() {
        return MeasurementMediaType.MEASUREMENT_COLLECTION;
    }

    protected Class getResponseClass() {
        return MeasurementCollectionRepresentation.class;
    }
}
