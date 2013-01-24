package com.cumulocity.sdk.client.inventory;

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

import static com.cumulocity.sdk.client.inventory.ManagedObjectImpl.PAGE_SIZE_PARAM_WITH_MAX_VALUE;

public class ManagedObjectImplTest {

    private static final String MANAGED_OBJECT_URL = "path_to_managedobject";

    private static final String CHILD_DEVICES_URL = "path_to_child_devices";

    private static final String CHILD_ASSETS_URL = "path_to_child_assets";

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

        managedObjectRep = createMoWithChildDevicesAndAssets();
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
        when(restConnector.post(CHILD_DEVICES_URL, InventoryMediaType.MANAGED_OBJECT_REFERENCE, newChildDevice)).thenReturn(created);

        // when 
        ManagedObjectReferenceRepresentation result = managedObject.addChildDevice(newChildDevice);

        // then 
        assertThat(result, sameInstance(created));
    }

    @Test
    public void testGetChildDevice() throws SDKException {
        //Given
        GId gid = new GId("deviceId");
        ManagedObjectReferenceRepresentation retrieved = new ManagedObjectReferenceRepresentation();
        when(
                restConnector.get(CHILD_DEVICES_URL + "/deviceId", InventoryMediaType.MANAGED_OBJECT_REFERENCE,
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
        ManagedObjectReferenceCollectionRepresentation retrieved = new ManagedObjectReferenceCollectionRepresentation();
        when(
                restConnector.get(CHILD_DEVICES_URL+ PAGE_SIZE_PARAM_WITH_MAX_VALUE, InventoryMediaType.MANAGED_OBJECT_REFERENCE_COLLECTION,
                        ManagedObjectReferenceCollectionRepresentation.class)).thenReturn(retrieved);

        // when 
        ManagedObjectReferenceCollectionRepresentation result = managedObject.getAllChildDevices();

        // then
        assertThat(result, sameInstance(retrieved));
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
        when(
                restConnector.get(CHILD_ASSETS_URL+PAGE_SIZE_PARAM_WITH_MAX_VALUE, InventoryMediaType.MANAGED_OBJECT_REFERENCE_COLLECTION,
                        ManagedObjectReferenceCollectionRepresentation.class)).thenReturn(retrieved);

        // when 
        ManagedObjectReferenceCollectionRepresentation result = managedObject.getAllChildAssets();

        // then
        assertThat(result, sameInstance(retrieved));

    }

    @Test
    public void testGetChildAsset() throws Exception {
        //Given
        GId gid = new GId("assetId");
        ManagedObjectReferenceRepresentation retrieved = new ManagedObjectReferenceRepresentation();
        when(
                restConnector.get(CHILD_ASSETS_URL + "/assetId", InventoryMediaType.MANAGED_OBJECT_REFERENCE,
                        ManagedObjectReferenceRepresentation.class)).thenReturn(retrieved);

        // when 
        ManagedObjectReferenceRepresentation result = managedObject.getChildAsset(gid);

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
    public void testAddChildAsset() throws Exception {
        //Given 
        ManagedObjectReferenceRepresentation created = new ManagedObjectReferenceRepresentation();
        ManagedObjectReferenceRepresentation newChildAsset = new ManagedObjectReferenceRepresentation();
        when(restConnector.post(CHILD_ASSETS_URL, InventoryMediaType.MANAGED_OBJECT_REFERENCE, newChildAsset)).thenReturn(created);

        // when 
        ManagedObjectReferenceRepresentation result = managedObject.addChildAssets(newChildAsset);

        // then 
        assertThat(result, sameInstance(created));
    }

    private ManagedObjectRepresentation createMoWithChildDevicesAndAssets() {
        ManagedObjectRepresentation mo = new ManagedObjectRepresentation();
        ManagedObjectReferenceCollectionRepresentation childDevices = new ManagedObjectReferenceCollectionRepresentation();
        childDevices.setSelf(CHILD_DEVICES_URL);
        mo.setChildDevices(childDevices);

        ManagedObjectReferenceCollectionRepresentation childAssets = new ManagedObjectReferenceCollectionRepresentation();
        childAssets.setSelf(CHILD_ASSETS_URL);
        mo.setChildAssets(childAssets);

        return mo;
    }

}
