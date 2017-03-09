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

import static com.cumulocity.rest.representation.inventory.InventoryMediaType.MANAGED_OBJECT;
import static com.cumulocity.rest.representation.inventory.InventoryMediaType.MANAGED_OBJECT_REFERENCE;
import static com.cumulocity.rest.representation.inventory.InventoryMediaType.MANAGED_OBJECT_REFERENCE_COLLECTION;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.sameInstance;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.cumulocity.model.idtype.GId;
import com.cumulocity.rest.representation.inventory.InventoryMediaType;
import com.cumulocity.rest.representation.inventory.ManagedObjectReferenceCollectionRepresentation;
import com.cumulocity.rest.representation.inventory.ManagedObjectReferenceRepresentation;
import com.cumulocity.rest.representation.inventory.ManagedObjectRepresentation;
import com.cumulocity.sdk.client.RestConnector;
import com.cumulocity.sdk.client.SDKException;

public class ManagedObjectImplTest {

    private static final String MANAGED_OBJECT_URL = "path_to_managedobject";

    private static final String CHILD_DEVICES_URL = "path_to_child_devices";

    private static final String CHILD_ASSETS_URL = "path_to_child_assets";

    private static final String CHILD_ADDITIONS_URL = "path_to_child_additions";

    private static final int DEFAULT_PAGE_SIZE = 9;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private ManagedObjectImpl managedObject;

    @Mock
    private RestConnector restConnector;

    private ManagedObjectRepresentation managedObjectRep;

    @Before
    public void setup() throws Exception {
        MockitoAnnotations.initMocks(this);
        managedObject = new ManagedObjectImpl(restConnector, MANAGED_OBJECT_URL, DEFAULT_PAGE_SIZE);

        managedObjectRep = createMoWithChildDevicesAndAssetsAndAdditions();
        when(restConnector.get(MANAGED_OBJECT_URL, InventoryMediaType.MANAGED_OBJECT, ManagedObjectRepresentation.class)).thenReturn(
                managedObjectRep);
    }

    @Test
    public void testGet() throws Exception {
        //when
        ManagedObjectRepresentation mor = managedObject.get();

        // then 
        assertThat(mor, sameInstance(managedObjectRep));
    }

    @Test
    public void testAddChildDevice() throws SDKException {
        //Given 
        ManagedObjectReferenceRepresentation created = new ManagedObjectReferenceRepresentation();
        ManagedObjectReferenceRepresentation newChildDevice = new ManagedObjectReferenceRepresentation();
        when(restConnector.post(CHILD_DEVICES_URL, MANAGED_OBJECT_REFERENCE, newChildDevice)).thenReturn(created);

        // when 
        ManagedObjectReferenceRepresentation result = managedObject.addChildDevice(newChildDevice);

        // then 
        assertThat(result, sameInstance(created));
    }

    @Test
    public void testCreateAndAddChildDevice() throws SDKException {
        //Given
        ManagedObjectRepresentation created = new ManagedObjectRepresentation();
        ManagedObjectRepresentation newChildDevice = new ManagedObjectRepresentation();
        when(restConnector.post(CHILD_DEVICES_URL, MANAGED_OBJECT, newChildDevice)).thenReturn(created);

        // when
        ManagedObjectRepresentation result = managedObject.addChildDevice(newChildDevice);

        // then
        assertThat(result, sameInstance(created));
    }

    @Test
    public void testGetChildDevice() throws SDKException {
        //Given
        GId gid = new GId("deviceId");
        ManagedObjectReferenceRepresentation retrieved = new ManagedObjectReferenceRepresentation();
        when(restConnector.get(CHILD_DEVICES_URL + "/deviceId", MANAGED_OBJECT_REFERENCE,
                ManagedObjectReferenceRepresentation.class)).thenReturn(retrieved);

        // when 
        ManagedObjectReferenceRepresentation result = managedObject.getChildDevice(gid);

        // then
        assertThat(result, sameInstance(retrieved));
    }

