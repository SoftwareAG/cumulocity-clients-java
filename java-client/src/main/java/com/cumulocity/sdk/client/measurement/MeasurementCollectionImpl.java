/*
 * Copyright 2012 Nokia Siemens Networks 
 */
package com.cumulocity.sdk.client.measurement;

import com.cumulocity.rest.representation.CumulocityMediaType;
import com.cumulocity.rest.representation.measurement.MeasurementCollectionRepresentation;
import com.cumulocity.rest.representation.measurement.MeasurementMediaType;
import com.cumulocity.sdk.client.PagedCollectionResource;
import com.cumulocity.sdk.client.PagedCollectionResourceImpl;
import com.cumulocity.sdk.client.RestConnector;

public class MeasurementCollectionImpl extends PagedCollectionResourceImpl<MeasurementCollectionRepresentation> implements
        PagedCollectionResource<MeasurementCollectionRepresentation> {

    @Deprecated
    public MeasurementCollectionImpl(RestConnector restConnector, String url) {
        super(restConnector, url);
    }

    public MeasurementCollectionImpl(RestConnector restConnector, String url, int pageSize) {
        super(restConnector, url, pageSize);
    }

    @Override
    protected CumulocityMediaType getMediaType() {
        return MeasurementMediaType.MEASUREMENT_COLLECTION;
    }

    @Override
    protected Class<MeasurementCollectionRepresentation> getResponseClass() {
        return MeasurementCollectionRepresentation.class;
    }
}
