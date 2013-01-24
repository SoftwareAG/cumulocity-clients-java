package com.cumulocity.sdk.client.inventory;

import static com.cumulocity.me.rest.representation.RestRepresentationObjectMother.anMoRefRepresentationLike;
import static com.cumulocity.me.rest.representation.RestRepresentationObjectMother.anMoRepresentationLike;
import static com.cumulocity.me.rest.representation.SampleManagedObjectReferenceRepresentation.MO_REF_REPRESENTATION;
import static com.cumulocity.me.rest.representation.SampleManagedObjectRepresentation.MO_REPRESENTATION;
import static com.cumulocity.sdk.client.common.SDKExceptionMatcher.sdkException;
import static java.util.Arrays.asList;
import static javax.microedition.io.HttpConnection.HTTP_NOT_FOUND;
import static org.fest.assertions.Assertions.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.util.HashSet;
import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.cumulocity.me.lang.ArrayList;
import com.cumulocity.me.lang.Iterator;
import com.cumulocity.me.lang.List;
import com.cumulocity.me.model.idtype.GId;
import com.cumulocity.me.rest.convert.energy.ThreePhaseElectricitySensorConverter;
import com.cumulocity.me.rest.convert.inventory.CoordinateConverter;
import com.cumulocity.me.rest.convert.inventory.SecondFragmentConverter;
import com.cumulocity.me.rest.representation.BaseCollectionRepresentation;
import com.cumulocity.me.rest.representation.inventory.Coordinate;
import com.cumulocity.me.rest.representation.inventory.ManagedObjectCollectionRepresentation;
import com.cumulocity.me.rest.representation.inventory.ManagedObjectReferenceCollectionRepresentation;
import com.cumulocity.me.rest.representation.inventory.ManagedObjectReferenceRepresentation;
import com.cumulocity.me.rest.representation.inventory.ManagedObjectRepresentation;
import com.cumulocity.me.rest.representation.inventory.ManagedObjectRepresentationBuilder;
import com.cumulocity.me.rest.representation.inventory.SecondFragment;
import com.cumulocity.me.sdk.SDKException;
import com.cumulocity.me.sdk.client.inventory.InventoryApi;
import com.cumulocity.me.sdk.client.inventory.InventoryFilter;
import com.cumulocity.me.sdk.client.inventory.ManagedObject;
import com.cumulocity.me.sdk.client.page.PagedCollectionResource;
import com.cumulocity.model.energy.sensor.ThreePhaseElectricitySensor;
import com.cumulocity.sdk.client.common.JavaSdkITBase;

public class InventoryIT extends JavaSdkITBase {

    private InventoryApi inventory;

    @Rule
    public ExpectedException exception = ExpectedException.none();
    
    @BeforeClass
    public static void registerConverters() {
        SecondFragmentConverter fragmentConverter = new SecondFragmentConverter();
        platform.getConversionService().register(fragmentConverter);
        platform.getValidationService().register(fragmentConverter);
        
        CoordinateConverter coordinateConverter = new CoordinateConverter();
        platform.getConversionService().register(coordinateConverter);
        platform.getValidationService().register(coordinateConverter);
        
        ThreePhaseElectricitySensorConverter sensorConverter = new ThreePhaseElectricitySensorConverter();
        platform.getConversionService().register(sensorConverter);
        platform.getValidationService().register(sensorConverter);
    }

    @Before
    public void setUp() throws Exception {
        inventory = platform.getInventoryApi();
        platform.setRequireResponseBody(true);
    }

    @After
    public void deleteManagedObjects() throws Exception {
        List mosOn1stPage = getMOsFrom1stPage();
        while (!mosOn1stPage.isEmpty()) {
            deleteMOs(mosOn1stPage);
            mosOn1stPage = getMOsFrom1stPage();
        }
    }

    @Test
    public void createManagedObject() throws Exception {
        // Given
        ManagedObjectRepresentation rep = aSampleMo().build();
        
        // When
        ManagedObjectRepresentation created = inventory.create(rep);

        // Then
        assertThat(created).isNotNull();
        assertThat(created.getId()).isNotNull();
        assertThat(created.getSelf()).isNotNull();
        assertThat(created).isNotSameAs(rep);
    }

//    @Test
//    public void createManagedObjectWithoutResponseBody() throws Exception {
//        // Given
//        platform.setRequireResponseBody(false);
//        ManagedObjectRepresentation rep = aSampleMo().build();
//
//        // When
//        ManagedObjectRepresentation created = inventory.create(rep);
//
//        // Then
//        assertThat(created.getId()).isNotNull();
//        assertThat(created.getSelf()).isNull();
//        assertThat(created).isSameAs(rep);
//    }

