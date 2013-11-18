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

import static java.util.Collections.singletonMap;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.sameInstance;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.cumulocity.model.idtype.GId;
import com.cumulocity.model.idtype.XtId;
import com.cumulocity.rest.representation.identity.ExternalIDCollectionRepresentation;
import com.cumulocity.rest.representation.identity.ExternalIDRepresentation;
import com.cumulocity.rest.representation.identity.IdentityMediaType;
import com.cumulocity.rest.representation.identity.IdentityRepresentation;
import com.cumulocity.rest.representation.inventory.ManagedObjectRepresentation;
import com.cumulocity.sdk.client.PagedCollectionResource;
import com.cumulocity.sdk.client.RestConnector;
import com.cumulocity.sdk.client.SDKException;
import com.cumulocity.sdk.client.TemplateUrlParser;

public class IdentityApiImplTest {

    private static final String EXACT_URL = "exact_url";

    private static final String TEMPLATE_URL = "template_url";

    private static final int DEFAULT_PAGE_SIZE = 8;

    IdentityApiImpl identityApiImpl;

    @Mock
    private RestConnector restConnector;

    @Mock
    private TemplateUrlParser templateUrlParser;

    private IdentityRepresentation identityApiRep;

    @Before
    public void setup() throws Exception {
        MockitoAnnotations.initMocks(this);

        identityApiRep = new IdentityRepresentation();
        identityApiImpl = new IdentityApiImpl(restConnector, templateUrlParser, identityApiRep, DEFAULT_PAGE_SIZE);
    }

    @Test
    public void shouldLookupExternalId() throws SDKException {
        // Given
        String extIdType = "myExtIdType";
        String extIdValue = "myExtIdValue";

        XtId extID = new XtId(extIdValue);
        extID.setType(extIdType);

        Map<String, String> params = new HashMap<String, String>();
        params.put("type", extIdType);
        params.put("externaId", extIdValue);

        ExternalIDRepresentation extIdRep = new ExternalIDRepresentation();

        identityApiRep.setExternalId(TEMPLATE_URL);
        when(templateUrlParser.replacePlaceholdersWithParams(TEMPLATE_URL, params)).thenReturn(EXACT_URL);
        when(restConnector.get(EXACT_URL, IdentityMediaType.EXTERNAL_ID, ExternalIDRepresentation.class)).thenReturn(extIdRep);

        
        // When
        ExternalIDRepresentation result = identityApiImpl.getExternalId(extID);

        // Then
        assertThat(result, sameInstance(extIdRep));
    }

    @Test
    public void testDelete() throws SDKException {
        // Given
        String extIdType = "myExtIdType";
        String extIdValue = "myExtIdValue";
        ExternalIDRepresentation extIdRep = new ExternalIDRepresentation();
        extIdRep.setExternalId(extIdValue);
        extIdRep.setType(extIdType);

        Map<String, String> params = new HashMap<String, String>();
        params.put("type", extIdType);
        params.put("externaId", extIdValue);

        identityApiRep.setExternalId(TEMPLATE_URL);
        when(templateUrlParser.replacePlaceholdersWithParams(TEMPLATE_URL, params)).thenReturn(EXACT_URL);

        // When
        identityApiImpl.deleteExternalId(extIdRep);

        // Then
        verify(restConnector).delete(EXACT_URL);
    }

    @Test(expected = SDKException.class)
    public void shouldThrowExceptionWhenExtIdWithoutValueAndType() throws SDKException {
        // Given
        XtId extID = new XtId();

        // When
        identityApiImpl.getExternalId(extID);
    }

    @Test(expected = SDKException.class)
    public void shouldThrowExceptionWhenGidWithoutValue() throws SDKException {
        // Given
        GId gid = new GId();

        // When
        identityApiImpl.getExternalIdsOfGlobalId(gid);
    }

    @Test(expected = SDKException.class)
    public void shouldThrowExceptionWhenGlobalIdWithNullValue() throws SDKException {
        identityApiImpl.getExternalIdsOfGlobalId(new GId(null));
    }

    @Test
    public void testGetExternalIdsForGId() throws Exception {
        // Given 
        GId gid = new GId("10");
        identityApiRep.setExternalIdsOfGlobalId(TEMPLATE_URL);
        when(templateUrlParser.replacePlaceholdersWithParams(TEMPLATE_URL, singletonMap("globalId", "10"))).thenReturn(EXACT_URL);

        PagedCollectionResource<ExternalIDCollectionRepresentation> expected = new ExternalIDCollectionImpl(restConnector, EXACT_URL,
                DEFAULT_PAGE_SIZE);

        // when
        PagedCollectionResource<ExternalIDCollectionRepresentation> result = identityApiImpl.getExternalIdsOfGlobalId(gid);

        // then
        assertThat(result, is(expected));
    }

    @Test
    public void shouldCreate() throws Exception {
        // Given
        ManagedObjectRepresentation mo = new ManagedObjectRepresentation();
        String globalIdValue = "myGlobalIdValue";
        mo.setId(new GId(globalIdValue));

        ExternalIDRepresentation repToCreate = new ExternalIDRepresentation();
        repToCreate.setManagedObject(mo);

        ExternalIDRepresentation createdRep = new ExternalIDRepresentation();

        identityApiRep.setExternalIdsOfGlobalId(TEMPLATE_URL);
        when(templateUrlParser.replacePlaceholdersWithParams(TEMPLATE_URL, singletonMap("globalId", globalIdValue))).thenReturn(EXACT_URL);
        when(restConnector.post(EXACT_URL, IdentityMediaType.EXTERNAL_ID, repToCreate)).thenReturn(createdRep);

        // When
        ExternalIDRepresentation result = identityApiImpl.create(repToCreate);

        // Then
        assertThat(result, sameInstance(createdRep));
    }
}
