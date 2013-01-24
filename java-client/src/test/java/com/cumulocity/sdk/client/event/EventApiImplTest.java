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

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.sameInstance;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.whenNew;

import java.util.Date;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.cumulocity.model.DateConverter;
import com.cumulocity.model.idtype.GId;
import com.cumulocity.model.util.ExtensibilityConverter;
import com.cumulocity.rest.representation.event.EventCollectionRepresentation;
import com.cumulocity.rest.representation.event.EventMediaType;
import com.cumulocity.rest.representation.event.EventRepresentation;
import com.cumulocity.rest.representation.event.EventsApiRepresentation;
import com.cumulocity.rest.representation.inventory.ManagedObjectRepresentation;
import com.cumulocity.rest.representation.platform.PlatformApiRepresentation;
import com.cumulocity.rest.representation.platform.PlatformMediaType;
import com.cumulocity.sdk.client.PagedCollectionResource;
import com.cumulocity.sdk.client.RestConnector;
import com.cumulocity.sdk.client.SDKException;
import com.cumulocity.sdk.client.TemplateUrlParser;

@RunWith(PowerMockRunner.class)
@PrepareForTest(EventApiImpl.class)
public class EventApiImplTest {

    private static final String SOURCE_GID = "1";

    private static final String EXACT_URL = "exact_url";

    private static final String EVENTS_COLLECTION_URL = "event_collection_url";

    private static final String PLATFORM_API_URL = "platform_api_url";

    EventApi eventApi;

    EventsApiRepresentation eventsApiRepresentation = new EventsApiRepresentation();

    ManagedObjectRepresentation source = new ManagedObjectRepresentation();

    @Mock
    private RestConnector restConnector;

    private TemplateUrlParser templateUrlParser = new TemplateUrlParser();

    private static final String TYPE = "type1";

    private static final int DEFAULT_PAGE_SIZE = 11;

    private Date from;

    private Date to;

    private EventCollectionImpl expected;

    @Before
    public void setup() throws Exception {
        MockitoAnnotations.initMocks(this);
        eventApi = new EventApiImpl(restConnector, templateUrlParser, PLATFORM_API_URL, DEFAULT_PAGE_SIZE);
        EventCollectionRepresentation eventCollectionRepresentation = new EventCollectionRepresentation();
        eventCollectionRepresentation.setSelf(EVENTS_COLLECTION_URL);
        eventsApiRepresentation.setEvents(eventCollectionRepresentation);
        source.setId(new GId(SOURCE_GID));

        from = new Date(10000L);
        to = new Date(20000L);

        eventsApiRepresentation.setEventsForDateAndFragmentType("?dateFrom={dateFrom}&dateTo={dateTo}&fragmentType={fragmentType}");
        eventsApiRepresentation
                .setEventsForDateAndFragmentTypeAndType("?dateFrom={dateFrom}&dateTo={dateTo}&fragmentType={fragmentType}&type={type}");
        eventsApiRepresentation.setEventsForFragmentType("?fragmentType={fragmentType}");
        eventsApiRepresentation.setEventsForFragmentTypeAndType("?fragmentType={fragmentType}&type={type}");
        eventsApiRepresentation.setEventsForSource("?source={source}");
        eventsApiRepresentation
                .setEventsForSourceAndDateAndFragmentType("?source={source}&dateFrom={dateFrom}&dateTo={dateTo}&fragmentType={fragmentType}");
        eventsApiRepresentation
                .setEventsForSourceAndDateAndFragmentTypeAndType("?source={source}&dateFrom={dateFrom}&dateTo={dateTo}&fragmentType={fragmentType}&type={type}");
        eventsApiRepresentation.setEventsForSourceAndFragmentType("?source={source}&fragmentType={fragmentType}");
        eventsApiRepresentation.setEventsForSourceAndFragmentTypeAndType("?source={source}&fragmentType={fragmentType}&type={type}");
        eventsApiRepresentation.setEventsForSourceAndTime("?source={source}&dateFrom={dateFrom}&dateTo={dateTo}");
        eventsApiRepresentation.setEventsForSourceAndTimeAndType("?source={source}&dateFrom={dateFrom}&dateTo={dateTo}&type={type}");
        eventsApiRepresentation.setEventsForSourceAndType("?type={type}&source={source}");
        eventsApiRepresentation.setEventsForTime("?dateFrom={dateFrom}&dateTo={dateTo}");
        eventsApiRepresentation.setEventsForTimeAndType("?dateFrom={dateFrom}&dateTo={dateTo}&type={type}");
        eventsApiRepresentation.setEventsForType("?type={type}");

        PlatformApiRepresentation platformApiRepresentation = new PlatformApiRepresentation();
        platformApiRepresentation.setEvent(eventsApiRepresentation);
        when(restConnector.get(PLATFORM_API_URL, PlatformMediaType.PLATFORM_API, PlatformApiRepresentation.class)).thenReturn(platformApiRepresentation);

        expected = new EventCollectionImpl(restConnector, EXACT_URL, DEFAULT_PAGE_SIZE);
    }