    @Test
    public void createManagedObjectWithCoordinateFragment() throws Exception {
        // Given
        Coordinate coordinate = new Coordinate(100.0, 10.0);
        ManagedObjectRepresentation rep = aSampleMo().with(coordinate).build();

        // When
        ManagedObjectRepresentation result = inventory.create(rep);

        // Then
        assertThat(result.getId()).isNotNull();
        Coordinate fragment = (Coordinate) result.get(Coordinate.class);
        assertThat(fragment).isEqualTo(coordinate);
    }

    @Test
    public void createManagedObjectWithThreePhaseElectricitySensor() throws Exception {
        // Given
        ManagedObjectRepresentation rep = aSampleMo().with(new ThreePhaseElectricitySensor()).build();

        // When
        ManagedObjectRepresentation result = inventory.create(rep);

        // Then
        assertThat(result.getId()).isNotNull();
        assertThat(result.get(ThreePhaseElectricitySensor.class)).isNotNull();
    }

    @Test
    public void createManagedObjectWith2ThreePhaseElectricityFragments() throws Exception {
        // Given
        ManagedObjectRepresentation rep = aSampleMo().with(new ThreePhaseElectricitySensor()).with(new ThreePhaseElectricitySensor())
                .build();

        // When
        ManagedObjectRepresentation result = inventory.create(rep);

        // Then
        assertThat(result.getId()).isNotNull();
        assertThat(result.get(ThreePhaseElectricitySensor.class)).isNotNull();
    }

    @Test
    public void createAndGetManagedObject() throws Exception {
        // Given
        ManagedObjectRepresentation rep = aSampleMo().build();
        ManagedObjectRepresentation created = inventory.create(rep);

        // When
        ManagedObjectRepresentation result = inventory.getManagedObject(created.getId()).get();

        // Then
        assertThat(result.getId()).isEqualTo(created.getId());
        assertThat(result.getName()).isEqualTo(created.getName());
        assertThat(result.getType()).isEqualTo(created.getType());
    }

    @Test
    public void createAndDeleteManagedObject() throws Exception {
        // Given
        ManagedObjectRepresentation rep = aSampleMo().build();
        ManagedObjectRepresentation result = inventory.create(rep);

        // When
        ManagedObject mo = inventory.getManagedObject(result.getId());
        mo.delete();

        // Then
        exception.expect(sdkException(HTTP_NOT_FOUND));
        ManagedObject deletedMo = inventory.getManagedObject(result.getId());
        deletedMo.get();
    }

    @Test
    public void createAndUpdateManagedObject() throws Exception {
        // Given
        ManagedObjectRepresentation rep = aSampleMo().build();
        ManagedObjectRepresentation result = inventory.create(rep);

        // When
        Coordinate coordinate = new Coordinate(100.0, 10.0);
        result.set(coordinate);

        GId id = result.getId();
        result.setId(null);
        result.setLastUpdated(null);

        ManagedObjectRepresentation updated = inventory.getManagedObject(id).update(result);

        //Then
        assertThat(updated.getId()).isNotNull();
        assertThat(coordinate).isEqualTo(updated.get(Coordinate.class));
    }

    @Test @Ignore("unsupported on ME")
    public void createAndUpdateManagedObjectByRemovingFragment() throws Exception {
        // Given
        ManagedObjectRepresentation rep = aSampleMo().with(new Coordinate()).build();
        ManagedObjectRepresentation created = inventory.create(rep);

        // When
        created.set(null, Coordinate.class);
        GId id = created.getId();
        created.setId(null);
        created.setLastUpdated(null);
        ManagedObjectRepresentation updated = inventory.getManagedObject(id).update(created);

        // Then
        assertThat(updated.getId()).isNotNull();
        assertThat(updated.get(Coordinate.class)).isNull();
    }

    @Test
    public void tryToGetNonExistentManagedObject() throws Exception {
        // Given
        ManagedObjectRepresentation rep = aSampleMo().withID(new GId("1")).build();

        // Then
        exception.expect(sdkException(HTTP_NOT_FOUND));

        // When
        inventory.getManagedObject(rep.getId()).get();
    }

