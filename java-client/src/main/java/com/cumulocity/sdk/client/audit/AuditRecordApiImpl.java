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

package com.cumulocity.sdk.client.audit;

import java.util.Map;

import com.cumulocity.model.idtype.GId;
import com.cumulocity.rest.representation.audit.AuditMediaType;
import com.cumulocity.rest.representation.audit.AuditRecordRepresentation;
import com.cumulocity.rest.representation.audit.AuditRecordsRepresentation;
import com.cumulocity.sdk.client.RestConnector;
import com.cumulocity.sdk.client.SDKException;
import com.cumulocity.sdk.client.UrlProcessor;

public class AuditRecordApiImpl implements AuditRecordApi {

    private final RestConnector restConnector;

    private final int pageSize;

    private AuditRecordsRepresentation auditRecordsRepresentation;
    
    private UrlProcessor urlProcessor;

    public AuditRecordApiImpl(RestConnector restConnector, UrlProcessor urlProcessor, AuditRecordsRepresentation auditRecordsRepresentation, int pageSize) {
        this.restConnector = restConnector;
        this.urlProcessor = urlProcessor;
        this.auditRecordsRepresentation = auditRecordsRepresentation;
        this.pageSize = pageSize;
    }

    private AuditRecordsRepresentation getAuditRecordsRepresentation() throws SDKException {
        return auditRecordsRepresentation;
    }
    
    @Override
    public AuditRecordRepresentation getAuditRecord(GId gid) throws SDKException {
        String url = getSelfUri() + "/" + gid.getValue();
        return restConnector.get(url, AuditMediaType.AUDIT_RECORD, AuditRecordRepresentation.class);
    }

    private String getSelfUri() throws SDKException {
        return getAuditRecordsRepresentation().getAuditRecords().getSelf();
    }

    @Override
    public AuditRecordCollection getAuditRecords() throws SDKException {
        String url = getSelfUri();
        return new AuditRecordCollectionImpl(restConnector, url, pageSize);
    }

    @Override
    public AuditRecordRepresentation create(AuditRecordRepresentation representation) throws SDKException {
        return restConnector.post(getSelfUri(), AuditMediaType.AUDIT_RECORD, representation);
    }

    @Override
    public AuditRecordCollection getAuditRecordsByFilter(AuditRecordFilter filter)
            throws SDKException {
        if (filter == null) {
            return getAuditRecords();
        }
        Map<String, String> params = filter.getQueryParams();
        return new AuditRecordCollectionImpl(restConnector, urlProcessor.replaceOrAddQueryParam(getSelfUri(), params), pageSize);
    }

}