    @Test
    public void shouldGet() throws Exception {
        // Given 
        String gidValue = "10";
        GId gid = new GId(gidValue);
        EventRepresentation retrieved = new EventRepresentation();
        when(restConnector.get(EVENTS_COLLECTION_URL + "/" + gidValue, EventMediaType.EVENT, EventRepresentation.class)).thenReturn(
                retrieved);

        // When
        EventRepresentation event = eventApi.getEvent(gid);

        // Then 
        assertThat(event, sameInstance(retrieved));
    }

    @Test
    public void shouldDelete() throws Exception {
        // Given 
        String gidValue = "10";
        GId gid = new GId(gidValue);
        EventRepresentation eventToDelete = new EventRepresentation();
        eventToDelete.setId(gid);

        // When 
        eventApi.delete(eventToDelete);

        // Then
        verify(restConnector).delete(EVENTS_COLLECTION_URL + "/" + gidValue);
    }

    @Test
    public void shouldRetrieveEventCollection() throws SDKException {
        //Given
        PagedCollectionResource<EventCollectionRepresentation> expected = new EventCollectionImpl(restConnector, EVENTS_COLLECTION_URL,
                DEFAULT_PAGE_SIZE);

        // When
        PagedCollectionResource<EventCollectionRepresentation> result = eventApi.getEvents();

        // Then
        assertThat(result, is(expected));
    }

    @Test
    public void shouldRetrieveEventCollectionByEmptyFilter() throws SDKException {
        //Given
        PagedCollectionResource<EventCollectionRepresentation> expected = new EventCollectionImpl(restConnector, EVENTS_COLLECTION_URL,
                DEFAULT_PAGE_SIZE);

        // When
        EventFilter filter = new EventFilter();
        PagedCollectionResource<EventCollectionRepresentation> result = eventApi.getEventsByFilter(filter);

        // Then
        assertThat(result, is(expected));
    }

    @Test
    public void shouldRetrieveEventsByTypeFilter() throws Exception {
        // Given 
        EventCollectionImpl expected = new EventCollectionImpl(restConnector, EXACT_URL, DEFAULT_PAGE_SIZE);
        whenNew(EventCollectionImpl.class).withArguments(restConnector, "?type=" + TYPE, DEFAULT_PAGE_SIZE).thenReturn(expected);

        // When
        EventFilter filter = new EventFilter().byType(TYPE);
        PagedCollectionResource<EventCollectionRepresentation> result = eventApi.getEventsByFilter(filter);

        // Then
        assertThat((EventCollectionImpl) result, is(expected));
    }

    @Test
    public void shouldRetrieveEventsBySourceFilter() throws Exception {
        // Given 
        EventCollectionImpl expected = new EventCollectionImpl(restConnector, EXACT_URL, DEFAULT_PAGE_SIZE);
        whenNew(EventCollectionImpl.class).withArguments(restConnector, "?source=" + source.getId().getValue(), DEFAULT_PAGE_SIZE)
                .thenReturn(expected);

        // When
        EventFilter filter = new EventFilter().bySource(source);
        PagedCollectionResource<EventCollectionRepresentation> result = eventApi.getEventsByFilter(filter);

        // Then
        assertThat((EventCollectionImpl) result, is(expected));
    }