    @Test
    public void tryToDeleteNonExistentManagedObject() throws Exception {
        // Given
        ManagedObjectRepresentation rep = aSampleMo().withID(new GId("1")).build();

        // Then
        exception.expect(sdkException(HTTP_NOT_FOUND));

        // When
        inventory.getManagedObject(rep.getId()).delete();
    }

    @Test
    public void tryToUpdateNonExistentManagedObject() throws Exception {
        // Given
        ManagedObjectRepresentation rep = aSampleMo().build();

        // Then
        exception.expect(sdkException(HTTP_NOT_FOUND));

        // When
        inventory.getManagedObject(new GId("1")).update(rep);
    }

    @Test
    public void getAllWhenNoManagedObjectPresent() throws Exception {
        // When
        ManagedObjectCollectionRepresentation mos = (ManagedObjectCollectionRepresentation) inventory.getManagedObjects().get();

        // Then
        assertThat(mos.getManagedObjects()).isNotNull();
        assertThat(mos.getManagedObjects().isEmpty()).isTrue();
    }

    @Test
    public void getAllWhen2ManagedObjectArePresent() throws Exception {
        // Given
        ManagedObjectRepresentation rep1 = aSampleMo().withName("MO1").build();
        ManagedObjectRepresentation rep2 = aSampleMo().withName("MO2").build();

        // When
        inventory.create(rep1);
        inventory.create(rep2);

        // Then
        ManagedObjectCollectionRepresentation mos = (ManagedObjectCollectionRepresentation) inventory.getManagedObjects().get();
        assertThat(mos.getManagedObjects().size()).isEqualTo(2);
    }

    @Test
    public void addGetAndRemoveChildDevices() throws Exception {
        // Given
        ManagedObjectRepresentation parent = inventory.create(aSampleMo().withName("parent1").build());
        ManagedObjectRepresentation child1 = inventory.create(aSampleMo().withName("child11").build());
        ManagedObjectRepresentation child2 = inventory.create(aSampleMo().withName("child21").build());

        ManagedObjectReferenceRepresentation childRef1 = anMoRefRepresentationLike(MO_REF_REPRESENTATION).withMo(child1).build();
        ManagedObjectReferenceRepresentation childRef2 = anMoRefRepresentationLike(MO_REF_REPRESENTATION).withMo(child2).build();

        // When
        ManagedObject parentMo = inventory.getManagedObject(parent.getId());
        parentMo.addChildDevice(childRef1);
        parentMo.addChildDevice(childRef2);

        // Then
        ManagedObjectReferenceCollectionRepresentation refCollection = (ManagedObjectReferenceCollectionRepresentation) inventory.getManagedObject(parent.getId()).getChildDevices().get();

        List refs = refCollection.getReferences();
        Set<GId> childDeviceIDs = asSet(
                ((ManagedObjectReferenceRepresentation) refs.get(0)).getManagedObject().getId(), 
                ((ManagedObjectReferenceRepresentation) refs.get(1)).getManagedObject().getId());
        assertThat(childDeviceIDs, is(asSet(child1.getId(), child2.getId())));

        // When
        parentMo.deleteChildDevice(child1.getId());
        parentMo.deleteChildDevice(child2.getId());

        // Then
        ManagedObjectReferenceCollectionRepresentation allChildDevices = (ManagedObjectReferenceCollectionRepresentation) inventory.getManagedObject(parent.getId()).getChildDevices().get();
        assertThat(allChildDevices.getReferences().size()).isEqualTo(0);
    }

    @Test
    public void getPagedChildDevices() throws Exception {
        // Given
        ManagedObjectRepresentation parent = inventory.create(aSampleMo().withName("parent").build());
        ManagedObject parentMo = inventory.getManagedObject(parent.getId());

        for (int i = 0; i < platform.getPageSize() + 1; i++) {
            ManagedObjectRepresentation child = inventory.create(aSampleMo().withName("child" + i).build());
            ManagedObjectReferenceRepresentation childRef = anMoRefRepresentationLike(MO_REF_REPRESENTATION).withMo(child).build();
            parentMo.addChildDevice(childRef);
        }

        // When
        PagedCollectionResource refCollection = inventory.getManagedObject(parent.getId()).getChildDevices();

        // Then
        assertCollectionPaged(refCollection);

    }

