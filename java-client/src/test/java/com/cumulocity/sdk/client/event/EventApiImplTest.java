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

import java.util.Collections;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.cumulocity.model.idtype.GId;
import com.cumulocity.rest.representation.event.EventCollectionRepresentation;
import com.cumulocity.rest.representation.event.EventMediaType;
import com.cumulocity.rest.representation.event.EventRepresentation;
import com.cumulocity.rest.representation.event.EventsApiRepresentation;
import com.cumulocity.rest.representation.platform.PlatformApiRepresentation;
import com.cumulocity.rest.representation.platform.PlatformMediaType;
import com.cumulocity.sdk.client.PagedCollectionResource;
import com.cumulocity.sdk.client.RestConnector;
import com.cumulocity.sdk.client.SDKException;
import com.cumulocity.sdk.client.UrlProcessor;

public class EventApiImplTest {

    private static final String SOURCE_GID = "1";

    private static final String EVENTS_COLLECTION_URL = "event_collection_url";

    private static final String PLATFORM_API_URL = "platform_api_url";

    EventApi eventApi;

    EventsApiRepresentation eventsApiRepresentation = new EventsApiRepresentation();

    GId source = new GId(SOURCE_GID);

    @Mock
    private RestConnector restConnector;

    private static final String TYPE = "type1";

    private static final int DEFAULT_PAGE_SIZE = 11;

    @Mock
    private UrlProcessor urlProcessor;

    @Before
    public void setup() throws Exception {
        MockitoAnnotations.initMocks(this);
        eventApi = new EventApiImpl(restConnector, urlProcessor, PLATFORM_API_URL, DEFAULT_PAGE_SIZE);
        EventCollectionRepresentation eventCollectionRepresentation = new EventCollectionRepresentation();
        eventCollectionRepresentation.setSelf(EVENTS_COLLECTION_URL);
        eventsApiRepresentation.setEvents(eventCollectionRepresentation);

        PlatformApiRepresentation platformApiRepresentation = new PlatformApiRepresentation();
        platformApiRepresentation.setEvent(eventsApiRepresentation);
        when(restConnector.get(PLATFORM_API_URL, PlatformMediaType.PLATFORM_API, PlatformApiRepresentation.class)).thenReturn(platformApiRepresentation);
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
        when(urlProcessor.replaceOrAddQueryParam(EVENTS_COLLECTION_URL, Collections.<String, String>emptyMap())).thenReturn(EVENTS_COLLECTION_URL);
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
        EventFilter filter = new EventFilter().byType(TYPE);
        String eventsByTypeUrl = EVENTS_COLLECTION_URL + "?type=" + TYPE;
        when(urlProcessor.replaceOrAddQueryParam(EVENTS_COLLECTION_URL, filter.getQueryParams())).thenReturn(eventsByTypeUrl);
        EventCollectionImpl expected = new EventCollectionImpl(restConnector, eventsByTypeUrl, DEFAULT_PAGE_SIZE);

        // When
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
