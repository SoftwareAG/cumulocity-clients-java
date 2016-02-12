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
package com.cumulocity.sdk.client.event;

import static com.cumulocity.model.util.DateTimeUtils.nowLocal;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.cumulocity.model.DateConverter;
import com.cumulocity.rest.representation.event.EventCollectionRepresentation;
import com.cumulocity.rest.representation.event.EventRepresentation;
import com.cumulocity.rest.representation.inventory.ManagedObjectRepresentation;
import com.cumulocity.sdk.client.SDKException;
import com.cumulocity.sdk.client.common.JavaSdkITBase;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

//TODO inline step definitions (see AlarmIT or InventoryIT)
public class EventIT extends JavaSdkITBase {

    private static ManagedObjectRepresentation managedObject;

    @BeforeClass
    public static void createManagedObject() throws Exception {
        ManagedObjectRepresentation mo = new ManagedObjectRepresentation();
        mo.setName("MO");

        managedObject = platform.getInventoryApi().create(mo);
    }

    @Before
    public void setup() throws Exception {
        eventApi = platform.getEventApi();
        input = new ArrayList<EventRepresentation>();
        result = new ArrayList<EventRepresentation>();
        result1 = new ArrayList<EventRepresentation>();
        status = OK;
    }

    @After
    public void deleteEvents() throws Exception {
        List<EventRepresentation> eventsOn1stPage = getEventsFrom1stPage();
        while (!eventsOn1stPage.isEmpty()) {
            deleteMOs(eventsOn1stPage);
            eventsOn1stPage = getEventsFrom1stPage();
        }
    }

    private void deleteMOs(List<EventRepresentation> mosOn1stPage) throws SDKException {
        for (EventRepresentation e : mosOn1stPage) {
            eventApi.delete(e);
        }
    }

    private List<EventRepresentation> getEventsFrom1stPage() throws SDKException {
        return eventApi.getEvents().get().getEvents();
    }

    //    Scenario: Create Events
    @Test
    public void createEvents() throws Exception {
//    Given I have '2' Events of type 'type1' for the managed object
        iHaveEvents(2, "type1");
//    When I create all Events
        iCreateAll();
//    And I get all Events
        iGetAllEvents();
//    Then I should get '2' Events
        iShouldGetNumberOfEvents(2);
    }

    //    Scenario: Create Events without type
    @Test
    public void createEventsWithoutType() throws Exception {
//    Given I have a Event with no type value for the managed object
        iHaveAEventWithNoType();
//    When I create all Events
        iCreateAll();
//    Then Event response should be unprocessable
        shouldBeBadRequest();
    }

    //
//
//    Scenario: Create Events without time

    @Test
    public void createEventsWithoutTime() throws Exception {
//    Given I have a Event with no time value for the managed object
        iHaveAEventWithNoTime();
//    When I create all Events
        iCreateAll();
//    Then Event response should be unprocessable
        shouldBeBadRequest();
    }

//
//    Scenario: Create Events without text

    @Test
    public void createEventsWithoutText() throws Exception {
//    Given I have a Event with no text value for the managed object
        iHaveAEventWithNoText();
//    When I create all Events
        iCreateAll();
//    Then Event response should be unprocessable
        shouldBeBadRequest();
    }

//
//    Scenario: Get Event collection

    @Test
    public void getEventCollection() throws Exception {
//    Given I have '2' Events of type 'type1' for the managed object
        iHaveEvents(2, "type1");
//    When I create all Events
        iCreateAll();
//    And I get all Events
        iGetAllEvents();
//    Then I should get all the Events
        shouldGetAllEvents();
    }

//
//
//    Scenario: Get event collection by type

    @Test
    public void getEventCollectionByType() throws Exception {
//    Given I have '2' Events of type 'type' for the managed object
        iHaveEvents(2, "type");
//    And I have '3' Events of type 'type1' for the managed object
        iHaveEvents(3, "type1");
//    When I create all Events
        iCreateAll();
//    And I get all Events
        iGetAllEvents();
//    Then I should get '5' Events
        iShouldGetNumberOfEvents(5);
//    And I query all Events by type 'type'
        iQueryAllByType("type");
//    Then I should get '2' Events
        iShouldGetNumberOfEvents(2);
//    And I query all Events by type 'type1'
        iQueryAllByType("type1");
//    Then I should get '3' Events
        iShouldGetNumberOfEvents(3);
//    And I query all Events by type 'type2'
        iQueryAllByType("type2");
//    Then I should get '0' Events
        iShouldGetNumberOfEvents(0);
    }

//
//
//    Scenario: Get event collection by source