    @Test
    public void addGetAndRemoveChildAssets() throws Exception {
        // Given
        ManagedObjectRepresentation parent = inventory.create(aSampleMo().withName("parent").build());
        ManagedObjectRepresentation child1 = inventory.create(aSampleMo().withName("child1").build());
        ManagedObjectRepresentation child2 = inventory.create(aSampleMo().withName("child2").build());

        ManagedObjectReferenceRepresentation childRef1 = anMoRefRepresentationLike(MO_REF_REPRESENTATION).withMo(child1).build();
        ManagedObjectReferenceRepresentation childRef2 = anMoRefRepresentationLike(MO_REF_REPRESENTATION).withMo(child2).build();

        // When
        ManagedObject parentMo = inventory.getManagedObject(parent.getId());
        parentMo.addChildAssets(childRef1);
        parentMo.addChildAssets(childRef2);

        // Then
        ManagedObjectReferenceCollectionRepresentation refCollection = (ManagedObjectReferenceCollectionRepresentation) inventory.getManagedObject(parent.getId()).getChildAssets().get();

        List refs = refCollection.getReferences();
        Set<GId> childDeviceIDs = asSet(
                ((ManagedObjectReferenceRepresentation) refs.get(0)).getManagedObject().getId(), 
                ((ManagedObjectReferenceRepresentation) refs.get(1)).getManagedObject().getId());
        
        assertThat(childDeviceIDs, is(asSet(child1.getId(), child2.getId())));

        // When
        parentMo.deleteChildAsset(child1.getId());
        parentMo.deleteChildAsset(child2.getId());

        // Then
        ManagedObjectReferenceCollectionRepresentation allChildDevices = (ManagedObjectReferenceCollectionRepresentation) inventory.getManagedObject(parent.getId()).getChildAssets().get();
        assertThat(allChildDevices.getReferences().size()).isEqualTo(0);
    }

    @Test
    public void getPagedChildAssets() throws Exception {
        // Given
        ManagedObjectRepresentation parent = inventory.create(aSampleMo().withName("parent").build());
        ManagedObject parentMo = inventory.getManagedObject(parent.getId());

        for (int i = 0; i < platform.getPageSize() + 1; i++) {
            ManagedObjectRepresentation child = inventory.create(aSampleMo().withName("child" + i).build());
            ManagedObjectReferenceRepresentation childRef = anMoRefRepresentationLike(MO_REF_REPRESENTATION).withMo(child).build();
            parentMo.addChildAssets(childRef);
        }

        // When
        PagedCollectionResource refCollection = inventory.getManagedObject(parent.getId()).getChildAssets();

        // Then
        assertCollectionPaged(refCollection);
    }
    

    private void assertCollectionPaged(PagedCollectionResource refCollection) throws SDKException {
        ManagedObjectReferenceCollectionRepresentation firstPage = (ManagedObjectReferenceCollectionRepresentation) 
                refCollection.get();
        
        assertThat(firstPage.getReferences().size()).isEqualTo(platform.getPageSize());
        assertThat(firstPage.getPageStatistics().getPageSize()).isEqualTo(platform.getPageSize());
        assertThat(firstPage.getPageStatistics().getCurrentPage()).isEqualTo(1);
        assertThat(firstPage.getSelf()).contains("pageSize=" + platform.getPageSize() + "&currentPage=1");
        assertThat(firstPage.getNext()).contains("pageSize=" + platform.getPageSize() + "&currentPage=2");
        assertThat(firstPage.getPrev()).isEmpty();

        ManagedObjectReferenceCollectionRepresentation secondPage = (ManagedObjectReferenceCollectionRepresentation) 
                refCollection.getPage((BaseCollectionRepresentation)refCollection.get(), 2);
        
        assertThat(secondPage.getReferences().size()).isEqualTo(1);
    }

