package com.cumulocity.sdk.client.inventory;

import static com.cumulocity.model.util.ExtensibilityConverter.classToStringRepresentation;
import static java.util.Collections.singletonMap;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.sameInstance;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

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
import com.cumulocity.sdk.client.TemplateUrlParser;

public class InventoryApiImplTest {

    private static final String EXACT_URL = "exact_url";

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
    private TemplateUrlParser templateUrlParser;

    @Before
    public void setup() throws Exception {
        MockitoAnnotations.initMocks(this);
        inventoryApiResource = new InventoryApiImpl(restConnector, templateUrlParser, PLATFORM_API_URL, DEFAULT_PAGE_SIZE);

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
        inventoryRepresentation.setManagedObjectsForType(TEMPLATE_URL);
        when(templateUrlParser.replacePlaceholdersWithParams(TEMPLATE_URL, singletonMap("type", myType))).thenReturn(EXACT_URL);

        PagedCollectionResource<ManagedObjectCollectionRepresentation> expected = new ManagedObjectCollectionImpl(restConnector, EXACT_URL,
                DEFAULT_PAGE_SIZE);

        // When
        InventoryFilter filter = new InventoryFilter().byType(myType);
        PagedCollectionResource<ManagedObjectCollectionRepresentation> result = inventoryApiResource.getManagedObjectsByFilter(filter);

        // Then 
        assertThat(result, is(expected));
    }

    @Test
    public void shouldGetMosByFragmentTypeFilterClass() throws SDKException {
        // Given 
        inventoryRepresentation.setManagedObjectsForFragmentType(TEMPLATE_URL);
        Class<?> myFragment = NonRelevantFragmentClass.class;
        when(
                templateUrlParser.replacePlaceholdersWithParams(TEMPLATE_URL,
                        singletonMap("fragmentType", classToStringRepresentation(myFragment)))).thenReturn(EXACT_URL);

        PagedCollectionResource<ManagedObjectCollectionRepresentation> expected = new ManagedObjectCollectionImpl(restConnector, EXACT_URL,
                DEFAULT_PAGE_SIZE);

        // When
        InventoryFilter filter = new InventoryFilter().byFragmentType(myFragment);
        PagedCollectionResource<ManagedObjectCollectionRepresentation> result = inventoryApiResource.getManagedObjectsByFilter(filter);

        // Then 
        assertThat(result, is(expected));
    }

    @Test
    public void shouldGetMosByFragmentTypeFilterString() throws SDKException {
        // Given 
        inventoryRepresentation.setManagedObjectsForFragmentType(TEMPLATE_URL);
        String myFragment = "myFragment";
        when(templateUrlParser.replacePlaceholdersWithParams(TEMPLATE_URL, singletonMap("fragmentType", myFragment))).thenReturn(EXACT_URL);

        PagedCollectionResource<ManagedObjectCollectionRepresentation> expected = new ManagedObjectCollectionImpl(restConnector, EXACT_URL,
                DEFAULT_PAGE_SIZE);
        // When
        InventoryFilter filter = new InventoryFilter().byFragmentType(myFragment);
        PagedCollectionResource<ManagedObjectCollectionRepresentation> result = inventoryApiResource.getManagedObjectsByFilter(filter);

        // Then 
        assertThat(result, is(expected));
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowIllegalArgumentExceptionWhenSearchingByFragmentTypeAndTypeFilter() throws SDKException {
        // When
        InventoryFilter filter = new InventoryFilter().byFragmentType(Object.class).byType("myType");
        inventoryApiResource.getManagedObjectsByFilter(filter);
    }

    @Test
    public void shouldReturnEmptyCollectionWhenSearchingWithEmptyListOfIds() throws SDKException {
        // When
        PagedCollectionResource<ManagedObjectCollectionRepresentation> result = inventoryApiResource
                .getManagedObjectsByListOfIds(Collections.EMPTY_LIST);
        // Then
        assertThat(result.get().getManagedObjects().size(), is(equalTo(0)));
    }

    @Test
    public void shouldGetMosByRequestedIdsOneElem() throws SDKException {
        // Given 
        inventoryRepresentation.setManagedObjectsForListOfIds(TEMPLATE_URL);
        when(templateUrlParser.replacePlaceholdersWithParams(TEMPLATE_URL, singletonMap("ids", "1"))).thenReturn(EXACT_URL);

        PagedCollectionResource<ManagedObjectCollectionRepresentation> expected = new ManagedObjectCollectionImpl(restConnector, EXACT_URL,
                DEFAULT_PAGE_SIZE);

        // When
        List<GId> ids = new LinkedList<GId>();
        ids.add(new GId("1"));
        PagedCollectionResource<ManagedObjectCollectionRepresentation> result = inventoryApiResource.getManagedObjectsByListOfIds(ids);

        // Then
        assertThat(result, is(expected));
    }

    @Test
    public void shouldGetMosByRequestedIdsManyElemsPreservedOrder() throws SDKException {
        // Given 
        inventoryRepresentation.setManagedObjectsForListOfIds(TEMPLATE_URL);
        when(templateUrlParser.replacePlaceholdersWithParams(TEMPLATE_URL, singletonMap("ids", "111,1,5"))).thenReturn(EXACT_URL);

        PagedCollectionResource<ManagedObjectCollectionRepresentation> expected = new ManagedObjectCollectionImpl(restConnector, EXACT_URL,
                DEFAULT_PAGE_SIZE);

        // When
        List<GId> ids = new LinkedList<GId>();
        ids.add(new GId("111"));
        ids.add(new GId("1"));
        ids.add(new GId("5"));
        PagedCollectionResource<ManagedObjectCollectionRepresentation> result = inventoryApiResource.getManagedObjectsByListOfIds(ids);

        // Then
        assertThat(result, is(expected));
    }

    private static class NonRelevantFragmentClass {
    }

}