    @Test
    public void getEventCollectionBySource() throws Exception {
//    Given I have '3' Events for the source '0' the managed object
        iHaveEventsForSource(3, 0);
//    When I create all Events
        iCreateAll();
//    And I get all Events
        iGetAllEvents();
//    Then I should get '3' Events
        iShouldGetNumberOfEvents(3);
//    And I query all Events by source '0'
        iQueryAllBySource(0);
//    Then I should get '3' Events
        iShouldGetNumberOfEvents(3);

    }

//
//    Scenario: Get event collection by source and type

    @Test
    public void getEventCollectionbySourceAndType() throws Exception {
//    Given I have a Event with time '2011-11-03T11:01:00.000+05:30' with type 'type' and for '0' managed object
        iHaveAEventWithTypeAndTime("2011-11-03T11:01:00.000+05:30", "type", 0);
//    And I have a Event with time '2011-11-03T11:05:00.000+05:30' with type 'type1' and for '0' managed object
        iHaveAEventWithTypeAndTime("2011-11-03T11:05:00.000+05:30", "type1", 0);
//    When I create all Events
        iCreateAll();
//    And I get all Events
        iGetAllEvents();
//    Then I should get '2' Events
        iShouldGetNumberOfEvents(2);
//    And I query all Events by source '0' and type 'type'
        iQueryAllBySourceAndType(0, "type");
//    Then I should get '1' Events
        iShouldGetNumberOfEvents(1);
//    And I query all Events by source '0' and type 'type1'
        iQueryAllBySourceAndType(0, "type1");
//    Then I should get '1' Events
        iShouldGetNumberOfEvents(1);
//    And I query all Events by source '0' and type 'type2'
        iQueryAllBySourceAndType(0, "type2");
//    Then I should get '0' Events
        iShouldGetNumberOfEvents(0);
    }
    
    
    @Test
    public void getEventCollectionbyTime() throws Exception {
//    Given I have a Event with time '2011-11-03T11:01:00.000+05:30' with type 'type' and for '0' managed object
        iHaveAEventWithTypeAndTime("2011-11-03T11:01:00.000+05:30", "type", 0);
//    And I have a Event with time '2011-11-03T11:05:00.000+05:30' with type 'type1' and for '0' managed object
        iHaveAEventWithTypeAndTime("2011-11-03T11:05:00.000+05:30", "type1", 0);
//    When I create all Events
        iCreateAll();
//    And I get all Events
        iGetAllEvents();
//    Then I should get '2' Events
        iShouldGetNumberOfEvents(2);
//    And I query all Events by source '0' and type 'type'
        iQueryAllBySourceAndTime(0, "2011-11-03T11:01:00.000+05:30");
//    Then I should get '1' Events
        iShouldGetNumberOfEvents(1);

    }

//
//    Scenario: Get Event

    @Test
    public void getEvent() throws Exception {
//    Given I have a Event with time '2011-11-03T11:01:00.000+05:30' with type 'type' and for '0' managed object
        iHaveAEventWithTypeAndTime("2011-11-03T11:01:00.000+05:30", "type", 0);
//    When I create all Events
        iCreateAll();
//    And I get the Events with the created id
        iGetEventsWithCreatedId();
//    Then I should get the Events
        shouldGetTheEvent();
    }

//
//    Scenario: Delete Event

    @Test
    public void deleteEvent() throws Exception {
//    Given I have a Event with time '2011-11-03T11:01:00.000+05:30' with type 'type' and for '0' managed object
        iHaveAEventWithTypeAndTime("2011-11-03T11:01:00.000+05:30", "type", 0);
//    When I create all Events
        iCreateAll();
//    And I delete the Events with the created id
        iDeleteEventsWithCreatedId();
//    And I get the Events with the created id
        iGetEventsWithCreatedId();
//    Then Events should not be found
        shouldNotBeFound();
    }

//
//    Scenario: Get event collection by paging

    @Test
    public void getEventCollectionByPaging() throws Exception {
//    Given I have '17' Events for the source '0' the managed object
        iHaveEventsForSource(17, 0);
//    When I create all Events
        iCreateAll();
//    And I get all Events
        iGetAllEvents();
//    Then I should get '5' Events
        iShouldGetNumberOfEvents(5);
//    And I query all Events by page '1'
        iQueryAllByPageNumber(1);
//    Then I should get '5' Events of paging
        iShouldGetNumberOfEventsOfPaging(5);
//    And I query all Events by page '2'
        iQueryAllByPageNumber(2);
//    Then I should get '5' Events of paging
        iShouldGetNumberOfEventsOfPaging(5);
//    And I query all Events by page '3'
        iQueryAllByPageNumber(3);
//    Then I should get '5' Events of paging
        iShouldGetNumberOfEventsOfPaging(5);
//    Then I should get next page which has current page '4' and events '2'
        iQueryAllByNextPage(4, 2);
//    Then I should get previous page which has current page '2' and events '5'
        iQueryAllByPreviousPage(2, 5);
//    And I query all Events by page '4'
        iQueryAllByPageNumber(4);
//    Then I should get '2' Events of paging
        iShouldGetNumberOfEventsOfPaging(2);
//    And I query all Events by page '5'
        iQueryAllByPageNumber(5);
//    Then I should get '0' Events of paging
        iShouldGetNumberOfEventsOfPaging(0);
//    Then I should get previous page which has current page '4' and events '2'
        iQueryAllByPreviousPage(4, 2);
    }