    @Test
    public void testDelete() throws SDKException {
        // when 
        managedObject.delete();

        // then
        verify(restConnector).delete(MANAGED_OBJECT_URL);
    }

    @Test
    public void testUpdate() throws SDKException {
        //Given 
        ManagedObjectRepresentation mo = new ManagedObjectRepresentation();
        ManagedObjectRepresentation updated = new ManagedObjectRepresentation();
        when(restConnector.put(MANAGED_OBJECT_URL, InventoryMediaType.MANAGED_OBJECT, mo)).thenReturn(updated);

        // when 
        ManagedObjectRepresentation result = managedObject.update(mo);

        // then 
        assertThat(result, sameInstance(updated));

    }

    @Test
    public void testGetAllChildDevice() throws SDKException {
        //Given
        ManagedObjectReferenceCollectionRepresentation retrieved =
                new ManagedObjectReferenceCollectionRepresentation();
        when(restConnector.get(CHILD_DEVICES_URL+ "?pageSize=" + DEFAULT_PAGE_SIZE, MANAGED_OBJECT_REFERENCE_COLLECTION,
                ManagedObjectReferenceCollectionRepresentation.class)).thenReturn(retrieved);

        // when 
        ManagedObjectReferenceCollectionRepresentation result = managedObject.getChildDevices().get();

        // then
        assertThat(result.getReferences(), sameInstance(retrieved.getReferences()));
    }

    @Test
    public void testDeleteChildDevice() throws Exception {
        //Given 
        GId gid = new GId("deviceId");

        // when 
        managedObject.deleteChildDevice(gid);

        // then 
        verify(restConnector).delete(CHILD_DEVICES_URL + "/deviceId");
    }

    @Test
    public void testGetAllChildAssets() throws Exception {
        //Given 
        ManagedObjectReferenceCollectionRepresentation retrieved = new ManagedObjectReferenceCollectionRepresentation();
        when(restConnector.get(CHILD_ASSETS_URL+ "?pageSize=" +DEFAULT_PAGE_SIZE, MANAGED_OBJECT_REFERENCE_COLLECTION,
                ManagedObjectReferenceCollectionRepresentation.class)).thenReturn(retrieved);

        // when 
        ManagedObjectReferenceCollectionRepresentation result = managedObject.getChildAssets().get();

        // then
        assertThat(result.getReferences(), sameInstance(retrieved.getReferences()));
    }

    @Test
    public void testGetAllChildAdditions() throws Exception {
        //Given
        ManagedObjectReferenceCollectionRepresentation retrieved = new ManagedObjectReferenceCollectionRepresentation();
        when(restConnector.get(CHILD_ADDITIONS_URL+ "?pageSize=" +DEFAULT_PAGE_SIZE, MANAGED_OBJECT_REFERENCE_COLLECTION,
                ManagedObjectReferenceCollectionRepresentation.class)).thenReturn(retrieved);

        // when
        ManagedObjectReferenceCollectionRepresentation result = managedObject.getChildAdditions().get();

        // then
        assertThat(result.getReferences(), sameInstance(retrieved.getReferences()));
    }

    @Test
    public void testGetChildAsset() throws Exception {
        //Given
        GId gid = new GId("assetId");
        ManagedObjectReferenceRepresentation retrieved = new ManagedObjectReferenceRepresentation();
        when(restConnector.get(CHILD_ASSETS_URL + "/assetId", MANAGED_OBJECT_REFERENCE,
                ManagedObjectReferenceRepresentation.class)).thenReturn(retrieved);

        // when 
        ManagedObjectReferenceRepresentation result = managedObject.getChildAsset(gid);

        // then
        assertThat(result, sameInstance(retrieved));
    }

    @Test
    public void testGetChildAddition() throws Exception {
        //Given
        GId gid = new GId("additionId");
        ManagedObjectReferenceRepresentation retrieved = new ManagedObjectReferenceRepresentation();
        when(restConnector.get(CHILD_ADDITIONS_URL + "/additionId", MANAGED_OBJECT_REFERENCE,
                ManagedObjectReferenceRepresentation.class)).thenReturn(retrieved);

        // when
        ManagedObjectReferenceRepresentation result = managedObject.getChildAddition(gid);

        // then
        assertThat(result, sameInstance(retrieved));
    }

