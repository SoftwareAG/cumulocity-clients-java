/*
 * Copyright 2012 Nokia Siemens Networks 
 */
package com.cumulocity.sdk.client.devicecontrol;

import com.cumulocity.rest.representation.CumulocityMediaType;
import com.cumulocity.rest.representation.operation.DeviceControlMediaType;
import com.cumulocity.rest.representation.operation.OperationCollectionRepresentation;
import com.cumulocity.sdk.client.PagedCollectionResource;
import com.cumulocity.sdk.client.PagedCollectionResourceImpl;
import com.cumulocity.sdk.client.RestConnector;

public class OperationCollectionImpl extends PagedCollectionResourceImpl<OperationCollectionRepresentation> implements
        PagedCollectionResource<OperationCollectionRepresentation> {

    @Deprecated
    public OperationCollectionImpl(RestConnector restConnector, String url) {
        super(restConnector, url);
    }

    public OperationCollectionImpl(RestConnector restConnector, String url, int pageSize) {
        super(restConnector, url, pageSize);
    }

    @Override
    protected CumulocityMediaType getMediaType() {
        return DeviceControlMediaType.OPERATION_COLLECTION;
    }

    @Override
    protected Class<OperationCollectionRepresentation> getResponseClass() {
        return OperationCollectionRepresentation.class;
    }

}