    private static final int OK = 200;

    private static final int NOT_FOUND = 404;

    private static final int UNPROCESSABLE = 422;

    private List<EventRepresentation> input;

    private List<EventRepresentation> result;

    private List<EventRepresentation> result1;

    private EventCollectionRepresentation collection;

    private EventCollectionRepresentation collection1;

    private EventApi eventApi;

    private int status;

    // ------------------------------------------------------------------------
    // Given
    // ------------------------------------------------------------------------

    @Given("I have '(\\d+)' Events of type '([^']*)' for the managed object$")
    public void iHaveEvents(int n, String type) {
        for (int i = 0; i < n; i++) {
            EventRepresentation rep = new EventRepresentation();
            rep.setType(type);
            rep.setTime(nowLocal());
            rep.setText(" Event of Managed Object : " + i);
            rep.setSource(managedObject);
            input.add(rep);

        }
    }

    @Given("I have '(\\d+)' Events with type '([^']*)' for the managed object$")
    public void iHaveEventsWithFragments(int n, String type) throws ClassNotFoundException, InstantiationException,
            IllegalAccessException {
        for (int i = 0; i < n; i++) {
            EventRepresentation rep = new EventRepresentation();
            rep.setType(type);
            rep.setTime(nowLocal());
            rep.setText(" Event of Managed Object : " + i);
            rep.setSource(managedObject);
            input.add(rep);
        }
    }

    @Given("I have a Event with no type value for the managed object$")
    public void iHaveAEventWithNoType() {
        EventRepresentation rep = new EventRepresentation();
        rep.setTime(nowLocal());
        rep.setText(" Event of Managed Object : " + 0);
        rep.setSource(managedObject);
        input.add(rep);
    }

    @Given("I have a Event with no text value for the managed object$")
    public void iHaveAEventWithNoText() {
        EventRepresentation rep = new EventRepresentation();
        rep.setType("type");
        rep.setTime(nowLocal());
        rep.setSource(managedObject);
        input.add(rep);
    }

    @Given("I have a Event with no time value for the managed object$")
    public void iHaveAEventWithNoTime() {
        EventRepresentation rep = new EventRepresentation();
        rep.setType("type");
        rep.setText(" Event of Managed Object : " + 1);
        rep.setSource(managedObject);
        input.add(rep);
    }

