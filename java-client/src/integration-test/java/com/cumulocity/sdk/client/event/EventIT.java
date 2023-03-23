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

import com.cumulocity.model.DateConverter;
import com.cumulocity.rest.representation.event.EventCollectionRepresentation;
import com.cumulocity.rest.representation.event.EventRepresentation;
import com.cumulocity.rest.representation.inventory.ManagedObjectRepresentation;
import com.cumulocity.sdk.client.SDKException;
import com.cumulocity.sdk.client.common.JavaSdkITBase;
import org.joda.time.DateTime;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static com.cumulocity.model.DateConverter.string2Date;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.awaitility.Awaitility.await;
import static org.awaitility.Durations.TEN_SECONDS;

//TODO inline step definitions (see AlarmIT or InventoryIT)
public class EventIT extends JavaSdkITBase {

    private static ManagedObjectRepresentation managedObject;

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

    @BeforeAll
    public static void createManagedObject() {
        ManagedObjectRepresentation mo = new ManagedObjectRepresentation();
        mo.setName("MO");
        managedObject = platform.getInventoryApi().create(mo);
    }

    @BeforeEach
    public void setup() throws Exception {
        eventApi = platform.getEventApi();
        input = new ArrayList<>();
        result = new ArrayList<>();
        result1 = new ArrayList<>();
        status = OK;
    }

    @AfterEach
    public void deleteEvents() {
        List<EventRepresentation> eventsOn1stPage = getEventsFrom1stPage();
        while (!eventsOn1stPage.isEmpty()) {
            deleteMOs(eventsOn1stPage);
            eventsOn1stPage = getEventsFrom1stPage();
        }
    }

    @Test
    public void createEvents() {
        // given
        iHaveEvents(2, "type1");
        // when
        iCreateAll();
        iGetAllEvents();
        // then
        iShouldGetNumberOfEvents(2);
    }

    @Test
    public void createEventsWithoutType() {
        // given
        iHaveAEventWithNoType();
        // when
        iCreateAll();
        // then
        shouldBeBadRequest();
    }

    @Test
    public void createEventsWithoutTime() {
        // given
        iHaveAEventWithNoTime();
        // when
        iCreateAll();
        // then
        shouldBeBadRequest();
    }

    @Test
    public void createEventsWithoutText() {
        // given
        iHaveAEventWithNoText();
        // when
        iCreateAll();
        // then
        shouldBeBadRequest();
    }

    @Test
    public void getEventCollection() {
        // given
        iHaveEvents(2, "type1");
        // when
        iCreateAll();
        iGetAllEvents();
        // then
        shouldGetAllEvents();
    }


    @Test
    public void getEventCollectionByType() throws Exception {
        // given
        iHaveEvents(2, "type");
        iHaveEvents(3, "type1");
        // when
        iCreateAll();
        iGetAllEvents();
        // then
        iShouldGetNumberOfEvents(5);
        // when
        iQueryAllByType("type");
        // then
        iShouldGetNumberOfEvents(2);
        // when
        iQueryAllByType("type1");
        // then
        iShouldGetNumberOfEvents(3);
        // when
        iQueryAllByType("type2");
        // then
        iShouldGetNumberOfEvents(0);
    }

    @Test
    public void getEventCollectionBySource() {
        // given
        iHaveEventsForSource(3);
        // when
        iCreateAll();
        iGetAllEvents();
        // then
        iShouldGetNumberOfEvents(3);
        // when
        iQueryAllBySource();
        // then
        iShouldGetNumberOfEvents(3);
    }

    @Test
    public void getEventCollectionBySourceAndType() {
        // given
        iHaveAEventWithTypeAndTime("2011-11-03T11:01:00.000+05:30", "type");
        iHaveAEventWithTypeAndTime("2011-11-03T11:05:00.000+05:30", "type1");
        // when
        iCreateAll();
        iGetAllEvents();
        // then
        iShouldGetNumberOfEvents(2);
        // when
        iQueryAllBySourceAndType(0, "type");
        // then
        iShouldGetNumberOfEvents(1);
        // when
        iQueryAllBySourceAndType(0, "type1");
        // then
        iShouldGetNumberOfEvents(1);
        // when
        iQueryAllBySourceAndType(0, "type2");
        // then
        iShouldGetNumberOfEvents(0);
    }

