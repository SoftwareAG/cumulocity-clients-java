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
package com.cumulocity.sdk.client.alarm;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.sameInstance;
import static org.mockito.Matchers.argThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.when;

import java.util.Collections;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.cumulocity.model.event.CumulocityAlarmStatuses;
import com.cumulocity.model.idtype.GId;
import com.cumulocity.rest.representation.alarm.AlarmCollectionRepresentation;
import com.cumulocity.rest.representation.alarm.AlarmMediaType;
import com.cumulocity.rest.representation.alarm.AlarmRepresentation;
import com.cumulocity.rest.representation.alarm.AlarmsApiRepresentation;
import com.cumulocity.sdk.client.RestConnector;
import com.cumulocity.sdk.client.SDKException;
import com.cumulocity.sdk.client.UrlProcessor;

public class AlarmApiImplTest {

    private static final String ALARM_COLLECTION_URL = "/alarm/alarms";

    private static final int DEFAULT_PAGE_SIZE = 555;

    private AlarmApi alarmApi;

    private AlarmsApiRepresentation alarmsApiRepresentation = new AlarmsApiRepresentation();

    @Mock
    private RestConnector restConnector;
    
    @Mock
    private UrlProcessor urlProcessor;


    @Before
    public void setup() throws Exception {
        MockitoAnnotations.initMocks(this);

        AlarmCollectionRepresentation alarmCollectionRepresentation = new AlarmCollectionRepresentation();
        alarmCollectionRepresentation.setSelf(ALARM_COLLECTION_URL);
        alarmsApiRepresentation.setAlarms(alarmCollectionRepresentation);

        alarmApi = new AlarmApiImpl(restConnector, urlProcessor, alarmsApiRepresentation, DEFAULT_PAGE_SIZE);
    }
    
    @Test
    public void shouldRetrieveAlarmRep() throws SDKException {
        //Given 
        GId gid = new GId("global_id");
        AlarmRepresentation alarmRep = new AlarmRepresentation();
        when(restConnector.get(ALARM_COLLECTION_URL + "/global_id", AlarmMediaType.ALARM, AlarmRepresentation.class)).thenReturn(alarmRep);

        //When
        AlarmRepresentation retrieved = alarmApi.getAlarm(gid);

        //Then
        assertThat(retrieved, sameInstance(alarmRep));
    }

    @Test
    public void shouldUpdateAlarmRep() throws SDKException {
        //Given
        AlarmRepresentation alarmToUpdate = new AlarmRepresentation();
        alarmToUpdate.setId(new GId("global_id"));
        alarmToUpdate.setStatus("STATUS");
        AlarmRepresentation alarmRep = new AlarmRepresentation();
        when(
                restConnector.put(eq(ALARM_COLLECTION_URL + "/global_id"), eq(AlarmMediaType.ALARM),
                        argThat(hasOnlyUpdatableFields(alarmToUpdate)))).thenReturn(alarmRep);

        //When
        AlarmRepresentation updated = alarmApi.updateAlarm(alarmToUpdate);

        //Then
        assertThat(updated, sameInstance(alarmRep));
    }

    private Matcher<AlarmRepresentation> hasOnlyUpdatableFields(final AlarmRepresentation alarm) {
        return new TypeSafeMatcher<AlarmRepresentation>() {

            @Override
            public boolean matchesSafely(AlarmRepresentation item) {
                if (item.getId() != null) {
                    return false;
                }
                if (!item.getStatus().equals(alarm.getStatus())) {
                    return false;
                }
                return true;

            }

            @Override
            public void describeTo(Description description) {
                description.appendText("an alarm representation having only updatable fields, as ").appendValue(alarm);
            }
        };
    }

    @Test
    public void shouldRetrieveAlarmCollectionResource() throws SDKException {
        // Given
        AlarmCollection expected = new AlarmCollectionImpl(restConnector, ALARM_COLLECTION_URL,
                DEFAULT_PAGE_SIZE);

        //When
        AlarmCollection alarmCollection = alarmApi.getAlarms();

        //Then
        assertThat(alarmCollection, is(expected));
    }

    @Test
    public void testCreateAlarmInCollection() throws SDKException {
        //Given
        AlarmRepresentation alarmRepresentation = new AlarmRepresentation();
        AlarmRepresentation created = new AlarmRepresentation();
        when(restConnector.postWithBuffer(ALARM_COLLECTION_URL, AlarmMediaType.ALARM, alarmRepresentation)).thenReturn(created);

        // When 
        AlarmRepresentation result = alarmApi.create(alarmRepresentation);

        // Then
        assertThat(result, sameInstance(created));
    }

    @Test
    public void shouldGetCollectionByFilterStatus() throws Exception {
        // Given
        CumulocityAlarmStatuses status = CumulocityAlarmStatuses.ACKNOWLEDGED;
        AlarmFilter alarmFilter = new AlarmFilter().byStatus(status);
        String alarmsByStatusUrl = ALARM_COLLECTION_URL + "?status=" + CumulocityAlarmStatuses.ACKNOWLEDGED.toString();
        when(urlProcessor.replaceOrAddQueryParam(ALARM_COLLECTION_URL, alarmFilter.getQueryParams())).thenReturn(alarmsByStatusUrl);
        AlarmCollectionImpl expected = new AlarmCollectionImpl(restConnector, alarmsByStatusUrl, DEFAULT_PAGE_SIZE);

        // When
        AlarmCollection alarms = alarmApi.getAlarmsByFilter(alarmFilter);

        // Then
        assertThat((AlarmCollectionImpl) alarms, is(expected));
    }

    @Test
    public void shouldGetCollectionByEmptyFilter() throws Exception {
        // Given
        when(urlProcessor.replaceOrAddQueryParam(ALARM_COLLECTION_URL, Collections.<String,String>emptyMap())).thenReturn(ALARM_COLLECTION_URL);
        AlarmCollection expected = new AlarmCollectionImpl(restConnector, ALARM_COLLECTION_URL,
                DEFAULT_PAGE_SIZE);

        // When
        AlarmCollection alarms = alarmApi.getAlarmsByFilter(new AlarmFilter());

        // Then
        assertThat(alarms, is(expected));
    }
}