    @Given("I have '(\\d+)' Events for the source '(\\d+)' the managed object$")
    public void iHaveEventsForSource(int n, int index) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        for (int i = 0; i < n; i++) {
            EventRepresentation rep = new EventRepresentation();
            rep.setType("type");
            rep.setTime(nowLocal());
            rep.setText(" Event of Managed Object : " + i);
            rep.setSource(managedObject);
            input.add(rep);
        }
    }

    @Given("I have a Event with time '([^']*)' with type '([^']*)' and for '(\\d+)' managed object$")
    public void iHaveAEventWithTypeAndTime(String time, String type, int index) throws ClassNotFoundException, InstantiationException,
            IllegalAccessException {
        EventRepresentation rep = new EventRepresentation();
        rep.setType(type);
        rep.setText(" Event of Managed Object : ");
        rep.setTime(DateConverter.string2Date(time));
        rep.setSource(managedObject);
        input.add(rep);
    }

    // ------------------------------------------------------------------------
    // When
    // ------------------------------------------------------------------------

    @When("I create all Events$")
    public void iCreateAll() throws SDKException {
        try {
            for (EventRepresentation rep : input) {
                result.add(eventApi.create(rep));
            }
        } catch (SDKException ex) {
            status = ex.getHttpStatus();
        }
    }

    @When("I get all Events$")
    public void iGetAllEvents() throws SDKException {
        try {
            collection = eventApi.getEvents().get();
        } catch (SDKException ex) {
            status = ex.getHttpStatus();
        }
    }

    @When("I query all Events by type '([^']*)'$")
    public void iQueryAllByType(String type) throws SDKException, ClassNotFoundException {
        try {
            EventFilter filter = new EventFilter().byType(type);
            collection = eventApi.getEventsByFilter(filter).get();
        } catch (SDKException ex) {
            status = ex.getHttpStatus();
        }
    }

    @When("I query all Events by source '(\\d+)'$")
    public void iQueryAllBySource(int index) throws SDKException {
        try {
            ManagedObjectRepresentation mo = managedObject;
            EventFilter filter = new EventFilter().bySource(mo);
            collection = eventApi.getEventsByFilter(filter).get();
        } catch (SDKException ex) {
            status = ex.getHttpStatus();
        }
    }

    @When("I query all Events by source '(\\d+)' and type '([^']*)'$")
    public void iQueryAllBySourceAndType(int index, String type) throws SDKException, ClassNotFoundException {
        try {
            ManagedObjectRepresentation mo = managedObject;
            EventFilter filter = new EventFilter().bySource(mo).byType(type);
            collection = eventApi.getEventsByFilter(filter).get();
        } catch (SDKException ex) {
            status = ex.getHttpStatus();
        }
    }
    
    @When("I query all Events by source '(\\d+)' and time '([^']*)'$")
    public void iQueryAllBySourceAndTime(int index, String time) throws SDKException, ClassNotFoundException {
        try {
            ManagedObjectRepresentation mo = managedObject;
            EventFilter filter = new EventFilter().byDate(DateConverter.string2Date(time), DateConverter.string2Date(time));
            collection = eventApi.getEventsByFilter(filter).get();
        } catch (SDKException ex) {
            status = ex.getHttpStatus();
        }
    }


    @When("I get the Events with the created id$")
    public void iGetEventsWithCreatedId() throws SDKException {
        try {
            result1.add(eventApi.getEvent(result.get(0).getId()));
        } catch (SDKException ex) {
            status = ex.getHttpStatus();
        }
    }

    @When("I delete the Events with the created id$")
    public void iDeleteEventsWithCreatedId() throws SDKException {
        try {
            eventApi.delete(result.get(0));
        } catch (SDKException ex) {
            status = ex.getHttpStatus();
        }
    }

    @When("I query all Events by page '(\\d+)'$")
    public void iQueryAllByPageNumber(int pageNumber) throws SDKException {
        try {
            collection1 = eventApi.getEvents().getPage(collection, pageNumber);
        } catch (SDKException ex) {
            status = ex.getHttpStatus();
        }
    }

    @When("I should get next page which has current page '(\\d+)' and events '(\\d+)'$")
    public void iQueryAllByNextPage(int currentPage, int numEvents) throws SDKException {
        try {
            EventCollectionRepresentation collectionRepresentation = eventApi.getEvents().getNextPage(collection1);
            assertThat(collectionRepresentation.getPageStatistics().getCurrentPage(), is(currentPage));
            assertThat(collectionRepresentation.getEvents().size(), is(numEvents));
        } catch (SDKException ex) {
            status = ex.getHttpStatus();
        }

    }

    @When("I should get previous page which has current page '(\\d+)' and events '(\\d+)'$")
    public void iQueryAllByPreviousPage(int currentPage, int numEvents) throws SDKException {
        try {
            EventCollectionRepresentation collectionRepresentation = eventApi.getEvents().getPreviousPage(collection1);
            assertThat(currentPage, is(equalTo(collectionRepresentation.getPageStatistics().getCurrentPage())));
            assertThat(numEvents, is(equalTo(collectionRepresentation.getEvents().size())));
        } catch (SDKException ex) {
            status = ex.getHttpStatus();
        }

    }

    // ------------------------------------------------------------------------
    // Then
    // ------------------------------------------------------------------------

    @Then("All Events should be created$")
    public void shouldBeCreated() {
        for (EventRepresentation rep : result) {
            assertThat(rep.getId(), is(notNullValue()));
        }
    }

    @Then("I should get '(\\d+)' Events$")
    public void iShouldGetNumberOfEvents(int count) {
        assertThat(collection.getEvents().size(), is(count));
    }

    @Then("I should get '(\\d+)' Events of paging$")
    public void iShouldGetNumberOfEventsOfPaging(int count) {
        assertThat(collection1.getEvents().size(), is(count));
    }

    @Then("Event response should be unprocessable$")
    public void shouldBeBadRequest() {
        assertThat(status, is(UNPROCESSABLE));
    }

    @Then("I should get all the Events$")
    public void shouldGetAllEvents() {
        assertThat(collection.getEvents().size(), is(equalTo(result.size())));
    }

    @Then("I should get the Events$")
    public void shouldGetTheEvent() {
        assertThat(result1.get(0), is(equalTo(result1.get(0))));
    }

    @Then("Events should not be found$")
    public void shouldNotBeFound() {
        assertThat(status, is(equalTo(NOT_FOUND)));
    }

}
