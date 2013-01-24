package com.cumulocity.sdk.client.measurement;

import static com.cumulocity.rest.representation.builder.RestRepresentationObjectMother.anMoRepresentationLike;
import static com.cumulocity.rest.representation.builder.SampleManagedObjectRepresentation.MO_REPRESENTATION;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import java.util.*;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.cumulocity.model.DateConverter;
import com.cumulocity.model.idtype.GId;
import com.cumulocity.rest.representation.builder.ManagedObjectRepresentationBuilder;
import com.cumulocity.rest.representation.inventory.ManagedObjectRepresentation;
import com.cumulocity.rest.representation.measurement.MeasurementCollectionRepresentation;
import com.cumulocity.rest.representation.measurement.MeasurementRepresentation;
import com.cumulocity.sdk.client.SDKException;
import com.cumulocity.sdk.client.common.JavaSdkITBase;

import cucumber.annotation.en.Given;
import cucumber.annotation.en.Then;
import cucumber.annotation.en.When;

//TODO inline step definitions (see AlarmIT or InventoryIT)
public class MeasurementIT extends JavaSdkITBase {

    private static List<ManagedObjectRepresentation> managedObjects = new ArrayList<ManagedObjectRepresentation>();

    @BeforeClass
    public static void createManagedObject() throws Exception {
//    Background:
//    Given I have a platform and a tenant
//    And I have '3' managed objects
//    And I create all
        for (int i = 0; i < 3; ++i) {
            ManagedObjectRepresentation mo = platform.getInventoryApi().create(aSampleMo().withName("MO" + i).build());
            managedObjects.add(mo);
        }
    }

    @Before
    public void setup() {
        measurementApi = platform.getMeasurementApi();
        input = new ArrayList<MeasurementRepresentation>();
        result1 = new ArrayList<MeasurementRepresentation>();
        result2 = new ArrayList<MeasurementRepresentation>();
        status = OK;
    }

    @After
    public void deleteManagedObjects() throws Exception {
        List<MeasurementRepresentation> measOn1stPage = getMeasurementsFrom1stPage();
        while (!measOn1stPage.isEmpty()) {
            deleteMeasurements(measOn1stPage);
            measOn1stPage = getMeasurementsFrom1stPage();
        }
    }

    private void deleteMeasurements(List<MeasurementRepresentation> measOn1stPage) throws SDKException {
        for (MeasurementRepresentation m : measOn1stPage) {
            measurementApi.deleteMeasurement(m);
        }
    }

    private List<MeasurementRepresentation> getMeasurementsFrom1stPage() throws SDKException {
        return measurementApi.getMeasurements().get().getMeasurements();
    }

    private static ManagedObjectRepresentationBuilder aSampleMo() {
        return anMoRepresentationLike(MO_REPRESENTATION);
    }

    //
//    Scenario: Create measurements
    @Test
    public void createMeasurements() throws Exception {
//    Given I have '2' measurements of type 'com.type1' for the managed object
        iHaveMeasurements(2, "com.type1");
//    When I create all measurements
        iCreateAll();
//    Then All measurements should be created
        allShouldBeCreated();
    }

//
//
//    Scenario: Create measurements without time

    @Test
    public void createMeasurementsWithoutTime() throws Exception {
//    Given I have a measurement of type 'com.type1' and no time value for the managed object
        iHaveAMeasurementWithNoTime("com.type1");
//    When I create all measurements
        iCreateAll();
//    Then Measurement response should be unprocessable
        shouldBeBadRequest();
    }

//
//
//    Scenario: Get measurement collection

    @Test
    public void getMeasurementCollection() throws Exception {
//    Given I have '2' measurements of type 'com.type1' for the managed object
        iHaveMeasurements(2, "com.type1");
//    When I create all measurements
        iCreateAll();
//    And I get all measurements
        iGetAllMeasurements();
//    Then I should I get all the measurements
        shouldGetAllMeasurements();
    }

//
//
//    Scenario: Get measurement collection by fragment type

    @Test
    public void getMeasurementCollectionByFragmentType() throws Exception {
//    Given I have '2' measurements with fragment type 'com.cumulocity.sdk.client.measurement.FragmentOne' for the managed object
        iHaveMeasurementsWithFragments(2, "com.cumulocity.sdk.client.measurement.FragmentOne");
//    And I have '3' measurements with fragment type 'com.cumulocity.sdk.client.measurement.FragmentTwo' for the managed object
        iHaveMeasurementsWithFragments(3, "com.cumulocity.sdk.client.measurement.FragmentTwo");
//    When I create all measurements
        iCreateAll();
//    And I get all measurements
        iGetAllMeasurements();
//    Then I should get '5' measurements
        iShouldGetNumberOfMeasurements(5);
//    And I query all measurements by fragment type 'com.cumulocity.sdk.client.measurement.FragmentOne'
        iQueryAllByType("com.cumulocity.sdk.client.measurement.FragmentOne");
//    Then I should get '2' measurements
        iShouldGetNumberOfMeasurements(2);
//    And I query all measurements by fragment type 'com.cumulocity.sdk.client.measurement.FragmentTwo'
        iQueryAllByType("com.cumulocity.sdk.client.measurement.FragmentTwo");
//    Then I should get '3' measurements
        iShouldGetNumberOfMeasurements(3);
//    And I query all measurements by fragment type 'com.cumulocity.sdk.client.measurement.FragmentThree'
        iQueryAllByType("com.cumulocity.sdk.client.measurement.FragmentThree");
//    Then I should get '0' measurements
        iShouldGetNumberOfMeasurements(0);
    }

//
//
//    Scenario: Get measurement collection by source