    @Test
    public void shouldRetrieveEventCollectionResourceByTypeAndSource() throws Exception {
        // Given 
        EventCollectionImpl expected = new EventCollectionImpl(restConnector, EXACT_URL, DEFAULT_PAGE_SIZE);
        whenNew(EventCollectionImpl.class).withArguments(restConnector, "?type=" + TYPE + "&source=" + source.getId().getValue(),
                DEFAULT_PAGE_SIZE).thenReturn(expected);

        // When
        EventFilter filter = new EventFilter().byType(TYPE).bySource(source);
        PagedCollectionResource<EventCollectionRepresentation> result = eventApi.getEventsByFilter(filter);

        // Then
        assertThat((EventCollectionImpl) result, is(expected));
    }

    @Test
    public void shouldRetrieveEventCollectionResourceByTime() throws Exception {
        // Given
        EventCollectionImpl expected = new EventCollectionImpl(restConnector, EXACT_URL, DEFAULT_PAGE_SIZE);
        whenNew(EventCollectionImpl.class).withArguments(
                restConnector,
                "?dateFrom=" + TemplateUrlParser.encode(DateConverter.date2String(from)) + "&dateTo="
                        + TemplateUrlParser.encode(DateConverter.date2String(to)), DEFAULT_PAGE_SIZE).thenReturn(expected);

        // When
        EventFilter filter = new EventFilter().byDate(from, to);
        PagedCollectionResource<EventCollectionRepresentation> result = eventApi.getEventsByFilter(filter);

        // Then
        assertThat((EventCollectionImpl) result, is(expected));
    }

    @Test
    public void shouldRetrieveEventCollectionResourceByFromTime() throws Exception {
        // Given
        EventCollectionImpl expected = new EventCollectionImpl(restConnector, EXACT_URL, DEFAULT_PAGE_SIZE);
        whenNew(EventCollectionImpl.class).withArguments(restConnector,
                "?dateFrom=" + TemplateUrlParser.encode(DateConverter.date2String(from)), DEFAULT_PAGE_SIZE).thenReturn(expected);

        // When
        EventFilter filter = new EventFilter().byFromDate(from);
        PagedCollectionResource<EventCollectionRepresentation> result = eventApi.getEventsByFilter(filter);

        // Then
        assertThat((EventCollectionImpl) result, is(expected));
    }

    @Test
    public void shouldRetrieveEventCollectionResourceBySourceAndTime() throws Exception {
        // Given 
        EventCollectionImpl expected = new EventCollectionImpl(restConnector, EXACT_URL, DEFAULT_PAGE_SIZE);
        whenNew(EventCollectionImpl.class).withArguments(
                restConnector,
                "?source=" + source.getId().getValue() + "&dateFrom=" + TemplateUrlParser.encode(DateConverter.date2String(from))
                        + "&dateTo=" + TemplateUrlParser.encode(DateConverter.date2String(to)), DEFAULT_PAGE_SIZE).thenReturn(expected);

        // When
        EventFilter filter = new EventFilter().bySource(source).byDate(from, to);
        PagedCollectionResource<EventCollectionRepresentation> result = eventApi.getEventsByFilter(filter);

        // Then
        assertThat((EventCollectionImpl) result, is(expected));
    }

    @Test
    public void shouldRetrieveEventCollectionResourceBySourceAndFromTime() throws Exception {
        // Given 
        EventCollectionImpl expected = new EventCollectionImpl(restConnector, EXACT_URL, DEFAULT_PAGE_SIZE);
        whenNew(EventCollectionImpl.class).withArguments(restConnector,
                "?source=" + source.getId().getValue() + "&dateFrom=" + TemplateUrlParser.encode(DateConverter.date2String(from)),
                DEFAULT_PAGE_SIZE).thenReturn(expected);

        // When
        EventFilter filter = new EventFilter().bySource(source).byFromDate(from);
        PagedCollectionResource<EventCollectionRepresentation> result = eventApi.getEventsByFilter(filter);

        // Then
        assertThat((EventCollectionImpl) result, is(expected));
    }

    @Test
    public void shouldRetrieveEventCollectionResourceByTimeAndType() throws Exception {
        // Given 
        EventCollectionImpl expected = new EventCollectionImpl(restConnector, EXACT_URL, DEFAULT_PAGE_SIZE);
        whenNew(EventCollectionImpl.class).withArguments(
                restConnector,
                "?dateFrom=" + TemplateUrlParser.encode(DateConverter.date2String(from)) + "&dateTo="
                        + TemplateUrlParser.encode(DateConverter.date2String(to)) + "&type=" + TYPE, DEFAULT_PAGE_SIZE)
                .thenReturn(expected);

        // When
        EventFilter filter = new EventFilter().byType(TYPE).byDate(from, to);
        PagedCollectionResource<EventCollectionRepresentation> result = eventApi.getEventsByFilter(filter);

        // Then
        assertThat((EventCollectionImpl) result, is(expected));
    }