    @Test
    public void testDeleteChildAsset() throws Exception {
        //Given 
        GId gid = new GId("assetId");

        // when 
        managedObject.deleteChildAsset(gid);

        // then 
        verify(restConnector).delete(CHILD_ASSETS_URL + "/assetId");
    }

    @Test
    public void testDeleteChildAddition() throws Exception {
        //Given
        GId gid = new GId("additionId");

        // when
        managedObject.deleteChildAddition(gid);

        // then
        verify(restConnector).delete(CHILD_ADDITIONS_URL + "/additionId");
    }

    @Test
    public void testAddChildAsset() throws Exception {
        //Given 
        ManagedObjectReferenceRepresentation created = new ManagedObjectReferenceRepresentation();
        ManagedObjectReferenceRepresentation newChildAsset = new ManagedObjectReferenceRepresentation();
        when(restConnector.post(CHILD_ASSETS_URL, MANAGED_OBJECT_REFERENCE, newChildAsset)).thenReturn(created);

        // when 
        ManagedObjectReferenceRepresentation result = managedObject.addChildAssets(newChildAsset);

        // then 
        assertThat(result, sameInstance(created));
    }

    @Test
    public void testCreateAndAddChildAsset() throws Exception {
        //Given
        ManagedObjectRepresentation created = new ManagedObjectRepresentation();
        ManagedObjectRepresentation newChildAsset = new ManagedObjectRepresentation();
        when(restConnector.post(CHILD_ASSETS_URL, MANAGED_OBJECT, newChildAsset)).thenReturn(created);

        // when
        ManagedObjectRepresentation result = managedObject.addChildAsset(newChildAsset);

        // then
        assertThat(result, sameInstance(created));
    }

    @Test
    public void testAddChildAddition() throws Exception {
        //Given
        ManagedObjectReferenceRepresentation created = new ManagedObjectReferenceRepresentation();
        ManagedObjectReferenceRepresentation newChildAddition = new ManagedObjectReferenceRepresentation();
        when(restConnector.post(CHILD_ADDITIONS_URL, MANAGED_OBJECT_REFERENCE, newChildAddition)).thenReturn(created);

        // when
        ManagedObjectReferenceRepresentation result = managedObject.addChildAdditions(newChildAddition);

        // then
        assertThat(result, sameInstance(created));
    }

    @Test
    public void testCreateAndAddChildAddition() throws Exception {
        //Given
        ManagedObjectRepresentation created = new ManagedObjectRepresentation();
        ManagedObjectRepresentation newChildAddition = new ManagedObjectRepresentation();
        when(restConnector.post(CHILD_ADDITIONS_URL, MANAGED_OBJECT, newChildAddition)).thenReturn(created);

        // when
        ManagedObjectRepresentation result = managedObject.addChildAddition(newChildAddition);

        // then
        assertThat(result, sameInstance(created));
    }

    private ManagedObjectRepresentation createMoWithChildDevicesAndAssetsAndAdditions() {
        ManagedObjectRepresentation mo = new ManagedObjectRepresentation();
        ManagedObjectReferenceCollectionRepresentation childDevices = new ManagedObjectReferenceCollectionRepresentation();
        childDevices.setSelf(CHILD_DEVICES_URL);
        mo.setChildDevices(childDevices);

        ManagedObjectReferenceCollectionRepresentation childAssets = new ManagedObjectReferenceCollectionRepresentation();
        childAssets.setSelf(CHILD_ASSETS_URL);
        mo.setChildAssets(childAssets);

        ManagedObjectReferenceCollectionRepresentation childAdditions = new ManagedObjectReferenceCollectionRepresentation();
        childAdditions.setSelf(CHILD_ADDITIONS_URL);
        mo.setChildAdditions(childAdditions);

        return mo;
    }

}