    @Test
    public void getMeasurementCollectionBySource() throws Exception {
//    Given I have '1' measurements for the source '0' the managed object
        iHaveMeasurementsForSource(1, 0);
//    And I have '2' measurements for the source '1' the managed object
        iHaveMeasurementsForSource(2, 1);
//    When I create all measurements
        iCreateAll();
//    And I get all measurements
        iGetAllMeasurements();
//    Then I should get '3' measurements
        iShouldGetNumberOfMeasurements(3);
//    And I query all measurements by source '0'
        iQueryAllBySource(0);
//    Then I should get '1' measurements
        iShouldGetNumberOfMeasurements(1);
//    And I query all measurements by source '1'
        iQueryAllBySource(1);
//    Then I should get '2' measurements
        iShouldGetNumberOfMeasurements(2);
//    And I query all measurements by source '2'
        iQueryAllBySource(2);
//    Then I should get '0' measurements
        iShouldGetNumberOfMeasurements(0);
    }

//
//    Scenario: Get measurement collection by time

    @Test
    public void getMeasurementCollectionByTime() throws Exception {
//    Given I have a measurement with time '2011-11-03T11:01:00.000+05:30' with fragment type 'com.cumulocity.sdk.client.measurement
// .FragmentOne' and for '0' managed object
        iHaveAMeasurementWithTypeAndTime("2011-11-03T11:01:00.000+05:30", "com.cumulocity.sdk.client.measurement.FragmentOne", 0);
//    And I have a measurement with time '2011-11-03T11:05:00.000+05:30' with fragment type 'com.cumulocity.sdk.client.measurement
// .FragmentOne' and for '0' managed object
        iHaveAMeasurementWithTypeAndTime("2011-11-03T11:05:00.000+05:30", "com.cumulocity.sdk.client.measurement.FragmentOne", 0);
//    When I create all measurements
        iCreateAll();
//    And I get all measurements
        iGetAllMeasurements();
//    Then I should get '2' measurements
        iShouldGetNumberOfMeasurements(2);
//    And I query all measurements by time from '2011-11-03T11:00:00.000+05:30' and time to '2011-11-03T11:10:00.000+05:30'
        iQueryAllByTime("2011-11-03T11:00:00.000+05:30", "2011-11-03T11:10:00.000+05:30");
//    Then I should get '2' measurements
        iShouldGetNumberOfMeasurements(2);
//    And I query all measurements by time from '2011-11-03T10:00:00.000+05:30' and time to '2011-11-03T11:00:00.000+05:30'
        iQueryAllByTime("2011-11-03T10:00:00.000+05:30", "2011-11-03T11:00:00.000+05:30");
//    Then I should get '0' measurements
        iShouldGetNumberOfMeasurements(0);

    }

//
//    Scenario: Get measurement collection by source and time

    @Test
    public void getMeasurementCollectionBySourceAndTime() throws Exception {
//    Given I have a measurement with time '2011-11-03T11:01:00.000+05:30' with fragment type 'com.cumulocity.sdk.client.measurement
// .FragmentOne' and for '0' managed object
        iHaveAMeasurementWithTypeAndTime("2011-11-03T11:01:00.000+05:30", "com.cumulocity.sdk.client.measurement.FragmentOne", 0);
//    And I have a measurement with time '2011-11-03T11:05:00.000+05:30' with fragment type 'com.cumulocity.sdk.client.measurement
// .FragmentOne' and for '1' managed object
        iHaveAMeasurementWithTypeAndTime("2011-11-03T11:05:00.000+05:30", "com.cumulocity.sdk.client.measurement.FragmentOne", 1);
//    When I create all measurements
        iCreateAll();
//    And I get all measurements
        iGetAllMeasurements();
//    Then I should get '2' measurements
        iShouldGetNumberOfMeasurements(2);
//    And I query all measurements by source '0' and time from '2011-11-03T11:00:00.000+05:30' and time to '2011-11-03T11:10:00.000+05:30'
        iQueryAllBySourceAndTime(0, "2011-11-03T11:00:00.000+05:30", "2011-11-03T11:10:00.000+05:30");
//    Then I should get '1' measurements
        iShouldGetNumberOfMeasurements(1);
//    And I query all measurements by source '1' and time from '2011-11-03T11:00:00.000+05:30' and time to '2011-11-03T11:10:00.000+05:30'
        iQueryAllBySourceAndTime(1, "2011-11-03T11:00:00.000+05:30", "2011-11-03T11:10:00.000+05:30");
//    Then I should get '1' measurements
        iShouldGetNumberOfMeasurements(1);
//    And I query all measurements by source '0' and time from '2011-11-03T10:00:00.000+05:30' and time to '2011-11-03T11:00:00.000+05:30'
        iQueryAllBySourceAndTime(0, "2011-11-03T10:00:00.000+05:30", "2011-11-03T11:00:00.000+05:30");
//    Then I should get '0' measurements
        iShouldGetNumberOfMeasurements(0);
//    And I query all measurements by source '1' and time from '2011-11-03T10:00:00.000+05:30' and time to '2011-11-03T11:00:00.000+05:30'
        iQueryAllBySourceAndTime(1, "2011-11-03T10:00:00.000+05:30", "2011-11-03T11:00:00.000+05:30");
//    Then I should get '0' measurements
        iShouldGetNumberOfMeasurements(0);
    }

//
//    Scenario: Get measurement collection by source and fragment type

