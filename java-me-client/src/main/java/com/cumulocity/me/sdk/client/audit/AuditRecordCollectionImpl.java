/*
 * Copyright 2012 Nokia Siemens Networks 
 */
package com.cumulocity.me.sdk.client.audit;

import com.cumulocity.me.rest.representation.CumulocityMediaType;
import com.cumulocity.me.rest.representation.audit.AuditMediaType;
import com.cumulocity.me.rest.representation.audit.AuditRecordCollectionRepresentation;
import com.cumulocity.me.sdk.client.http.RestConnector;
import com.cumulocity.me.sdk.client.page.PagedCollectionResource;
import com.cumulocity.me.sdk.client.page.PagedCollectionResourceImpl;

public class AuditRecordCollectionImpl extends PagedCollectionResourceImpl implements
        PagedCollectionResource {

    public AuditRecordCollectionImpl(RestConnector restConnector, String url, int pageSize) {
        super(restConnector, url, pageSize);
    }

    protected CumulocityMediaType getMediaType() {
        return AuditMediaType.AUDIT_RECORD_COLLECTION;
    }

    protected Class getResponseClass() {
        return AuditRecordCollectionRepresentation.class;
    }
}