    @Test
    public void getEventCollectionByTime() {
        // given
        iHaveAEventWithTypeAndTime("2011-11-03T11:01:00.000+05:30", "type");
        iHaveAEventWithTypeAndTime("2011-11-03T11:05:00.000+05:30", "type1");
        // when
        iCreateAll();
        iGetAllEvents();
        // then
        iShouldGetNumberOfEvents(2);
        // when
        iQueryAllBySourceAndTime(0, "2011-11-03T11:01:00.000+05:30");
        // then
        iShouldGetNumberOfEvents(1);
    }

    @Test
    public void getEvent() {
        // given
        iHaveAEventWithTypeAndTime("2011-11-03T11:01:00.000+05:30", "type");
        // when
        iCreateAll();
        iGetEventsWithCreatedId();
        // then
        shouldGetTheEvent();
    }

    @Test
    public void deleteEvent() {
        // given
        iHaveAEventWithTypeAndTime("2011-11-03T11:01:00.000+05:30", "type");
        // when
        iCreateAll();
        iDeleteEventsWithCreatedId();
        iGetEventsWithCreatedId();
        // then
        shouldNotBeFound();
    }

    @Test
    public void deleteEventCollectionByEmptyFilter() {
        // given
        iHaveEvents(3, "type1");
        iHaveEvents(2, "type2");
        // when
        iCreateAll();
        iGetAllEvents();
        // then
        iShouldGetNumberOfEvents(5);
        // when
        iDeleteEventCollection();
        // then
        iShouldGetNumberOfEventsInTenSecond(0);
    }

    @Test
    public void deleteEventsByTypeFilter() {
        // given
        iHaveEvents(3, "type1");
        iHaveEvents(2, "type2");
        // when
        iCreateAll();
        iGetAllEvents();
        // then
        iShouldGetNumberOfEvents(5);
        // when
        iDeleteAllByType("type2");
        // then
        iShouldGetNumberOfEventsInTenSecond(3);
        // when
        iQueryAllByType("type1");
        // then
        iShouldGetNumberOfEvents(3);
        // when
        iQueryAllByType("type2");
        // then
        iShouldGetNumberOfEvents(0);
    }

    @Test
    public void getEventCollectionByPaging() {
        // given
        iHaveEventsForSource(17);
        // when
        iCreateAll();
        iGetAllEvents();
        // when
        iShouldGetNumberOfEvents(5);
        // when
        iQueryAllByPageNumber(1);
        // then
        iShouldGetNumberOfEventsOfPaging(5);
        // when
        iQueryAllByPageNumber(2);
        // then
        iShouldGetNumberOfEventsOfPaging(5);
        // when
        iQueryAllByPageNumber(3);
        // then
        iShouldGetNumberOfEventsOfPaging(5);
        // when
        iQueryAllByPageNumber(4);
        // then
        iShouldGetNumberOfEventsOfPaging(2);
        // when
        iQueryAllByPageNumber(5);
        // then
        iShouldGetNumberOfEventsOfPaging(0);
    }

    // ------------------------------------------------------------------------
    // Given
    // ------------------------------------------------------------------------

    private void iHaveEvents(int n, String type) {
        for (int i = 0; i < n; i++) {
            EventRepresentation rep = new EventRepresentation();
            rep.setType(type);
            rep.setDateTime(new DateTime());
            rep.setText(" Event of Managed Object : " + i);
            rep.setSource(managedObject);
            input.add(rep);

        }
    }

    private void iHaveAEventWithNoType() {
        EventRepresentation rep = new EventRepresentation();
        rep.setDateTime(new DateTime());
        rep.setText(" Event of Managed Object : " + 0);
        rep.setSource(managedObject);
        input.add(rep);
    }

    private void iHaveAEventWithNoText() {
        EventRepresentation rep = new EventRepresentation();
        rep.setType("type");
        rep.setDateTime(new DateTime());
        rep.setSource(managedObject);
        input.add(rep);
    }

    private void iHaveAEventWithNoTime() {
        EventRepresentation rep = new EventRepresentation();
        rep.setType("type");
        rep.setText(" Event of Managed Object : " + 1);
        rep.setSource(managedObject);
        input.add(rep);
    }

    private void iHaveEventsForSource(int n) {
        for (int i = 0; i < n; i++) {
            EventRepresentation rep = new EventRepresentation();
            rep.setType("type");
            rep.setDateTime(new DateTime());
            rep.setText(" Event of Managed Object : " + i);
            rep.setSource(managedObject);
            input.add(rep);
        }
    }

    private void iHaveAEventWithTypeAndTime(String time, String type) {
        EventRepresentation rep = new EventRepresentation();
        rep.setType(type);
        rep.setText(" Event of Managed Object : ");
        rep.setDateTime(DateTime.parse(time));
        rep.setSource(managedObject);
        input.add(rep);
    }