    @Test
    public void getMeasurementCollectionBySourceAndFragmentType() throws Exception {
//    Given I have a measurement with time '2011-11-03T11:01:00.000+05:30' with fragment type 'com.cumulocity.sdk.client.measurement
// .FragmentOne' and for '0' managed object
        iHaveAMeasurementWithTypeAndTime("2011-11-03T11:01:00.000+05:30", "com.cumulocity.sdk.client.measurement.FragmentOne", 0);
//    And I have a measurement with time '2011-11-03T11:05:00.000+05:30' with fragment type 'com.cumulocity.sdk.client.measurement
// .FragmentOne' and for '1' managed object
        iHaveAMeasurementWithTypeAndTime("2011-11-03T11:05:00.000+05:30", "com.cumulocity.sdk.client.measurement.FragmentOne", 1);
//    When I create all measurements
        iCreateAll();
//    And I get all measurements
        iGetAllMeasurements();
//    Then I should get '2' measurements
        iShouldGetNumberOfMeasurements(2);
//    And I query all measurements by source '0' and fragment type 'com.cumulocity.sdk.client.measurement.FragmentOne'
        iQueryAllBySourceAndType(0, "com.cumulocity.sdk.client.measurement.FragmentOne");
//    Then I should get '1' measurements
        iShouldGetNumberOfMeasurements(1);
//    And I query all measurements by source '1' and fragment type 'com.cumulocity.sdk.client.measurement.FragmentOne'
        iQueryAllBySourceAndType(1, "com.cumulocity.sdk.client.measurement.FragmentOne");
//    Then I should get '1' measurements
        iShouldGetNumberOfMeasurements(1);
//    And I query all measurements by source '0' and fragment type 'com.cumulocity.sdk.client.measurement.FragmentTwo'
        iQueryAllBySourceAndType(0, "com.cumulocity.sdk.client.measurement.FragmentTwo");
//    Then I should get '0' measurements
        iShouldGetNumberOfMeasurements(0);
//    And I query all measurements by source '0' and fragment type 'com.cumulocity.sdk.client.measurement.FragmentTwo'
        iQueryAllBySourceAndType(0, "com.cumulocity.sdk.client.measurement.FragmentTwo");
//    Then I should get '0' measurements
        iShouldGetNumberOfMeasurements(0);
    }

//
//
//    Scenario: Get measurement collection by fragment type and time

    @Test
    public void getMeasurementCollectionByFragmentTypeAndTime() throws Exception {
//    Given I have a measurement with time '2011-11-03T11:01:00.000+05:30' with fragment type 'com.cumulocity.sdk.client.measurement
// .FragmentOne' and for '0' managed object
        iHaveAMeasurementWithTypeAndTime("2011-11-03T11:01:00.000+05:30", "com.cumulocity.sdk.client.measurement.FragmentOne", 0);
//    And I have a measurement with time '2011-11-03T11:05:00.000+05:30' with fragment type 'com.cumulocity.sdk.client.measurement
// .FragmentOne' and for '1' managed object
        iHaveAMeasurementWithTypeAndTime("2011-11-03T11:05:00.000+05:30", "com.cumulocity.sdk.client.measurement.FragmentOne", 1);
//    When I create all measurements
        iCreateAll();
//    And I get all measurements
        iGetAllMeasurements();
//    Then I should get '2' measurements
        iShouldGetNumberOfMeasurements(2);
//    And I query all measurements by fragment type 'com.cumulocity.sdk.client.measurement.FragmentOne' and time from '2011-11-03T11:00:00
// .000+05:30' and time to '2011-11-03T11:10:00.000+05:30'
        iQueryAllByTypeAndTime("com.cumulocity.sdk.client.measurement.FragmentOne", "2011-11-03T11:00:00.000+05:30",
                "2011-11-03T11:10:00.000+05:30");
//    Then I should get '2' measurements
        iShouldGetNumberOfMeasurements(2);
//    And I query all measurements by fragment type 'com.cumulocity.sdk.client.measurement.FragmentTwo' and time from '2011-11-03T11:00:00
// .000+05:30' and time to '2011-11-03T11:10:00.000+05:30'
        iQueryAllByTypeAndTime("com.cumulocity.sdk.client.measurement.FragmentTwo", "2011-11-03T11:00:00.000+05:30",
                "2011-11-03T11:10:00.000+05:30");
//    Then I should get '0' measurements
        iShouldGetNumberOfMeasurements(0);
//    And I query all measurements by fragment type 'com.cumulocity.sdk.client.measurement.FragmentOne' and time from '2011-11-03T10:00:00
// .000+05:30' and time to '2011-11-03T11:00:00.000+05:30'
        iQueryAllByTypeAndTime("com.cumulocity.sdk.client.measurement.FragmentOne", "2011-11-03T10:00:00.000+05:30",
                "2011-11-03T11:00:00.000+05:30");
//    Then I should get '0' measurements
        iShouldGetNumberOfMeasurements(0);

    }

//
//
//    Scenario: Get measurement collection by source and fragment type and time

