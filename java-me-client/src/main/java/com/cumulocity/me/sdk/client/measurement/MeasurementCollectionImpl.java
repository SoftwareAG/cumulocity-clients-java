/*
 * Copyright 2012 Nokia Siemens Networks 
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
