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
package com.cumulocity.sdk.client.measurement;

import static com.cumulocity.me.rest.representation.RestRepresentationObjectMother.anMoRepresentationLike;
import static com.cumulocity.me.rest.representation.SampleManagedObjectRepresentation.MO_REPRESENTATION;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

import java.util.ArrayList;
import java.util.Date;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.cumulocity.me.lang.HashMap;
import com.cumulocity.me.lang.Iterator;
import com.cumulocity.me.lang.List;
import com.cumulocity.me.lang.Map;
import com.cumulocity.me.rest.convert.measurement.FragmentOneConverter;
import com.cumulocity.me.rest.convert.measurement.FragmentThreeConverter;
import com.cumulocity.me.rest.convert.measurement.FragmentTwoConverter;
import com.cumulocity.me.rest.representation.inventory.ManagedObjectRepresentation;
import com.cumulocity.me.rest.representation.inventory.ManagedObjectRepresentationBuilder;
import com.cumulocity.me.rest.representation.measurement.FragmentOne;
import com.cumulocity.me.rest.representation.measurement.MeasurementCollectionRepresentation;
import com.cumulocity.me.rest.representation.measurement.MeasurementRepresentation;
import com.cumulocity.me.sdk.SDKException;
import com.cumulocity.me.sdk.client.measurement.MeasurementApi;
import com.cumulocity.me.sdk.client.measurement.MeasurementFilter;
import com.cumulocity.model.DateConverter;
import com.cumulocity.sdk.client.common.JavaSdkITBase;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class MeasurementIT extends JavaSdkITBase {

    private static java.util.List<ManagedObjectRepresentation> managedObjects = new ArrayList<ManagedObjectRepresentation>();

    @BeforeClass
    public static void createManagedObject() throws Exception {
        FragmentOneConverter oneConverter = new FragmentOneConverter();
        platform.getConversionService().register(oneConverter);
        platform.getValidationService().register(oneConverter);
        
        FragmentTwoConverter twoConverter = new FragmentTwoConverter();
        platform.getConversionService().register(twoConverter);
        platform.getValidationService().register(twoConverter);
        
        FragmentThreeConverter threeConverter = new FragmentThreeConverter();
        platform.getConversionService().register(threeConverter);
        platform.getValidationService().register(threeConverter);
        
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
        platform.getConversionService().register(new FragmentOneConverter());
        input = new ArrayList<MeasurementRepresentation>();
        result1 = new ArrayList<MeasurementRepresentation>();
        result2 = new ArrayList<MeasurementRepresentation>();
        status = OK;
    }

    @After
    public void deleteManagedObjects() throws Exception {
        List measOn1stPage = getMeasurementsFrom1stPage();
        while (!measOn1stPage.isEmpty()) {
            deleteMeasurements(measOn1stPage);
            measOn1stPage = getMeasurementsFrom1stPage();
        }
    }

    private void deleteMeasurements(List measOn1stPage) throws SDKException {
        Iterator iterator = measOn1stPage.iterator();
        while (iterator.hasNext()) {
            measurementApi.deleteMeasurement((MeasurementRepresentation) iterator.next());
        }
    }

    private List getMeasurementsFrom1stPage() throws SDKException {
        return ((MeasurementCollectionRepresentation) measurementApi.getMeasurements().get()).getMeasurements();
    }

    private static ManagedObjectRepresentationBuilder aSampleMo() {
        return anMoRepresentationLike(MO_REPRESENTATION);
    }

    @Test
    public void createMeasurements() throws Exception {
        iHaveMeasurements(2, "com.type1");

        iCreateAll();

        allShouldBeCreated();
    }

    @Test
    public void createMeasurementsWithoutTime() throws Exception {
        iHaveAMeasurementWithNoTime("com.type1");

        iCreateAll();

        shouldBeBadRequest();
    }

    @Test
    public void getMeasurementCollection() throws Exception {
        iHaveMeasurements(2, "com.type1");
        iCreateAll();

        iGetAllMeasurements();

        shouldGetAllMeasurements();
    }

    @Test
    public void getMeasurementCollectionByFragmentType() throws Exception {
        iHaveMeasurementsWithFragments(2, "com.cumulocity.me.rest.representation.measurement.FragmentOne");
        iHaveMeasurementsWithFragments(3, "com.cumulocity.me.rest.representation.measurement.FragmentTwo");

        iCreateAll();
        iGetAllMeasurements();

        iShouldGetNumberOfMeasurements(5);
        iQueryAllByType("com.cumulocity.me.rest.representation.measurement.FragmentOne");
        iShouldGetNumberOfMeasurements(2);
        iQueryAllByType("com.cumulocity.me.rest.representation.measurement.FragmentTwo");
        iShouldGetNumberOfMeasurements(3);
        iQueryAllByType("com.cumulocity.me.rest.representation.measurement.FragmentThree");
        iShouldGetNumberOfMeasurements(0);
    }

    @Test
    public void getMeasurementCollectionBySource() throws Exception {
        iHaveMeasurementsForSource(1, 0);
        iHaveMeasurementsForSource(2, 1);

        iCreateAll();
        iGetAllMeasurements();

        iShouldGetNumberOfMeasurements(3);
        iQueryAllBySource(0);
        iShouldGetNumberOfMeasurements(1);
        iQueryAllBySource(1);
        iShouldGetNumberOfMeasurements(2);
        iQueryAllBySource(2);
        iShouldGetNumberOfMeasurements(0);
    }

    @Test
    public void getMeasurementCollectionByTime() throws Exception {
        iHaveAMeasurementWithTypeAndTime("2011-11-03T11:01:00.000+05:30", "com.cumulocity.me.rest.representation.measurement.FragmentOne", 0);
        iHaveAMeasurementWithTypeAndTime("2011-11-03T11:05:00.000+05:30", "com.cumulocity.me.rest.representation.measurement.FragmentOne", 0);

        iCreateAll();
        iGetAllMeasurements();

        iShouldGetNumberOfMeasurements(2);
        iQueryAllByTime("2011-11-03T11:00:00.000+05:30", "2011-11-03T11:10:00.000+05:30");
        iShouldGetNumberOfMeasurements(2);
        iQueryAllByTime("2011-11-03T10:00:00.000+05:30", "2011-11-03T11:00:00.000+05:30");
        iShouldGetNumberOfMeasurements(0);
    }

    @Test
    public void getMeasurementCollectionBySourceAndTime() throws Exception {
        iHaveAMeasurementWithTypeAndTime("2011-11-03T11:01:00.000+05:30", "com.cumulocity.me.rest.representation.measurement.FragmentOne", 0);
        iHaveAMeasurementWithTypeAndTime("2011-11-03T11:05:00.000+05:30", "com.cumulocity.me.rest.representation.measurement.FragmentOne", 1);

        iCreateAll();
        iGetAllMeasurements();

        iShouldGetNumberOfMeasurements(2);
        iQueryAllBySourceAndTime(0, "2011-11-03T11:00:00.000+05:30", "2011-11-03T11:10:00.000+05:30");

        iShouldGetNumberOfMeasurements(1);
        iQueryAllBySourceAndTime(1, "2011-11-03T11:00:00.000+05:30", "2011-11-03T11:10:00.000+05:30");
        iShouldGetNumberOfMeasurements(1);
        iQueryAllBySourceAndTime(0, "2011-11-03T10:00:00.000+05:30", "2011-11-03T11:00:00.000+05:30");
        iShouldGetNumberOfMeasurements(0);
        iQueryAllBySourceAndTime(1, "2011-11-03T10:00:00.000+05:30", "2011-11-03T11:00:00.000+05:30");
        iShouldGetNumberOfMeasurements(0);
    }

    @Test
    public void getMeasurementCollectionBySourceAndFragmentType() throws Exception {
        iHaveAMeasurementWithTypeAndTime("2011-11-03T11:01:00.000+05:30", "com.cumulocity.me.rest.representation.measurement.FragmentOne", 0);
        iHaveAMeasurementWithTypeAndTime("2011-11-03T11:05:00.000+05:30", "com.cumulocity.me.rest.representation.measurement.FragmentOne", 1);

        iCreateAll();
        iGetAllMeasurements();

        iShouldGetNumberOfMeasurements(2);
        iQueryAllBySourceAndType(0, "com.cumulocity.me.rest.representation.measurement.FragmentOne");
        iShouldGetNumberOfMeasurements(1);
        iQueryAllBySourceAndType(1, "com.cumulocity.me.rest.representation.measurement.FragmentOne");
        iShouldGetNumberOfMeasurements(1);
        iQueryAllBySourceAndType(0, "com.cumulocity.me.rest.representation.measurement.FragmentTwo");
        iShouldGetNumberOfMeasurements(0);
        iQueryAllBySourceAndType(0, "com.cumulocity.me.rest.representation.measurement.FragmentTwo");
        iShouldGetNumberOfMeasurements(0);
    }

    @Test
    public void getMeasurementCollectionByFragmentTypeAndTime() throws Exception {
        iHaveAMeasurementWithTypeAndTime("2011-11-03T11:01:00.000+05:30", "com.cumulocity.me.rest.representation.measurement.FragmentOne", 0);
        iHaveAMeasurementWithTypeAndTime("2011-11-03T11:05:00.000+05:30", "com.cumulocity.me.rest.representation.measurement.FragmentOne", 1);

        iCreateAll();
        iGetAllMeasurements();

        iShouldGetNumberOfMeasurements(2);
        iQueryAllByTypeAndTime("com.cumulocity.me.rest.representation.measurement.FragmentOne", "2011-11-03T11:00:00.000+05:30",
                "2011-11-03T11:10:00.000+05:30");
        iShouldGetNumberOfMeasurements(2);
        iQueryAllByTypeAndTime("com.cumulocity.me.rest.representation.measurement.FragmentTwo", "2011-11-03T11:00:00.000+05:30",
                "2011-11-03T11:10:00.000+05:30");
        iShouldGetNumberOfMeasurements(0);
        iQueryAllByTypeAndTime("com.cumulocity.me.rest.representation.measurement.FragmentOne", "2011-11-03T10:00:00.000+05:30",
                "2011-11-03T11:00:00.000+05:30");
        iShouldGetNumberOfMeasurements(0);
    }

    @Test
    public void getMeasurementCollectionBySourceAndFragmentTypeAndTime() throws Exception {
        iHaveAMeasurementWithTypeAndTime("2011-11-03T11:01:00.000+05:30", "com.cumulocity.me.rest.representation.measurement.FragmentOne", 0);
        iHaveAMeasurementWithTypeAndTime("2011-11-03T11:05:00.000+05:30", "com.cumulocity.me.rest.representation.measurement.FragmentOne", 1);

        iCreateAll();
        iGetAllMeasurements();

        iShouldGetNumberOfMeasurements(2);
        iQueryAllBySourceTypeAndTime(0, "com.cumulocity.me.rest.representation.measurement.FragmentOne", "2011-11-03T11:00:00.000+05:30",
                "2011-11-03T11:10:00.000+05:30");
        iShouldGetNumberOfMeasurements(1);
        iQueryAllBySourceTypeAndTime(0, "com.cumulocity.me.rest.representation.measurement.FragmentTwo", "2011-11-03T11:00:00.000+05:30",
                "2011-11-03T11:10:00.000+05:30");
        iShouldGetNumberOfMeasurements(0);
        iQueryAllBySourceTypeAndTime(0, "com.cumulocity.me.rest.representation.measurement.FragmentOne", "2011-11-03T10:00:00.000+05:30",
                "2011-11-03T11:00:00.000+05:30");
        iShouldGetNumberOfMeasurements(0);
        iQueryAllBySourceTypeAndTime(0, "com.cumulocity.me.rest.representation.measurement.FragmentTwo", "2011-11-03T10:00:00.000+05:30",
                "2011-11-03T11:00:00.000+05:30");
        iShouldGetNumberOfMeasurements(0);
        iQueryAllBySourceTypeAndTime(1, "com.cumulocity.me.rest.representation.measurement.FragmentOne", "2011-11-03T11:00:00.000+05:30",
                "2011-11-03T11:10:00.000+05:30");
        iShouldGetNumberOfMeasurements(1);
        iQueryAllBySourceTypeAndTime(1, "com.cumulocity.me.rest.representation.measurement.FragmentTwo", "2011-11-03T11:00:00.000+05:30",
                "2011-11-03T11:10:00.000+05:30");
        iShouldGetNumberOfMeasurements(0);
        iQueryAllBySourceTypeAndTime(1, "com.cumulocity.me.rest.representation.measurement.FragmentOne", "2011-11-03T10:00:00.000+05:30",
                "2011-11-03T11:00:00.000+05:30");
        iShouldGetNumberOfMeasurements(0);
        iQueryAllBySourceTypeAndTime(1, "com.cumulocity.me.rest.representation.measurement.FragmentTwo", "2011-11-03T10:00:00.000+05:30",
                "2011-11-03T11:00:00.000+05:30");
        iShouldGetNumberOfMeasurements(0);
        iQueryAllBySourceTypeAndTime(2, "com.cumulocity.me.rest.representation.measurement.FragmentOne", "2011-11-03T11:00:00.000+05:30",
                "2011-11-03T11:10:00.000+05:30");
        iShouldGetNumberOfMeasurements(0);
        iQueryAllBySourceTypeAndTime(2, "com.cumulocity.me.rest.representation.measurement.FragmentTwo", "2011-11-03T11:00:00.000+05:30",
                "2011-11-03T11:10:00.000+05:30");
        iShouldGetNumberOfMeasurements(0);
        iQueryAllBySourceTypeAndTime(2, "com.cumulocity.me.rest.representation.measurement.FragmentOne", "2011-11-03T10:00:00.000+05:30",
                "2011-11-03T11:00:00.000+05:30");
        iShouldGetNumberOfMeasurements(0);
        iQueryAllBySourceTypeAndTime(2, "com.cumulocity.me.rest.representation.measurement.FragmentTwo", "2011-11-03T10:00:00.000+05:30",
                "2011-11-03T11:00:00.000+05:30");
        iShouldGetNumberOfMeasurements(0);
    }

    @Test
    public void getMeasurement() throws Exception {
        iHaveAMeasurementWithTypeAndTime("2011-11-03T11:01:00.000+05:30", "com.cumulocity.me.rest.representation.measurement.FragmentOne", 0);

        iCreateAll();
        iGetMeasurementWithCreatedId();

        shouldGetTheMeasurement();
    }

    @Test
    public void deleteMeasurement() throws Exception {
        iHaveAMeasurementWithTypeAndTime("2011-11-03T11:01:00.000+05:30", "com.cumulocity.me.rest.representation.measurement.FragmentOne", 0);

        iCreateAll();
        iDeleteMeasurementWithCreatedId();
        iGetMeasurementWithCreatedId();

        shouldNotBeFound();
    }

    @Test
    public void getMeasurementCollectionByDefaultPageSettings() throws Exception {
        for (int i = 0; i < 12; i++) {
            MeasurementRepresentation rep = aSampleMeasurement((ManagedObjectRepresentation) managedObjects.get(0));
            measurementApi.create(rep);
        }

        MeasurementCollectionRepresentation measurements = (MeasurementCollectionRepresentation) measurementApi.getMeasurements().get();
        MeasurementCollectionRepresentation page1st = (MeasurementCollectionRepresentation) measurementApi.getMeasurements().getPage(
                measurements, 1);
        MeasurementCollectionRepresentation page2nd = (MeasurementCollectionRepresentation) measurementApi.getMeasurements().getPage(
                measurements, 2);
        MeasurementCollectionRepresentation page3rd = (MeasurementCollectionRepresentation) measurementApi.getMeasurements().getPage(
                measurements, 3);
        MeasurementCollectionRepresentation page4th = (MeasurementCollectionRepresentation) measurementApi.getMeasurements().getPage(
                measurements, 4);

        assertThat(measurements.getMeasurements().size(), is(equalTo(5)));
        assertThat(page1st.getMeasurements().size(), is(equalTo(5)));
        assertThat(page2nd.getMeasurements().size(), is(equalTo(5)));
        assertThat(page3rd.getMeasurements().size(), is(equalTo(2)));
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

    @Test
    public void getMeasurementNextAndPreviousCollection() throws Exception {
        iHaveMeasurementsWithFragments(12, "com.cumulocity.me.rest.representation.measurement.FragmentOne");

        iCreateAll();
        iGetAllMeasurements();

        iShouldGetNumberOfMeasurements(5);
        iQueryAllByPageNumber(2);
        iShouldGetNumberOfMeasurements(5);
        iQueryAllByNextPage(3, 2);
        iQueryAllByPreviousPage(1, 5);
    }

    private static final int OK = 200;

    private static final int NOT_FOUND = 404;

    private static final int UNPROCESSABLE = 422;

    private java.util.List<MeasurementRepresentation> input;

    private java.util.List<MeasurementRepresentation> result1;

    private java.util.List<MeasurementRepresentation> result2;

    private MeasurementCollectionRepresentation collection1;

    private MeasurementCollectionRepresentation collection2;

    private MeasurementApi measurementApi;

    private int status;

    @Given("I have '(\\d+)' measurements of type '([^']*)' for the managed object")
    public void iHaveMeasurements(int n, String type) {
        for (int i = 0; i < n; i++) {
            MeasurementRepresentation rep = new MeasurementRepresentation();
            rep.setType(type);
            rep.setTime(new Date());
            rep.setSource((ManagedObjectRepresentation) managedObjects.get(0));
            input.add(rep);
        }
    }

    @Given("I have '(\\d+)' measurements with fragment type '([^']*)' for the managed object")
    public void iHaveMeasurementsWithFragments(int n, String fragmentType) throws ClassNotFoundException, InstantiationException,
            IllegalAccessException {
        for (int i = 0; i < n; i++) {
            MeasurementRepresentation rep = new MeasurementRepresentation();
            rep.setTime(new Date());
            rep.setType("com.type1");
            rep.setSource((ManagedObjectRepresentation) managedObjects.get(0));

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
            rep.setSource((ManagedObjectRepresentation) managedObjects.get(index));
            input.add(rep);
        }
    }

    @Given("I have a measurement of type '([^']*)' and no time value for the managed object")
    public void iHaveAMeasurementWithNoTime(String type) {
        MeasurementRepresentation rep = new MeasurementRepresentation();
        rep.setType(type);
        rep.setSource((ManagedObjectRepresentation) managedObjects.get(0));
        input.add(rep);
    }

    @Given("I have a measurement with time '([^']*)' with fragment type '([^']*)' and for '(\\d+)' managed object")
    public void iHaveAMeasurementWithTypeAndTime(String time, String fragmentType, int index) throws ClassNotFoundException,
            InstantiationException, IllegalAccessException {
        MeasurementRepresentation rep = new MeasurementRepresentation();
        rep.setType("com.type1");
        rep.setTime(DateConverter.string2Date(time).toDate());
        rep.setSource((ManagedObjectRepresentation) managedObjects.get(index));

        // Set fragment
        Class<?> cls = Class.forName(fragmentType);
        Object fragment = cls.newInstance();
        rep.set(fragment);

        input.add(rep);
    }

    @When("I create all measurements")
    public void iCreateAll() throws SDKException {
        try {
            for (MeasurementRepresentation representation : input) {
                result1.add(measurementApi.create(representation));
            }
        } catch (SDKException ex) {
            status = ex.getHttpStatus();
        }
    }

    @When("I get all measurements")
    public void iGetAllMeasurements() throws SDKException {
        try {
            collection1 = (MeasurementCollectionRepresentation) measurementApi.getMeasurements().get();
        } catch (SDKException ex) {
            status = ex.getHttpStatus();
        }
    }

    @When("I get the measurement with the created id")
    public void iGetMeasurementWithCreatedId() throws SDKException {
        try {
            result2.add(measurementApi.getMeasurement(((MeasurementRepresentation) result1.get(0)).getId()));
        } catch (SDKException ex) {
            status = ex.getHttpStatus();
        }
    }

    @When("I delete the measurement with the created id")
    public void iDeleteMeasurementWithCreatedId() throws SDKException {
        try {
            measurementApi.deleteMeasurement((MeasurementRepresentation) result1.get(0));
        } catch (SDKException ex) {
            status = ex.getHttpStatus();
        }
    }

    @When("I query all measurements by fragment type '([^']*)'")
    public void iQueryAllByType(String fragmentType) throws SDKException, ClassNotFoundException {
        try {
            Class<?> fragmentClass = Class.forName(fragmentType);
            MeasurementFilter filter = new MeasurementFilter().byFragmentType(fragmentClass);
            collection1 = (MeasurementCollectionRepresentation) measurementApi.getMeasurementsByFilter(filter).get();
        } catch (SDKException ex) {
            status = ex.getHttpStatus();
        }
    }

    @When("I query all measurements by time from '([^']*)' and time to '([^']*)'")
    public void iQueryAllByTime(String from, String to) throws SDKException {
        try {
            Date fromDate = DateConverter.string2Date(from).toDate();
            Date toDate = DateConverter.string2Date(to).toDate();
            MeasurementFilter filter = new MeasurementFilter().byDate(fromDate, toDate);
            collection1 = (MeasurementCollectionRepresentation) measurementApi.getMeasurementsByFilter(filter).get();
        } catch (SDKException ex) {
            status = ex.getHttpStatus();
        }
    }

    @When("I query all measurements by source '(\\d+)'")
    public void iQueryAllBySource(int index) throws SDKException {
        try {
            ManagedObjectRepresentation source = (ManagedObjectRepresentation) managedObjects.get(index);
            MeasurementFilter filter = new MeasurementFilter().bySource(source);
            collection1 = (MeasurementCollectionRepresentation) measurementApi.getMeasurementsByFilter(filter).get();
        } catch (SDKException ex) {
            status = ex.getHttpStatus();
        }
    }

    @When("I query all measurements by source '(\\d+)' and time from '([^']*)' and time to '([^']*)'")
    public void iQueryAllBySourceAndTime(int index, String from, String to) throws SDKException {
        try {
            ManagedObjectRepresentation source = (ManagedObjectRepresentation) managedObjects.get(index);
            Date fromDate = DateConverter.string2Date(from).toDate();
            Date toDate = DateConverter.string2Date(to).toDate();
            MeasurementFilter filter = new MeasurementFilter().byDate(fromDate, toDate).bySource(source);
            collection1 = (MeasurementCollectionRepresentation) measurementApi.getMeasurementsByFilter(filter).get();
        } catch (SDKException ex) {
            status = ex.getHttpStatus();
        }
    }

    @When("I query all measurements by source '(\\d+)' and fragment type '([^']*)'")
    public void iQueryAllBySourceAndType(int index, String fragmentType) throws SDKException, ClassNotFoundException {
        try {
            Class<?> fragmentClass = Class.forName(fragmentType);
            ManagedObjectRepresentation source = (ManagedObjectRepresentation) managedObjects.get(index);
            MeasurementFilter filter = new MeasurementFilter().byFragmentType(fragmentClass).bySource(source);
            collection1 = (MeasurementCollectionRepresentation) measurementApi.getMeasurementsByFilter(filter).get();
        } catch (SDKException ex) {
            status = ex.getHttpStatus();
        }
    }

    @When("I query all measurements by fragment type '([^']*)' and time from '([^']*)' and time to '([^']*)'")
    public void iQueryAllByTypeAndTime(String fragmentType, String from, String to) throws SDKException, ClassNotFoundException {
        try {
            Class<?> fragmentClass = Class.forName(fragmentType);
            Date fromDate = DateConverter.string2Date(from).toDate();
            Date toDate = DateConverter.string2Date(to).toDate();
            MeasurementFilter filter = new MeasurementFilter().byDate(fromDate, toDate).byFragmentType(fragmentClass);
            collection1 = (MeasurementCollectionRepresentation) measurementApi.getMeasurementsByFilter(filter).get();
        } catch (SDKException ex) {
            status = ex.getHttpStatus();
        }
    }

    @When("I query all measurements by source '(\\d+)' and fragment type '([^']*)' and time from '([^']*)' and time to '([^']*)'")
    public void iQueryAllBySourceTypeAndTime(int index, String fragmentType, String from, String to) throws SDKException,
            ClassNotFoundException {
        try {
            Class<?> fragmentClass = Class.forName(fragmentType);
            ManagedObjectRepresentation source = (ManagedObjectRepresentation) managedObjects.get(index);
            Date fromDate = DateConverter.string2Date(from).toDate();
            Date toDate = DateConverter.string2Date(to).toDate();
            MeasurementFilter filter = new MeasurementFilter().bySource(source).byDate(fromDate, toDate).byFragmentType(fragmentClass);
            collection1 = (MeasurementCollectionRepresentation) measurementApi.getMeasurementsByFilter(filter).get();
        } catch (SDKException ex) {
            status = ex.getHttpStatus();
        }
    }

    @When("I query all measurements by page '(\\d+)'")
    public void iQueryAllByPageNumber(int pageNumber) throws SDKException {
        try {
            collection2 = (MeasurementCollectionRepresentation) measurementApi.getMeasurements().getPage(collection1, pageNumber);
        } catch (SDKException ex) {
            status = ex.getHttpStatus();
        }
    }

    @Then("I should get next page which has current page '(\\d+)' and measurements '(\\d+)'")
    public void iQueryAllByNextPage(int currentPage, int numMeasurements) throws SDKException {
        try {
            MeasurementCollectionRepresentation collectionRepresentation = (MeasurementCollectionRepresentation) measurementApi
                    .getMeasurements().getNextPage(collection2);
            assertThat(currentPage, is(equalTo(collectionRepresentation.getPageStatistics().getCurrentPage())));
            assertThat(numMeasurements, is(equalTo(collectionRepresentation.getMeasurements().size())));
        } catch (SDKException ex) {
            status = ex.getHttpStatus();
        }

    }

    @Then("I should get previous page which has current page '(\\d+)' and measurements '(\\d+)'")
    public void iQueryAllByPreviousPage(int currentPage, int numMeasurements) throws SDKException {
        try {
            MeasurementCollectionRepresentation collectionRepresentation = (MeasurementCollectionRepresentation) measurementApi
                    .getMeasurements().getPreviousPage(collection2);
            assertThat(currentPage, is(equalTo(collectionRepresentation.getPageStatistics().getCurrentPage())));
            assertThat(numMeasurements, is(equalTo(collectionRepresentation.getMeasurements().size())));
        } catch (SDKException ex) {
            status = ex.getHttpStatus();
        }

    }

    @Then("I query measurements by previous page")
    public void iQueryAllByPreviousPage() throws SDKException {
        try {
            collection2 = (MeasurementCollectionRepresentation) measurementApi.getMeasurements().getPreviousPage(collection2);
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
        for (MeasurementRepresentation representation : result1) {
            assertThat(representation.getId(), is(notNullValue()));
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
        assertThat(((MeasurementRepresentation) result1.get(0)).getId(), is(equalTo(((MeasurementRepresentation) result2.get(0)).getId())));
    }

    @Then("I should I get all the measurements")
    public void shouldGetAllMeasurements() {
        assertThat(collection1.getMeasurements().size(), is(equalTo(result1.size())));

        Map map = new HashMap();

        for (MeasurementRepresentation rep : result1) {
            map.put(rep.getId(), rep);
        }

        Iterator iterator = collection1.getMeasurements().iterator();
        while (iterator.hasNext()) {
            MeasurementRepresentation orig = (MeasurementRepresentation) map.get(((MeasurementRepresentation) iterator.next()).getId());
            assertThat(orig, is(notNullValue()));
        }
    }

    @Then("Measurement should not be found")
    public void shouldNotBeFound() {
        assertThat(status, is(equalTo(NOT_FOUND)));
    }
}