    @Test
    public void getMeasurementCollectionBySourceAndFragmentTypeAndTime() throws Exception {
//    Given I have a measurement with time '2011-11-03T11:01:00.000+05:30' with fragment type 'com.cumulocity.sdk.client.measurement
// .FragmentOne' and for '0' managed object
        iHaveAMeasurementWithTypeAndTime("2011-11-03T11:01:00.000+05:30", "com.cumulocity.sdk.client.measurement.FragmentOne", 0);
//    And I have a measurement with time '2011-11-03T11:05:00.000+05:30' with fragment type 'com.cumulocity.sdk.client.measurement
// .FragmentOne' and for '1' managed object
        iHaveAMeasurementWithTypeAndTime("2011-11-03T11:05:00.000+05:30", "com.cumulocity.sdk.client.measurement.FragmentOne", 1);
//    When I create all measurements
        iCreateAll();
//    And I get all measurements
        iGetAllMeasurements();
//    Then I should get '2' measurements
        iShouldGetNumberOfMeasurements(2);
//    And I query all measurements by source '0' and fragment type 'com.cumulocity.sdk.client.measurement.FragmentOne' and time from
// '2011-11-03T11:00:00.000+05:30' and time to '2011-11-03T11:10:00.000+05:30'
        iQueryAllBySourceTypeAndTime(0, "com.cumulocity.sdk.client.measurement.FragmentOne", "2011-11-03T11:00:00.000+05:30",
                "2011-11-03T11:10:00.000+05:30");
//    Then I should get '1' measurements
        iShouldGetNumberOfMeasurements(1);
//    And I query all measurements by source '0' and fragment type 'com.cumulocity.sdk.client.measurement.FragmentTwo' and time from
// '2011-11-03T11:00:00.000+05:30' and time to '2011-11-03T11:10:00.000+05:30'
        iQueryAllBySourceTypeAndTime(0, "com.cumulocity.sdk.client.measurement.FragmentTwo", "2011-11-03T11:00:00.000+05:30",
                "2011-11-03T11:10:00.000+05:30");
//    Then I should get '0' measurements
        iShouldGetNumberOfMeasurements(0);
//    And I query all measurements by source '0' and fragment type 'com.cumulocity.sdk.client.measurement.FragmentOne' and time from
// '2011-11-03T10:00:00.000+05:30' and time to '2011-11-03T11:00:00.000+05:30'
        iQueryAllBySourceTypeAndTime(0, "com.cumulocity.sdk.client.measurement.FragmentOne", "2011-11-03T10:00:00.000+05:30",
                "2011-11-03T11:00:00.000+05:30");
//    Then I should get '0' measurements
        iShouldGetNumberOfMeasurements(0);
//    And I query all measurements by source '0' and fragment type 'com.cumulocity.sdk.client.measurement.FragmentTwo' and time from
// '2011-11-03T10:00:00.000+05:30' and time to '2011-11-03T11:00:00.000+05:30'
        iQueryAllBySourceTypeAndTime(0, "com.cumulocity.sdk.client.measurement.FragmentTwo", "2011-11-03T10:00:00.000+05:30",
                "2011-11-03T11:00:00.000+05:30");
//    Then I should get '0' measurements
        iShouldGetNumberOfMeasurements(0);
//    And I query all measurements by source '1' and fragment type 'com.cumulocity.sdk.client.measurement.FragmentOne' and time from
// '2011-11-03T11:00:00.000+05:30' and time to '2011-11-03T11:10:00.000+05:30'
        iQueryAllBySourceTypeAndTime(1, "com.cumulocity.sdk.client.measurement.FragmentOne", "2011-11-03T11:00:00.000+05:30",
                "2011-11-03T11:10:00.000+05:30");
//    Then I should get '1' measurements
        iShouldGetNumberOfMeasurements(1);
//    And I query all measurements by source '1' and fragment type 'com.cumulocity.sdk.client.measurement.FragmentTwo' and time from
// '2011-11-03T11:00:00.000+05:30' and time to '2011-11-03T11:10:00.000+05:30'
        iQueryAllBySourceTypeAndTime(1, "com.cumulocity.sdk.client.measurement.FragmentTwo", "2011-11-03T11:00:00.000+05:30",
                "2011-11-03T11:10:00.000+05:30");
//    Then I should get '0' measurements
        iShouldGetNumberOfMeasurements(0);
//    And I query all measurements by source '1' and fragment type 'com.cumulocity.sdk.client.measurement.FragmentOne' and time from
// '2011-11-03T10:00:00.000+05:30' and time to '2011-11-03T11:00:00.000+05:30'
        iQueryAllBySourceTypeAndTime(1, "com.cumulocity.sdk.client.measurement.FragmentOne", "2011-11-03T10:00:00.000+05:30",
                "2011-11-03T11:00:00.000+05:30");
//    Then I should get '0' measurements
        iShouldGetNumberOfMeasurements(0);
//    And I query all measurements by source '1' and fragment type 'com.cumulocity.sdk.client.measurement.FragmentTwo' and time from
// '2011-11-03T10:00:00.000+05:30' and time to '2011-11-03T11:00:00.000+05:30'
        iQueryAllBySourceTypeAndTime(1, "com.cumulocity.sdk.client.measurement.FragmentTwo", "2011-11-03T10:00:00.000+05:30",
                "2011-11-03T11:00:00.000+05:30");
//    Then I should get '0' measurements
        iShouldGetNumberOfMeasurements(0);
//    And I query all measurements by source '2' and fragment type 'com.cumulocity.sdk.client.measurement.FragmentOne' and time from
// '2011-11-03T11:00:00.000+05:30' and time to '2011-11-03T11:10:00.000+05:30'
        iQueryAllBySourceTypeAndTime(2, "com.cumulocity.sdk.client.measurement.FragmentOne", "2011-11-03T11:00:00.000+05:30",
                "2011-11-03T11:10:00.000+05:30");
//    Then I should get '0' measurements
        iShouldGetNumberOfMeasurements(0);
//    And I query all measurements by source '2' and fragment type 'com.cumulocity.sdk.client.measurement.FragmentTwo' and time from
// '2011-11-03T11:00:00.000+05:30' and time to '2011-11-03T11:10:00.000+05:30'
        iQueryAllBySourceTypeAndTime(2, "com.cumulocity.sdk.client.measurement.FragmentTwo", "2011-11-03T11:00:00.000+05:30",
                "2011-11-03T11:10:00.000+05:30");
//    Then I should get '0' measurements
        iShouldGetNumberOfMeasurements(0);
//    And I query all measurements by source '2' and fragment type 'com.cumulocity.sdk.client.measurement.FragmentOne' and time from
// '2011-11-03T10:00:00.000+05:30' and time to '2011-11-03T11:00:00.000+05:30'
        iQueryAllBySourceTypeAndTime(2, "com.cumulocity.sdk.client.measurement.FragmentOne", "2011-11-03T10:00:00.000+05:30",
                "2011-11-03T11:00:00.000+05:30");
//    Then I should get '0' measurements
        iShouldGetNumberOfMeasurements(0);
//    And I query all measurements by source '2' and fragment type 'com.cumulocity.sdk.client.measurement.FragmentTwo' and time from
// '2011-11-03T10:00:00.000+05:30' and time to '2011-11-03T11:00:00.000+05:30'
        iQueryAllBySourceTypeAndTime(2, "com.cumulocity.sdk.client.measurement.FragmentTwo", "2011-11-03T10:00:00.000+05:30",
                "2011-11-03T11:00:00.000+05:30");
//    Then I should get '0' measurements
        iShouldGetNumberOfMeasurements(0);
    }

//
//    Scenario: Get measurement

