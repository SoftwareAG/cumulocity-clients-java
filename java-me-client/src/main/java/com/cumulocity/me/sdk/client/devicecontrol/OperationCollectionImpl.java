/*
 * Copyright 2012 Nokia Siemens Networks 
 */
package com.cumulocity.me.sdk.client.devicecontrol;

import com.cumulocity.me.rest.representation.CumulocityMediaType;
import com.cumulocity.me.rest.representation.operation.DeviceControlMediaType;
import com.cumulocity.me.rest.representation.operation.OperationCollectionRepresentation;
import com.cumulocity.me.sdk.client.http.RestConnector;
import com.cumulocity.me.sdk.client.page.PagedCollectionResource;
import com.cumulocity.me.sdk.client.page.PagedCollectionResourceImpl;

public class OperationCollectionImpl extends PagedCollectionResourceImpl implements
        PagedCollectionResource {

    public OperationCollectionImpl(RestConnector restConnector, String url, int pageSize) {
        super(restConnector, url, pageSize);
    }
    
    public OperationCollectionImpl(RestConnector restConnector, String url) {
        super(restConnector, url);
    }

    protected CumulocityMediaType getMediaType() {
        return DeviceControlMediaType.OPERATION_COLLECTION;
    }

    protected Class getResponseClass() {
        return OperationCollectionRepresentation.class;
    }

}
