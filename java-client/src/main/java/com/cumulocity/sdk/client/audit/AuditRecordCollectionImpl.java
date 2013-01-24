/*
 * Copyright 2012 Nokia Siemens Networks 
 */
package com.cumulocity.sdk.client.audit;

import com.cumulocity.rest.representation.CumulocityMediaType;
import com.cumulocity.rest.representation.audit.AuditMediaType;
import com.cumulocity.rest.representation.audit.AuditRecordCollectionRepresentation;
import com.cumulocity.sdk.client.PagedCollectionResource;
import com.cumulocity.sdk.client.PagedCollectionResourceImpl;
import com.cumulocity.sdk.client.RestConnector;

public class AuditRecordCollectionImpl extends PagedCollectionResourceImpl<AuditRecordCollectionRepresentation> implements
        PagedCollectionResource<AuditRecordCollectionRepresentation> {

    @Deprecated
    public AuditRecordCollectionImpl(RestConnector restConnector, String url) {
        super(restConnector, url);
    }

    public AuditRecordCollectionImpl(RestConnector restConnector, String url, int pageSize) {
        super(restConnector, url, pageSize);
    }

    @Override
    protected CumulocityMediaType getMediaType() {
        return AuditMediaType.AUDIT_RECORD_COLLECTION;
    }

    @Override
    protected Class<AuditRecordCollectionRepresentation> getResponseClass() {
        return AuditRecordCollectionRepresentation.class;
    }
}