    @Test
    public void shouldRetrieveEventCollectionResourceByFromTimeAndType() throws Exception {
        // Given 
        whenNew(EventCollectionImpl.class).withArguments(restConnector,
                "?dateFrom=" + TemplateUrlParser.encode(DateConverter.date2String(from)) + "&type=" + TYPE, DEFAULT_PAGE_SIZE).thenReturn(
                expected);

        // When
        EventFilter filter = new EventFilter().byType(TYPE).byFromDate(from);
        PagedCollectionResource<EventCollectionRepresentation> result = eventApi.getEventsByFilter(filter);

        // Then
        assertThat((EventCollectionImpl) result, is(expected));
    }

    @Test
    public void shouldRetrieveEventCollectionResourceBySourceAndTimeAndType() throws Exception {
        // Given 
        whenNew(EventCollectionImpl.class).withArguments(
                restConnector,
                "?source=" + source.getId().getValue() + "&dateFrom=" + TemplateUrlParser.encode(DateConverter.date2String(from))
                        + "&dateTo=" + TemplateUrlParser.encode(DateConverter.date2String(to)) + "&type=" + TYPE, DEFAULT_PAGE_SIZE)
                .thenReturn(expected);

        // When
        EventFilter filter = new EventFilter().bySource(source).byType(TYPE).byDate(from, to);
        PagedCollectionResource<EventCollectionRepresentation> result = eventApi.getEventsByFilter(filter);

        // Then
        assertThat((EventCollectionImpl) result, is(expected));
    }

    @Test
    public void shouldRetrieveEventCollectionResourceBySourceAndFromTimeAndType() throws Exception {
        // Given 
        whenNew(EventCollectionImpl.class).withArguments(
                restConnector,
                "?source=" + source.getId().getValue() + "&dateFrom=" + TemplateUrlParser.encode(DateConverter.date2String(from))
                        + "&type=" + TYPE, DEFAULT_PAGE_SIZE).thenReturn(expected);

        // When
        EventFilter filter = new EventFilter().bySource(source).byType(TYPE).byFromDate(from);
        PagedCollectionResource<EventCollectionRepresentation> result = eventApi.getEventsByFilter(filter);

        // Then
        assertThat((EventCollectionImpl) result, is(expected));
    }

    @Test
    public void shouldRetrieveEventCollectionResourceByFragmentType() throws Exception {
        // Given 
        whenNew(EventCollectionImpl.class).withArguments(restConnector,
                "?fragmentType=" + ExtensibilityConverter.classToStringRepresentation(Object.class), DEFAULT_PAGE_SIZE)
                .thenReturn(expected);

        // When
        EventFilter filter = new EventFilter().byFragmentType(Object.class);
        PagedCollectionResource<EventCollectionRepresentation> result = eventApi.getEventsByFilter(filter);

        // Then
        assertThat((EventCollectionImpl) result, is(expected));
    }

    @Test
    public void shouldRetrieveEventCollectionResourceBySourceAndFragmentType() throws Exception {
        // Given 
        whenNew(EventCollectionImpl.class).withArguments(
                restConnector,
                "?source=" + source.getId().getValue() + "&fragmentType="
                        + ExtensibilityConverter.classToStringRepresentation(Object.class), DEFAULT_PAGE_SIZE).thenReturn(expected);

        // When
        EventFilter filter = new EventFilter().bySource(source).byFragmentType(Object.class);
        PagedCollectionResource<EventCollectionRepresentation> result = eventApi.getEventsByFilter(filter);

        // Then
        assertThat((EventCollectionImpl) result, is(expected));
    }