    @Test
    public void getMeasurement() throws Exception {
//    Given I have a measurement with time '2011-11-03T11:01:00.000+05:30' with fragment type 'com.cumulocity.sdk.client.measurement
// .FragmentOne' and for '0' managed object
        iHaveAMeasurementWithTypeAndTime("2011-11-03T11:01:00.000+05:30", "com.cumulocity.sdk.client.measurement.FragmentOne", 0);
//    When I create all measurements
        iCreateAll();
//    And I get the measurement with the created id
        iGetMeasurementWithCreatedId();
//    Then I should get the measurement
        shouldGetTheMeasurement();
    }

//
//    Scenario: Delete measurement

    @Test
    public void deleteMeasurement() throws Exception {
//    Given I have a measurement with time '2011-11-03T11:01:00.000+05:30' with fragment type 'com.cumulocity.sdk.client.measurement
// .FragmentOne' and for '0' managed object
        iHaveAMeasurementWithTypeAndTime("2011-11-03T11:01:00.000+05:30", "com.cumulocity.sdk.client.measurement.FragmentOne", 0);
//    When I create all measurements
        iCreateAll();
//    And I delete the measurement with the created id
        iDeleteMeasurementWithCreatedId();
//    And I get the measurement with the created id
        iGetMeasurementWithCreatedId();
//    Then Measurement should not be found
        shouldNotBeFound();
    }

//
//    Scenario: Get measurements collection by default page settings