    // ------------------------------------------------------------------------
    // When
    // ------------------------------------------------------------------------

    private void iCreateAll() throws SDKException {
        try {
            for (EventRepresentation rep : input) {
                result.add(eventApi.create(rep));
            }
        } catch (SDKException ex) {
            status = ex.getHttpStatus();
        }
    }

    private void iGetAllEvents() throws SDKException {
        try {
            collection = eventApi.getEvents().get();
        } catch (SDKException ex) {
            status = ex.getHttpStatus();
        }
    }

    private void iQueryAllByType(String type) throws SDKException {
        try {
            EventFilter filter = new EventFilter().byType(type);
            collection = eventApi.getEventsByFilter(filter).get();
        } catch (SDKException ex) {
            status = ex.getHttpStatus();
        }
    }

    private void iQueryAllBySource() throws SDKException {
        try {
            ManagedObjectRepresentation mo = managedObject;
            EventFilter filter = new EventFilter().bySource(mo);
            collection = eventApi.getEventsByFilter(filter).get();
        } catch (SDKException ex) {
            status = ex.getHttpStatus();
        }
    }

    private void iQueryAllBySourceAndType(int index, String type) throws SDKException {
        try {
            ManagedObjectRepresentation mo = managedObject;
            EventFilter filter = new EventFilter().bySource(mo).byType(type);
            collection = eventApi.getEventsByFilter(filter).get();
        } catch (SDKException ex) {
            status = ex.getHttpStatus();
        }
    }

    private void iQueryAllBySourceAndTime(int index, String time) throws SDKException {
        try {
            ManagedObjectRepresentation mo = managedObject;
            EventFilter filter = new EventFilter().byDate(DateConverter.string2Date(time), DateConverter.string2Date(time));
            collection = eventApi.getEventsByFilter(filter).get();
        } catch (SDKException ex) {
            status = ex.getHttpStatus();
        }
    }

    private void iGetEventsWithCreatedId() throws SDKException {
        try {
            result1.add(eventApi.getEvent(result.get(0).getId()));
        } catch (SDKException ex) {
            status = ex.getHttpStatus();
        }
    }

    private void iDeleteEventsWithCreatedId() throws SDKException {
        try {
            eventApi.delete(result.get(0));
        } catch (SDKException ex) {
            status = ex.getHttpStatus();
        }
    }

    private void iDeleteEventCollection() throws SDKException {
        try {
            eventApi.deleteEventsByFilter(new EventFilter().byFromDate(string2Date("2000-01-01T00:00:00.000+00:00")));
        } catch (SDKException ex) {
            status = ex.getHttpStatus();
        }
    }

    private void iDeleteAllByType(String type) throws SDKException {
        try {
            EventFilter typeFilter = new EventFilter().byType(type).byFromDate(string2Date("2000-01-01T00:00:00.000+00:00"));
            eventApi.deleteEventsByFilter(typeFilter);
        } catch (SDKException ex) {
            status = ex.getHttpStatus();
        }
    }

    private void iQueryAllByPageNumber(int pageNumber) throws SDKException {
        try {
            collection1 = eventApi.getEvents().getPage(collection, pageNumber);
        } catch (SDKException ex) {
            status = ex.getHttpStatus();
        }
    }

    // ------------------------------------------------------------------------
    // Then
    // ------------------------------------------------------------------------

    private void iShouldGetNumberOfEvents(int count) {
        assertThat(collection.getEvents().size(), is(count));
    }

    private void iShouldGetNumberOfEventsOfPaging(int count) {
        assertThat(collection1.getEvents().size(), is(count));
    }

    private void shouldBeBadRequest() {
        assertThat(status, is(UNPROCESSABLE));
    }

    private void shouldGetAllEvents() {
        assertThat(collection.getEvents().size(), is(equalTo(result.size())));
    }

    private void shouldGetTheEvent() {
        assertThat(result1.get(0), is(equalTo(result1.get(0))));
    }

    private void shouldNotBeFound() {
        assertThat(status, is(equalTo(NOT_FOUND)));
    }

    private void deleteMOs(List<EventRepresentation> mosOn1stPage) throws SDKException {
        for (EventRepresentation e : mosOn1stPage) {
            eventApi.delete(e);
        }
    }

    private List<EventRepresentation> getEventsFrom1stPage() throws SDKException {
        return eventApi.getEvents().get().getEvents();
    }

    private void iShouldGetNumberOfEventsInTenSecond(int count) {
        await().atMost(TEN_SECONDS).until(() -> eventApi.getEvents().get().getEvents().size() == count);
    }
}