    @Test
    public void shouldRetrieveEventCollectionResourceByDateAndFragmentType() throws Exception {
        // Given
        whenNew(EventCollectionImpl.class).withArguments(
                restConnector,
                "?dateFrom=" + TemplateUrlParser.encode(DateConverter.date2String(from)) + "&dateTo="
                        + TemplateUrlParser.encode(DateConverter.date2String(to)) + "&fragmentType="
                        + ExtensibilityConverter.classToStringRepresentation(Object.class), DEFAULT_PAGE_SIZE).thenReturn(expected);

        // When
        EventFilter filter = new EventFilter().byDate(from, to).byFragmentType(Object.class);
        PagedCollectionResource<EventCollectionRepresentation> result = eventApi.getEventsByFilter(filter);

        // Then
        assertThat((EventCollectionImpl) result, is(expected));
    }

    @Test
    public void shouldRetrieveEventCollectionResourceByFromDateAndFragmentType() throws Exception {
        // Given
        whenNew(EventCollectionImpl.class).withArguments(
                restConnector,
                "?dateFrom=" + TemplateUrlParser.encode(DateConverter.date2String(from)) + "&fragmentType="
                        + ExtensibilityConverter.classToStringRepresentation(Object.class), DEFAULT_PAGE_SIZE).thenReturn(expected);

        // When
        EventFilter filter = new EventFilter().byFromDate(from).byFragmentType(Object.class);
        PagedCollectionResource<EventCollectionRepresentation> result = eventApi.getEventsByFilter(filter);

        // Then
        assertThat((EventCollectionImpl) result, is(expected));
    }

    @Test
    public void shouldRetrieveEventCollectionResourceByFragmentTypeAndType() throws Exception {
        // Given
        whenNew(EventCollectionImpl.class).withArguments(restConnector,
                "?fragmentType=" + ExtensibilityConverter.classToStringRepresentation(Object.class) + "&type=" + TYPE, DEFAULT_PAGE_SIZE)
                .thenReturn(expected);

        // When
        EventFilter filter = new EventFilter().byType(TYPE).byFragmentType(Object.class);
        PagedCollectionResource<EventCollectionRepresentation> result = eventApi.getEventsByFilter(filter);

        // Then
        assertThat((EventCollectionImpl) result, is(expected));
    }

    @Test
    public void shouldRetrieveEventCollectionResourceBySourceAndDateAndFragmentType() throws Exception {
        // Given
        whenNew(EventCollectionImpl.class).withArguments(
                restConnector,
                "?source=" + SOURCE_GID + "&dateFrom=" + TemplateUrlParser.encode(DateConverter.date2String(from)) + "&dateTo="
                        + TemplateUrlParser.encode(DateConverter.date2String(to)) + "&fragmentType="
                        + ExtensibilityConverter.classToStringRepresentation(Object.class), DEFAULT_PAGE_SIZE).thenReturn(expected);
        // When
        EventFilter filter = new EventFilter().bySource(source).byDate(from, to).byFragmentType(Object.class);
        PagedCollectionResource<EventCollectionRepresentation> result = eventApi.getEventsByFilter(filter);

        // Then
        assertThat((EventCollectionImpl) result, is(expected));
    }

    @Test
    public void shouldRetrieveEventCollectionResourceBySourceAndFromDateAndFragmentType() throws Exception {
        // Given
        whenNew(EventCollectionImpl.class).withArguments(
                restConnector,
                "?source=" + SOURCE_GID + "&dateFrom=" + TemplateUrlParser.encode(DateConverter.date2String(from)) + "&fragmentType="
                        + ExtensibilityConverter.classToStringRepresentation(Object.class), DEFAULT_PAGE_SIZE).thenReturn(expected);
        // When
        EventFilter filter = new EventFilter().bySource(source).byFromDate(from).byFragmentType(Object.class);
        PagedCollectionResource<EventCollectionRepresentation> result = eventApi.getEventsByFilter(filter);

        // Then
        assertThat((EventCollectionImpl) result, is(expected));
    }

    @Test
    public void shouldRetrieveEventCollectionResourceBySourceAndFragmentTypeAndType() throws Exception {
        // Given
        whenNew(EventCollectionImpl.class).withArguments(
                restConnector,
                "?source=" + SOURCE_GID + "&fragmentType=" + ExtensibilityConverter.classToStringRepresentation(Object.class) + "&type="
                        + TYPE, DEFAULT_PAGE_SIZE).thenReturn(expected);

        // When
        EventFilter filter = new EventFilter().bySource(source).byType(TYPE).byFragmentType(Object.class);
        PagedCollectionResource<EventCollectionRepresentation> result = eventApi.getEventsByFilter(filter);

        // Then
        assertThat((EventCollectionImpl) result, is(expected));
    }