    @Test
    public void getMeasurementCollectionByDefaultPageSettings() throws Exception {
        // Given
        for (int i = 0; i < 12; i++) {
            MeasurementRepresentation rep = aSampleMeasurement(managedObjects.get(0));
            measurementApi.create(rep);
        }

        // When
        MeasurementCollectionRepresentation measurements = measurementApi.getMeasurements().get();

        // Then
        assertThat(measurements.getMeasurements().size(), is(equalTo(5)));

        // When
        MeasurementCollectionRepresentation page1st = measurementApi.getMeasurements().getPage(measurements, 1);

        // Then
        assertThat(page1st.getMeasurements().size(), is(equalTo(5)));

        // When
        MeasurementCollectionRepresentation page2nd = measurementApi.getMeasurements().getPage(measurements, 2);

        // Then
        assertThat(page2nd.getMeasurements().size(), is(equalTo(5)));

        // When
        MeasurementCollectionRepresentation page3rd = measurementApi.getMeasurements().getPage(measurements, 3);

        // Then
        assertThat(page3rd.getMeasurements().size(), is(equalTo(2)));

        // When
        MeasurementCollectionRepresentation page4th = measurementApi.getMeasurements().getPage(measurements, 4);

        // Then
        assertThat(page4th.getMeasurements().size(), is(equalTo(0)));
    }

    private MeasurementRepresentation aSampleMeasurement(ManagedObjectRepresentation source) {
        MeasurementRepresentation rep = new MeasurementRepresentation();
        rep.setTime(new Date());
        rep.setType("com.type1");
        rep.setSource(source);

        rep.set(new FragmentOne());
        return rep;
    }

//
//    Scenario: Get measurements next and previous collection

    @Test
    public void getMeasurementNextAndPreviousCollection() throws Exception {
//    Given I have '12' measurements with fragment type 'com.cumulocity.sdk.client.measurement.FragmentOne' for the managed object
        iHaveMeasurementsWithFragments(12, "com.cumulocity.sdk.client.measurement.FragmentOne");
//    When I create all measurements
        iCreateAll();
//    And I get all measurements
        iGetAllMeasurements();
//    Then I should get '5' measurements
        iShouldGetNumberOfMeasurements(5);
//    And I query all measurements by page '2'
        iQueryAllByPageNumber(2);
//    Then I should get '5' measurements of paging
        iShouldGetNumberOfMeasurements(5);
//    Then I should get next page which has current page '3' and measurements '2'
        iQueryAllByNextPage(3, 2);
//    Then I should get previous page which has current page '1' and measurements '5'
        iQueryAllByPreviousPage(1, 5);
    }

//

    private static final int OK = 200;

    private static final int NOT_FOUND = 404;

    private static final int UNPROCESSABLE = 422;

    private List<MeasurementRepresentation> input;

    private List<MeasurementRepresentation> result1;

    private List<MeasurementRepresentation> result2;

    private MeasurementCollectionRepresentation collection1;

    private MeasurementCollectionRepresentation collection2;

    private MeasurementApi measurementApi;

    private int status;

    // ------------------------------------------------------------------------
    // Given
    // ------------------------------------------------------------------------

    @Given("I have '(\\d+)' measurements of type '([^']*)' for the managed object")
    public void iHaveMeasurements(int n, String type) {
        for (int i = 0; i < n; i++) {
            MeasurementRepresentation rep = new MeasurementRepresentation();
            rep.setType(type);
            rep.setTime(new Date());
            rep.setSource(managedObjects.get(0));
            input.add(rep);
        }
    }

