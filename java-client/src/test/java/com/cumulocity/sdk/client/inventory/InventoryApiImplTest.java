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
package com.cumulocity.sdk.client.inventory;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.sameInstance;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.when;

import java.util.Collections;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.cumulocity.model.idtype.GId;
import com.cumulocity.rest.representation.inventory.InventoryMediaType;
import com.cumulocity.rest.representation.inventory.InventoryRepresentation;
import com.cumulocity.rest.representation.inventory.ManagedObjectCollectionRepresentation;
import com.cumulocity.rest.representation.inventory.ManagedObjectReferenceCollectionRepresentation;
import com.cumulocity.rest.representation.inventory.ManagedObjectRepresentation;
import com.cumulocity.rest.representation.platform.PlatformApiRepresentation;
import com.cumulocity.rest.representation.platform.PlatformMediaType;
import com.cumulocity.sdk.client.PagedCollectionResource;
import com.cumulocity.sdk.client.RestConnector;
import com.cumulocity.sdk.client.SDKException;
import com.cumulocity.sdk.client.UrlProcessor;

public class InventoryApiImplTest {

    private static final String TEMPLATE_URL = "template_url";

    private static final String INVENTORY_COLLECTION_URL = "inventory_collection_url";

    private static final String PLATFORM_API_URL = "inventory_api_url";

    private static final int DEFAULT_PAGE_SIZE = 9;

    InventoryApi inventoryApiResource;

    InventoryRepresentation inventoryRepresentation = new InventoryRepresentation();

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Mock
    private RestConnector restConnector;

    @Mock
    private UrlProcessor urlProcessor;

    @Before
    public void setup() throws Exception {
        MockitoAnnotations.initMocks(this);
        inventoryApiResource = new InventoryApiImpl(restConnector, urlProcessor, PLATFORM_API_URL, DEFAULT_PAGE_SIZE);

        ManagedObjectReferenceCollectionRepresentation representation = new ManagedObjectReferenceCollectionRepresentation();
        representation.setSelf(INVENTORY_COLLECTION_URL);
        inventoryRepresentation.setManagedObjects(representation);
        PlatformApiRepresentation platformApiRepresentation = new PlatformApiRepresentation();
        platformApiRepresentation.setInventory(inventoryRepresentation);
        when(restConnector.get(PLATFORM_API_URL, PlatformMediaType.PLATFORM_API, PlatformApiRepresentation.class)).thenReturn(
                platformApiRepresentation);
    }

    @Test
    public void shouldCreateMo() throws Exception {
        //Given 
        ManagedObjectRepresentation managedObjectRepresentation = new ManagedObjectRepresentation();
        ManagedObjectRepresentation created = new ManagedObjectRepresentation();
        when(restConnector.post(INVENTORY_COLLECTION_URL, InventoryMediaType.MANAGED_OBJECT, managedObjectRepresentation)).thenReturn(
                created);

        // When 
        ManagedObjectRepresentation mor = inventoryApiResource.create(managedObjectRepresentation);

        // Then 
        assertThat(mor, sameInstance(created));
    }

    @Test
    public void shouldGetMo() throws Exception {
        //Given 
        String gidValue = "gid_value";
        GId gid = new GId(gidValue);

        //When
        ManagedObjectImpl mo = (ManagedObjectImpl) inventoryApiResource.getManagedObject(gid);

        // Then
        assertThat(mo.url, is(INVENTORY_COLLECTION_URL + "/" + gidValue));
    }

    @Test(expected = SDKException.class)
    public void shouldThrowExceptionWhenGetWithNullInput() throws SDKException {
        // Given 

        // When
        inventoryApiResource.getManagedObject(null);

        // Then
        fail();
    }

    @Test(expected = SDKException.class)
    public void shouldThrowExceptionWhenGetWithIncorrectGid() throws SDKException {
        // Given 
        GId gid = new GId();

        // When
        inventoryApiResource.getManagedObject(gid);

        // Then
        fail();
    }

    @Test
    public void shouldGetMos() throws SDKException {
        // Given
        PagedCollectionResource<ManagedObjectCollectionRepresentation> expected = new ManagedObjectCollectionImpl(restConnector,
                INVENTORY_COLLECTION_URL, DEFAULT_PAGE_SIZE);

        // When
        PagedCollectionResource<ManagedObjectCollectionRepresentation> result = inventoryApiResource.getManagedObjects();

        // Then 
        assertThat(result, is(expected));
    }

    @Test
    public void shouldGetMosByEmptyFilter() throws SDKException {
        // Given
        when(urlProcessor.replaceOrAddQueryParam(INVENTORY_COLLECTION_URL, Collections.<String, String>emptyMap())).thenReturn(INVENTORY_COLLECTION_URL);
        PagedCollectionResource<ManagedObjectCollectionRepresentation> expected = new ManagedObjectCollectionImpl(restConnector,
                INVENTORY_COLLECTION_URL, DEFAULT_PAGE_SIZE);

        // When
        InventoryFilter filter = new InventoryFilter();
        PagedCollectionResource<ManagedObjectCollectionRepresentation> result = inventoryApiResource.getManagedObjectsByFilter(filter);

        // Then 
        assertThat(result, is(expected));
    }

    @Test
    public void shouldGetMosByTypeFilter() throws SDKException {
        // Given 
        String myType = "myType";
        InventoryFilter filter = new InventoryFilter().byType(myType);
        inventoryRepresentation.setManagedObjectsForType(TEMPLATE_URL);
        String moByTypeUrl = INVENTORY_COLLECTION_URL + "?type=" + myType;
        when(urlProcessor.replaceOrAddQueryParam(INVENTORY_COLLECTION_URL, filter.getQueryParams())).thenReturn(moByTypeUrl);
        PagedCollectionResource<ManagedObjectCollectionRepresentation> expected = new ManagedObjectCollectionImpl(restConnector, moByTypeUrl, DEFAULT_PAGE_SIZE);

        // When
        PagedCollectionResource<ManagedObjectCollectionRepresentation> result = inventoryApiResource.getManagedObjectsByFilter(filter);

        // Then 
        assertThat(result, is(expected));
    }    
}