    @Test
    public void shouldRetrieveEventCollectionResourceByDateAndFragmentTypeAndType() throws Exception {
        // Given
        whenNew(EventCollectionImpl.class).withArguments(
                restConnector,
                "?dateFrom=" + TemplateUrlParser.encode(DateConverter.date2String(from)) + "&dateTo="
                        + TemplateUrlParser.encode(DateConverter.date2String(to)) + "&fragmentType="
                        + ExtensibilityConverter.classToStringRepresentation(Object.class) + "&type=" + TYPE, DEFAULT_PAGE_SIZE)
                .thenReturn(expected);

        // When
        EventFilter filter = new EventFilter().byDate(from, to).byType(TYPE).byFragmentType(Object.class);
        PagedCollectionResource<EventCollectionRepresentation> result = eventApi.getEventsByFilter(filter);

        // Then
        assertThat((EventCollectionImpl) result, is(expected));
    }

    @Test
    public void shouldRetrieveEventCollectionResourceByFromDateAndFragmentTypeAndType() throws Exception {
        // Given
        whenNew(EventCollectionImpl.class).withArguments(
                restConnector,
                "?dateFrom=" + TemplateUrlParser.encode(DateConverter.date2String(from)) + "&fragmentType="
                        + ExtensibilityConverter.classToStringRepresentation(Object.class) + "&type=" + TYPE, DEFAULT_PAGE_SIZE)
                .thenReturn(expected);

        // When
        EventFilter filter = new EventFilter().byFromDate(from).byType(TYPE).byFragmentType(Object.class);
        PagedCollectionResource<EventCollectionRepresentation> result = eventApi.getEventsByFilter(filter);

        // Then
        assertThat((EventCollectionImpl) result, is(expected));
    }

    @Test
    public void shouldRetrieveEventCollectionResourceBySourceAndDateAndFragmentTypeAndType() throws Exception {
        // Given 
        whenNew(EventCollectionImpl.class).withArguments(
                restConnector,
                "?source=" + SOURCE_GID + "&dateFrom=" + TemplateUrlParser.encode(DateConverter.date2String(from)) + "&dateTo="
                        + TemplateUrlParser.encode(DateConverter.date2String(to)) + "&fragmentType="
                        + ExtensibilityConverter.classToStringRepresentation(Object.class) + "&type=" + TYPE, DEFAULT_PAGE_SIZE)
                .thenReturn(expected);

        // When
        EventFilter filter = new EventFilter().bySource(source).byDate(from, to).byType(TYPE).byFragmentType(Object.class);
        PagedCollectionResource<EventCollectionRepresentation> result = eventApi.getEventsByFilter(filter);

        // Then
        assertThat((EventCollectionImpl) result, is(expected));
    }

    @Test
    public void shouldRetrieveEventCollectionResourceBySourceAndFromDateAndFragmentTypeAndType() throws Exception {
        // Given 
        whenNew(EventCollectionImpl.class).withArguments(
                restConnector,
                "?source=" + SOURCE_GID + "&dateFrom=" + TemplateUrlParser.encode(DateConverter.date2String(from)) + "&fragmentType="
                        + ExtensibilityConverter.classToStringRepresentation(Object.class) + "&type=" + TYPE, DEFAULT_PAGE_SIZE)
                .thenReturn(expected);

        // When
        EventFilter filter = new EventFilter().bySource(source).byFromDate(from).byType(TYPE).byFragmentType(Object.class);
        PagedCollectionResource<EventCollectionRepresentation> result = eventApi.getEventsByFilter(filter);

        // Then
        assertThat((EventCollectionImpl) result, is(expected));
    }

    @Test
    public void testCreateEventInCollection() throws SDKException {
        // Given
        EventRepresentation eventRepresentation = new EventRepresentation();
        EventRepresentation created = new EventRepresentation();
        when(restConnector.post(EVENTS_COLLECTION_URL, EventMediaType.EVENT, eventRepresentation)).thenReturn(created);

        // When
        EventRepresentation result = eventApi.create(eventRepresentation);

        // Then
        assertThat(result, sameInstance(created));

    }
}