    @Test
    public void queryWithManagedObjectType() throws Exception {
        // Given
        inventory.create(aSampleMo().withType("typeA").withName("A1").build());
        inventory.create(aSampleMo().withType("typeA").withName("A2").build());
        inventory.create(aSampleMo().withType("typeB").withName("B").build());

        // When
        InventoryFilter filterA = new InventoryFilter().byType("typeA");
        ManagedObjectCollectionRepresentation typeAMos = (ManagedObjectCollectionRepresentation) inventory.getManagedObjectsByFilter(filterA).get();

        // Then
        assertThat(typeAMos.getManagedObjects().size()).isEqualTo(2);

        // When
        InventoryFilter filterB = new InventoryFilter().byType("typeB");
        ManagedObjectCollectionRepresentation typeBMos = (ManagedObjectCollectionRepresentation) inventory.getManagedObjectsByFilter(filterB).get();

        // Then
        assertThat(typeBMos.getManagedObjects().size()).isEqualTo(1);
    }

    @Test
    public void bulkQuery() throws Exception {
        // Given
        ManagedObjectRepresentation mo1 = inventory.create(aSampleMo().withName("MO1").build());
        ManagedObjectRepresentation mo3 = inventory.create(aSampleMo().withName("MO3").build());
        inventory.create(aSampleMo().withName("MO2").build());

        // When
        List gidsList = new ArrayList();
        gidsList.add(mo3.getId());
        gidsList.add(mo1.getId());
        ManagedObjectCollectionRepresentation moCollection = (ManagedObjectCollectionRepresentation) inventory.getManagedObjectsByListOfIds(gidsList).get();

        // Then
        List mos = moCollection.getManagedObjects();
        assertThat(mos.size()).isEqualTo(2);
        assertThat(((ManagedObjectRepresentation) mos.get(0)).getName()).isEqualTo("MO3");
        assertThat(((ManagedObjectRepresentation) mos.get(1)).getName()).isEqualTo("MO1");

    }

    //
    @Test
    public void queryWithFragmentType() throws Exception {
        // Given
        inventory.create(aSampleMo().withName("MO1").with(new Coordinate()).build());
        inventory.create(aSampleMo().withName("MO2").with(new SecondFragment()).build());

        // When
        InventoryFilter filter = new InventoryFilter().byFragmentType(Coordinate.class);
        ManagedObjectCollectionRepresentation coordinates = (ManagedObjectCollectionRepresentation) inventory.getManagedObjectsByFilter(filter).get();

        // Then
        assertThat(coordinates.getManagedObjects().size()).isEqualTo(1);
    }

    @Test
    public void getAllWhen20ManagedObjectsPresent() throws Exception {
        // Given
        int numOfMos = 20;
        for (int i = 0; i < numOfMos; i++) {
            inventory.create(aSampleMo().withName("MO" + i).with(new Coordinate()).build());
        }

        // When
        ManagedObjectCollectionRepresentation mos = (ManagedObjectCollectionRepresentation) inventory.getManagedObjects().get();

        // Then
        assertThat(mos.getPageStatistics().getTotalPages()).isEqualTo(4);

        // When
        ManagedObjectCollectionRepresentation secondPage = (ManagedObjectCollectionRepresentation) inventory.getManagedObjects().getPage(mos, 2);

        // Then
        assertThat(secondPage.getPageStatistics().getCurrentPage()).isEqualTo(2);

        // When
        ManagedObjectCollectionRepresentation thirdPage = (ManagedObjectCollectionRepresentation) inventory.getManagedObjects().getNextPage(secondPage);

        // Then
        assertThat(thirdPage.getPageStatistics().getCurrentPage()).isEqualTo(3);

        // When
        ManagedObjectCollectionRepresentation firstPage = (ManagedObjectCollectionRepresentation) inventory.getManagedObjects().getPreviousPage(secondPage);
        assertThat(firstPage.getPageStatistics().getCurrentPage()).isEqualTo(1);
    }

    private void deleteMOs(List mosOn1stPage) throws SDKException {
        Iterator iterator = mosOn1stPage.iterator();
        while (iterator.hasNext()) {
        ManagedObjectRepresentation mo  = (ManagedObjectRepresentation) iterator.next();
            inventory.getManagedObject(mo.getId()).delete();
        }
    }

    private List getMOsFrom1stPage() throws SDKException {
        return ((ManagedObjectCollectionRepresentation) inventory.getManagedObjects().get()).getManagedObjects();
    }

    private static ManagedObjectRepresentationBuilder aSampleMo() {
        return anMoRepresentationLike(MO_REPRESENTATION);
    }

    private static <T> Set<T> asSet(T... items) {
        return new HashSet<T>(asList(items));
    }
}