    @Given("I have '(\\d+)' measurements with fragment type '([^']*)' for the managed object")
    public void iHaveMeasurementsWithFragments(int n, String fragmentType)
            throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        for (int i = 0; i < n; i++) {
            MeasurementRepresentation rep = new MeasurementRepresentation();
            rep.setTime(new Date());
            rep.setType("com.type1");
            rep.setSource(managedObjects.get(0));

            // Set fragment
            Class<?> cls = Class.forName(fragmentType);
            Object fragment = cls.newInstance();
            rep.set(fragment);

            input.add(rep);
        }
    }

    @Given("I have '(\\d+)' measurements for the source '(\\d+)' the managed object")
    public void iHaveMeasurementsForSource(int n, int index) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        for (int i = 0; i < n; i++) {
            MeasurementRepresentation rep = new MeasurementRepresentation();
            rep.setTime(new Date());
            rep.setType("com.type1");
            rep.setSource(managedObjects.get(index));
            input.add(rep);
        }
    }

    @Given("I have a measurement of type '([^']*)' and no time value for the managed object")
    public void iHaveAMeasurementWithNoTime(String type) {
        MeasurementRepresentation rep = new MeasurementRepresentation();
        rep.setType(type);
        rep.setSource(managedObjects.get(0));
        input.add(rep);
    }

    @Given("I have a measurement with time '([^']*)' with fragment type '([^']*)' and for '(\\d+)' managed object")
    public void iHaveAMeasurementWithTypeAndTime(String time, String fragmentType, int index)
            throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        MeasurementRepresentation rep = new MeasurementRepresentation();
        rep.setType("com.type1");
        rep.setTime(DateConverter.string2Date(time));
        rep.setSource(managedObjects.get(index));

        // Set fragment
        Class<?> cls = Class.forName(fragmentType);
        Object fragment = cls.newInstance();
        rep.set(fragment);

        input.add(rep);
    }

    // ------------------------------------------------------------------------
    // When
    // ------------------------------------------------------------------------

    @When("I create all measurements")
    public void iCreateAll() throws SDKException {
        try {
            for (MeasurementRepresentation rep : input) {
                result1.add(measurementApi.create(rep));
            }
        } catch (SDKException ex) {
            status = ex.getHttpStatus();
        }
    }

    @When("I get all measurements")
    public void iGetAllMeasurements() throws SDKException {
        try {
            collection1 = measurementApi.getMeasurements().get();
        } catch (SDKException ex) {
            status = ex.getHttpStatus();
        }
    }

    @When("I get the measurement with the created id")
    public void iGetMeasurementWithCreatedId() throws SDKException {
        try {
            result2.add(measurementApi.getMeasurement(result1.get(0).getId()));
        } catch (SDKException ex) {
            status = ex.getHttpStatus();
        }
    }

    @When("I delete the measurement with the created id")
    public void iDeleteMeasurementWithCreatedId() throws SDKException {
        try {
            measurementApi.deleteMeasurement(result1.get(0));
        } catch (SDKException ex) {
            status = ex.getHttpStatus();
        }
    }

    @When("I query all measurements by fragment type '([^']*)'")
    public void iQueryAllByType(String fragmentType) throws SDKException, ClassNotFoundException {
        try {
            Class<?> fragmentClass = Class.forName(fragmentType);
            MeasurementFilter filter = new MeasurementFilter().byFragmentType(fragmentClass);
            collection1 = measurementApi.getMeasurementsByFilter(filter).get();
        } catch (SDKException ex) {
            status = ex.getHttpStatus();
        }
    }

    @When("I query all measurements by time from '([^']*)' and time to '([^']*)'")
    public void iQueryAllByTime(String from, String to) throws SDKException {
        try {
            Date fromDate = DateConverter.string2Date(from);
            Date toDate = DateConverter.string2Date(to);
            MeasurementFilter filter = new MeasurementFilter().byDate(fromDate, toDate);
            collection1 = measurementApi.getMeasurementsByFilter(filter).get();
        } catch (SDKException ex) {
            status = ex.getHttpStatus();
        }
    }

    @When("I query all measurements by source '(\\d+)'")
    public void iQueryAllBySource(int index) throws SDKException {
        try {
            ManagedObjectRepresentation source = managedObjects.get(index);
            MeasurementFilter filter = new MeasurementFilter().bySource(source);
            collection1 = measurementApi.getMeasurementsByFilter(filter).get();
        } catch (SDKException ex) {
            status = ex.getHttpStatus();
        }
    }

    @When("I query all measurements by source '(\\d+)' and time from '([^']*)' and time to '([^']*)'")
    public void iQueryAllBySourceAndTime(int index, String from, String to) throws SDKException {
        try {
            ManagedObjectRepresentation source = managedObjects.get(index);
            Date fromDate = DateConverter.string2Date(from);
            Date toDate = DateConverter.string2Date(to);
            MeasurementFilter filter = new MeasurementFilter().byDate(fromDate, toDate).bySource(source);
            collection1 = measurementApi.getMeasurementsByFilter(filter).get();
        } catch (SDKException ex) {
            status = ex.getHttpStatus();
        }
    }

    @When("I query all measurements by source '(\\d+)' and fragment type '([^']*)'")
    public void iQueryAllBySourceAndType(int index, String fragmentType) throws SDKException, ClassNotFoundException {
        try {
            Class<?> fragmentClass = Class.forName(fragmentType);
            ManagedObjectRepresentation source = managedObjects.get(index);
            MeasurementFilter filter = new MeasurementFilter().byFragmentType(fragmentClass).bySource(source);
            collection1 = measurementApi.getMeasurementsByFilter(filter).get();
        } catch (SDKException ex) {
            status = ex.getHttpStatus();
        }
    }

    @When("I query all measurements by fragment type '([^']*)' and time from '([^']*)' and time to '([^']*)'")
    public void iQueryAllByTypeAndTime(String fragmentType, String from, String to) throws SDKException, ClassNotFoundException {
        try {
            Class<?> fragmentClass = Class.forName(fragmentType);
            Date fromDate = DateConverter.string2Date(from);
            Date toDate = DateConverter.string2Date(to);
            MeasurementFilter filter = new MeasurementFilter().byDate(fromDate, toDate).byFragmentType(fragmentClass);
            collection1 = measurementApi.getMeasurementsByFilter(filter).get();
        } catch (SDKException ex) {
            status = ex.getHttpStatus();
        }
    }

    @When("I query all measurements by source '(\\d+)' and fragment type '([^']*)' and time from '([^']*)' and time to '([^']*)'")
    public void iQueryAllBySourceTypeAndTime(int index, String fragmentType, String from, String to)
            throws SDKException, ClassNotFoundException {
        try {
            Class<?> fragmentClass = Class.forName(fragmentType);
            ManagedObjectRepresentation source = managedObjects.get(index);
            Date fromDate = DateConverter.string2Date(from);
            Date toDate = DateConverter.string2Date(to);
            MeasurementFilter filter = new MeasurementFilter().bySource(source).byDate(fromDate, toDate).byFragmentType(fragmentClass);
            collection1 = measurementApi.getMeasurementsByFilter(filter).get();
        } catch (SDKException ex) {
            status = ex.getHttpStatus();
        }
    }

    @When("I query all measurements by page '(\\d+)'")
    public void iQueryAllByPageNumber(int pageNumber) throws SDKException {
        try {
            collection2 = measurementApi.getMeasurements().getPage(collection1, pageNumber);
        } catch (SDKException ex) {
            status = ex.getHttpStatus();
        }
    }

    // ------------------------------------------------------------------------
    // Then
    // ------------------------------------------------------------------------

    @Then("I should get next page which has current page '(\\d+)' and measurements '(\\d+)'")
    public void iQueryAllByNextPage(int currentPage, int numMeasurements) throws SDKException {
        try {
            MeasurementCollectionRepresentation collectionRepresentation = measurementApi.getMeasurements().getNextPage(collection2);
            assertThat(currentPage, is(equalTo(collectionRepresentation.getPageStatistics().getCurrentPage())));
            assertThat(numMeasurements, is(equalTo(collectionRepresentation.getMeasurements().size())));
        } catch (SDKException ex) {
            status = ex.getHttpStatus();
        }

    }

    @Then("I should get previous page which has current page '(\\d+)' and measurements '(\\d+)'")
    public void iQueryAllByPreviousPage(int currentPage, int numMeasurements) throws SDKException {
        try {
            MeasurementCollectionRepresentation collectionRepresentation = measurementApi.getMeasurements().getPreviousPage(collection2);
            assertThat(currentPage, is(equalTo(collectionRepresentation.getPageStatistics().getCurrentPage())));
            assertThat(numMeasurements, is(equalTo(collectionRepresentation.getMeasurements().size())));
        } catch (SDKException ex) {
            status = ex.getHttpStatus();
        }

    }

    @Then("I query measurements by previous page")
    public void iQueryAllByPreviousPage() throws SDKException {
        try {
            collection2 = measurementApi.getMeasurements().getPreviousPage(collection2);
        } catch (SDKException ex) {
            status = ex.getHttpStatus();
        } catch (NullPointerException ex) {
            ex.printStackTrace();
        }
    }

    @Then("I should get '(\\d+)' measurements of paging")
    public void iShouldGetNumberOfEventsOfPaging(int count) {
        assertThat(collection2.getMeasurements().size(), is(count));
    }

    @Then("All measurements should be created")
    public void allShouldBeCreated() {
        assertThat(result1.size(), is(equalTo(input.size())));
        for (MeasurementRepresentation rep : result1) {
            assertThat(rep.getId(), is(notNullValue()));
        }
    }

    @Then("I should get '(\\d+)' measurements")
    public void iShouldGetNumberOfMeasurements(int count) {
        assertThat(collection1.getMeasurements().size(), is(equalTo(count)));
    }

    @Then("Measurement response should be unprocessable")
    public void shouldBeBadRequest() {
        assertThat(status, is(UNPROCESSABLE));
    }

    @Then("I should get the measurement")
    public void shouldGetTheMeasurement() {
        assertThat(result1.get(0).getId(), is(equalTo(result2.get(0).getId())));
    }

    @Then("I should I get all the measurements")
    public void shouldGetAllMeasurements() {
        assertThat(collection1.getMeasurements().size(), is(equalTo(result1.size())));

        Map<GId, MeasurementRepresentation> map = new HashMap<GId, MeasurementRepresentation>();

        for (MeasurementRepresentation rep : result1) {
            map.put(rep.getId(), rep);
        }

        for (MeasurementRepresentation rep : collection1.getMeasurements()) {
            MeasurementRepresentation orig = map.get(rep.getId());
            assertThat(orig, is(notNullValue()));
        }
    }

    @Then("Measurement should not be found")
    public void shouldNotBeFound() {
        assertThat(status, is(equalTo(NOT_FOUND)));
    }

    // ------------------------------------------------------------------------
    // Private Methods
    // ------------------------------------------------------------------------

}

