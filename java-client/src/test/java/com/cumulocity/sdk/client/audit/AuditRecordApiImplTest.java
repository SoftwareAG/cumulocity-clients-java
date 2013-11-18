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

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.sameInstance;
import static org.mockito.Mockito.when;

import java.util.Collections;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.cumulocity.model.idtype.GId;
import com.cumulocity.rest.representation.audit.AuditMediaType;
import com.cumulocity.rest.representation.audit.AuditRecordCollectionRepresentation;
import com.cumulocity.rest.representation.audit.AuditRecordRepresentation;
import com.cumulocity.rest.representation.audit.AuditRecordsRepresentation;
import com.cumulocity.sdk.client.PagedCollectionResource;
import com.cumulocity.sdk.client.RestConnector;
import com.cumulocity.sdk.client.SDKException;
import com.cumulocity.sdk.client.UrlProcessor;

public class AuditRecordApiImplTest {

    private static final String TEMPLATE_URL = "template_url";

    private static final String AUDIT_RECORDS_URL = "audit_records_url";

    private static final int DEFAULT_PAGE_SIZE = 555;

    private AuditRecordApi auditRecordApi;

    private AuditRecordsRepresentation auditRecordsApiRepresentation;

    @Mock
    private RestConnector restConnector;

    @Mock
    private UrlProcessor urlProcessor;

    @Before
    public void setup() throws Exception {
        MockitoAnnotations.initMocks(this);

        auditRecordsApiRepresentation = new AuditRecordsRepresentation();
        AuditRecordCollectionRepresentation collectionRepresentation = new AuditRecordCollectionRepresentation();
        collectionRepresentation.setSelf(AUDIT_RECORDS_URL);
        auditRecordsApiRepresentation.setAuditRecords(collectionRepresentation);

        auditRecordApi = new AuditRecordApiImpl(restConnector, urlProcessor, auditRecordsApiRepresentation, DEFAULT_PAGE_SIZE);
    }

    @Test
    public void shouldGetAuditRecord() throws SDKException {
        // Given 
        GId gid = new GId("123");
        AuditRecordRepresentation auditRep = new AuditRecordRepresentation();
        when(restConnector.get(AUDIT_RECORDS_URL + "/123", AuditMediaType.AUDIT_RECORD, AuditRecordRepresentation.class)).thenReturn(
                auditRep);

        // When
        AuditRecordRepresentation result = auditRecordApi.getAuditRecord(gid);

        //then
        assertThat(result, sameInstance(auditRep));
    }

    @Test
    public void shouldCreateAuditRecord() throws SDKException {
        // Given 
        AuditRecordRepresentation input = new AuditRecordRepresentation();
        AuditRecordRepresentation created = new AuditRecordRepresentation();
        when(restConnector.post(AUDIT_RECORDS_URL, AuditMediaType.AUDIT_RECORD, input)).thenReturn(created);

        // When
        AuditRecordRepresentation result = auditRecordApi.create(input);

        // Then
        assertThat(result, sameInstance(created));
    }

    @Test
    public void shouldReturnAuditRecords() throws Exception {
        // Given
        PagedCollectionResource<AuditRecordCollectionRepresentation> expected = new AuditRecordCollectionImpl(restConnector,
                AUDIT_RECORDS_URL, DEFAULT_PAGE_SIZE);

        // When
        PagedCollectionResource<AuditRecordCollectionRepresentation> result = auditRecordApi.getAuditRecords();

        // Then
        assertThat(result, is(expected));
    }

    @Test
    public void shouldReturnAuditRecordsByEmptyFilter() throws Exception {
        // Given
        when(urlProcessor.replaceOrAddQueryParam(AUDIT_RECORDS_URL, Collections.<String, String>emptyMap())).thenReturn(AUDIT_RECORDS_URL);
        PagedCollectionResource<AuditRecordCollectionRepresentation> expected = new AuditRecordCollectionImpl(restConnector,
                AUDIT_RECORDS_URL, DEFAULT_PAGE_SIZE);

        // When
        PagedCollectionResource<AuditRecordCollectionRepresentation> result = auditRecordApi
                .getAuditRecordsByFilter(new AuditRecordFilter());

        // Then
        assertThat(result, is(expected));
    }

    @Test
    public void shouldReturnAuditRecordsByTypeFilter() throws Exception {
        // Given
        String myType = "myType";
        AuditRecordFilter filter = new AuditRecordFilter().byType(myType);
        auditRecordsApiRepresentation.setAuditRecordsForType(TEMPLATE_URL);
        String auditsByTypeUrl = AUDIT_RECORDS_URL + "?type=" + myType;
        when(urlProcessor.replaceOrAddQueryParam(AUDIT_RECORDS_URL, filter.getQueryParams())).thenReturn(auditsByTypeUrl);
        PagedCollectionResource<AuditRecordCollectionRepresentation> expected = new AuditRecordCollectionImpl(restConnector, auditsByTypeUrl,
                DEFAULT_PAGE_SIZE);

        // When
        PagedCollectionResource<AuditRecordCollectionRepresentation> result = auditRecordApi.getAuditRecordsByFilter(filter);

        // Then
        assertThat(result, is(expected));
    }
}
