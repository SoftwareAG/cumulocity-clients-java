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

import static com.cumulocity.model.util.ExtensibilityConverter.classToStringRepresentation;
import static java.util.Collections.singletonMap;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.sameInstance;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.cumulocity.model.DateConverter;
import com.cumulocity.model.idtype.GId;
import com.cumulocity.model.util.ExtensibilityConverter;
import com.cumulocity.rest.representation.inventory.ManagedObjectRepresentation;
import com.cumulocity.rest.representation.measurement.MeasurementCollectionRepresentation;
import com.cumulocity.rest.representation.measurement.MeasurementMediaType;
import com.cumulocity.rest.representation.measurement.MeasurementRepresentation;
import com.cumulocity.rest.representation.measurement.MeasurementsApiRepresentation;
import com.cumulocity.rest.representation.platform.PlatformApiRepresentation;
import com.cumulocity.rest.representation.platform.PlatformMediaType;
import com.cumulocity.sdk.client.PagedCollectionResource;
import com.cumulocity.sdk.client.RestConnector;
import com.cumulocity.sdk.client.SDKException;
import com.cumulocity.sdk.client.TemplateUrlParser;

public class MeasurementApiImplTest {

    private static final String SOURCE_GID = "gid1";

    private static final String EXACT_URL = "exact_url";

    private static final String MEASUREMENT_COLLECTION_URL = "path_to_measurement";

    private static final String TEMPLATE_URL = "template_url";

    private static final String TYPE = "type1";

    private static final String PLATFORM_API_URL = "platform_api_url";

    private static final int DEAFAULT_PAGE_SIZE = 11;

    private MeasurementApi measurementApi;

    private Date from = new Date(10000L);

    private Date to = new Date(20000L);

    private MeasurementsApiRepresentation measurementsApiRepresentation = new MeasurementsApiRepresentation();

    private PlatformApiRepresentation platformApiRepresentation = new PlatformApiRepresentation();

    private MeasurementCollectionRepresentation measurementCollectionRepresentation = new MeasurementCollectionRepresentation();

    private ManagedObjectRepresentation source = new ManagedObjectRepresentation();

    @Mock
    private RestConnector restConnector;

    @Mock
    private TemplateUrlParser templateUrlParser;

    @Before
    public void setup() throws Exception {
        MockitoAnnotations.initMocks(this);
        measurementApi = new MeasurementApiImpl(restConnector, templateUrlParser,PLATFORM_API_URL, DEAFAULT_PAGE_SIZE);
        source.setId(new GId(SOURCE_GID));
        measurementCollectionRepresentation.setSelf(MEASUREMENT_COLLECTION_URL);
        measurementsApiRepresentation.setMeasurements(measurementCollectionRepresentation);
        platformApiRepresentation.setMeasurement(measurementsApiRepresentation);
        
        when(restConnector.get(PLATFORM_API_URL, PlatformMediaType.PLATFORM_API, PlatformApiRepresentation.class)).thenReturn(
                platformApiRepresentation);
    }

    @Test
    public void shouldReturnMeasurementRep() throws SDKException {
        // Given
        String gidValue = "123";
        GId gid = new GId(gidValue);

        MeasurementRepresentation meas = new MeasurementRepresentation();
        when(
                restConnector.get(MEASUREMENT_COLLECTION_URL + "/" + gidValue, MeasurementMediaType.MEASUREMENT,
                        MeasurementRepresentation.class)).thenReturn(meas);

        //when
        MeasurementRepresentation result = measurementApi.getMeasurement(gid);

        //then
        assertThat(result, sameInstance(meas));
    }

    @Test
    public void shouldDeleteMeasurementRep() throws SDKException {
        // Given
        String gidValue = "123";
        GId gid = new GId(gidValue);
        MeasurementRepresentation meas = new MeasurementRepresentation();
        meas.setId(gid);

        //when
        measurementApi.deleteMeasurement(meas);

        //then
        verify(restConnector).delete(MEASUREMENT_COLLECTION_URL + "/" + gidValue);
    }

    @Test
    public void shouldReturnMeasurements() throws SDKException {
        // Given
        PagedCollectionResource<MeasurementCollectionRepresentation> expected = new MeasurementCollectionImpl(restConnector,
                MEASUREMENT_COLLECTION_URL, DEAFAULT_PAGE_SIZE);

        // When
        PagedCollectionResource<MeasurementCollectionRepresentation> result = measurementApi.getMeasurements();

        // Then
        assertThat(result, is(expected));
    }

    @Test
    public void shouldReturnMeasurementsByEmptyFilter() throws SDKException {
        // Given
        PagedCollectionResource<MeasurementCollectionRepresentation> expected = new MeasurementCollectionImpl(restConnector,
                MEASUREMENT_COLLECTION_URL, DEAFAULT_PAGE_SIZE);

        // When
        PagedCollectionResource<MeasurementCollectionRepresentation> result = measurementApi
                .getMeasurementsByFilter(new MeasurementFilter());

        // Then
        assertThat(result, is(expected));
    }

    @Test
    public void shouldReturnMeasurementsByTypeFilter() throws Exception {
        // Given 
        measurementsApiRepresentation.setMeasurementsForType(TEMPLATE_URL);
        when(templateUrlParser.replacePlaceholdersWithParams(TEMPLATE_URL, singletonMap("type", TYPE))).thenReturn(EXACT_URL);

        PagedCollectionResource<MeasurementCollectionRepresentation> expected = new MeasurementCollectionImpl(restConnector, EXACT_URL,
                DEAFAULT_PAGE_SIZE);

        // When
        MeasurementFilter filter = new MeasurementFilter().byType(TYPE);
        PagedCollectionResource<MeasurementCollectionRepresentation> result = measurementApi.getMeasurementsByFilter(filter);

        // Then
        assertThat(result, is(expected));
    }

    @Test
    public void shouldReturnMeasurementsMeasurementsByDateFilter() throws SDKException {
        // Given 
        measurementsApiRepresentation.setMeasurementsForDate(TEMPLATE_URL);
        Map<String, String> params = new HashMap<String, String>();
        params.put("dateFrom", DateConverter.date2String(from));
        params.put("dateTo", DateConverter.date2String(to));
        when(templateUrlParser.replacePlaceholdersWithParams(TEMPLATE_URL, params)).thenReturn(EXACT_URL);

        PagedCollectionResource<MeasurementCollectionRepresentation> expected = new MeasurementCollectionImpl(restConnector, EXACT_URL,
                DEAFAULT_PAGE_SIZE);

        // When
        MeasurementFilter filter = new MeasurementFilter().byDate(from, to);
        PagedCollectionResource<MeasurementCollectionRepresentation> result = measurementApi.getMeasurementsByFilter(filter);

        // Then
        assertThat(result, is(expected));
    }

    @Test
    public void shouldReturnMeasurementsByDateAndFragmentTypeFilter() throws SDKException {
        // Given 
        measurementsApiRepresentation.setMeasurementsForDateAndFragmentType(TEMPLATE_URL);

        Map<String, String> params = new HashMap<String, String>();
        params.put("dateFrom", DateConverter.date2String(from));
        params.put("dateTo", DateConverter.date2String(to));
        params.put("fragmentType", ExtensibilityConverter.classToStringRepresentation(NonRelevantFragmentType.class));
        when(templateUrlParser.replacePlaceholdersWithParams(TEMPLATE_URL, params)).thenReturn(EXACT_URL);

        PagedCollectionResource<MeasurementCollectionRepresentation> expected = new MeasurementCollectionImpl(restConnector, EXACT_URL,
                DEAFAULT_PAGE_SIZE);

        // When
        MeasurementFilter filter = new MeasurementFilter().byFragmentType(NonRelevantFragmentType.class).byDate(from, to);
        PagedCollectionResource<MeasurementCollectionRepresentation> result = measurementApi.getMeasurementsByFilter(filter);

        // Then
        assertThat(result, is(expected));
    }

    @Test
    public void shouldReturnMeasurementsByDateAndTypeFilter() throws SDKException {
        // Given 
        measurementsApiRepresentation.setMeasurementsForDateAndType(TEMPLATE_URL);

        Map<String, String> params = new HashMap<String, String>();
        params.put("dateFrom", DateConverter.date2String(from));
        params.put("dateTo", DateConverter.date2String(to));
        params.put("type", TYPE);
        when(templateUrlParser.replacePlaceholdersWithParams(TEMPLATE_URL, params)).thenReturn(EXACT_URL);

        PagedCollectionResource<MeasurementCollectionRepresentation> expected = new MeasurementCollectionImpl(restConnector, EXACT_URL,
                DEAFAULT_PAGE_SIZE);

        // When
        MeasurementFilter filter = new MeasurementFilter().byDate(from, to).byType(TYPE);
        PagedCollectionResource<MeasurementCollectionRepresentation> result = measurementApi.getMeasurementsByFilter(filter);

        // Then
        assertThat(result, is(expected));
    }

    @Test
    public void shouldReturnMeasurementsByDateAndFragmentTypeAndTypeFilter() throws SDKException {
        // Given 
        measurementsApiRepresentation.setMeasurementsForDateAndFragmentTypeAndType(TEMPLATE_URL);

        Map<String, String> params = new HashMap<String, String>();
        params.put("dateFrom", DateConverter.date2String(from));
        params.put("dateTo", DateConverter.date2String(to));
        params.put("fragmentType", ExtensibilityConverter.classToStringRepresentation(NonRelevantFragmentType.class));
        params.put("type", TYPE);
        when(templateUrlParser.replacePlaceholdersWithParams(TEMPLATE_URL, params)).thenReturn(EXACT_URL);

        PagedCollectionResource<MeasurementCollectionRepresentation> expected = new MeasurementCollectionImpl(restConnector, EXACT_URL,
                DEAFAULT_PAGE_SIZE);

        // When
        MeasurementFilter filter = new MeasurementFilter().byDate(from, to).byFragmentType(NonRelevantFragmentType.class).byType(TYPE);
        PagedCollectionResource<MeasurementCollectionRepresentation> result = measurementApi.getMeasurementsByFilter(filter);

        // Then
        assertThat(result, is(expected));
    }

    @Test
    public void shouldReturnMeasurementsByFragmentTypeFilter() throws SDKException {
        // Given 
        measurementsApiRepresentation.setMeasurementsForFragmentType(TEMPLATE_URL);
        Map<String, String> params = new HashMap<String, String>();
        params.put("fragmentType", classToStringRepresentation(NonRelevantFragmentType.class));
        when(templateUrlParser.replacePlaceholdersWithParams(TEMPLATE_URL, params)).thenReturn(EXACT_URL);

        PagedCollectionResource<MeasurementCollectionRepresentation> expected = new MeasurementCollectionImpl(restConnector, EXACT_URL,
                DEAFAULT_PAGE_SIZE);

        // When
        MeasurementFilter filter = new MeasurementFilter().byFragmentType(NonRelevantFragmentType.class);
        PagedCollectionResource<MeasurementCollectionRepresentation> result = measurementApi.getMeasurementsByFilter(filter);

        // Then
        assertThat(result, is(expected));
    }

    @Test
    public void shouldReturnMeasurementsByFragmentTypeAndTypeFilter() throws SDKException {
        // Given 
        measurementsApiRepresentation.setMeasurementsForFragmentTypeAndType(TEMPLATE_URL);

        Map<String, String> params = new HashMap<String, String>();
        params.put("fragmentType", classToStringRepresentation(NonRelevantFragmentType.class));
        params.put("type", TYPE);
        when(templateUrlParser.replacePlaceholdersWithParams(TEMPLATE_URL, params)).thenReturn(EXACT_URL);

        PagedCollectionResource<MeasurementCollectionRepresentation> expected = new MeasurementCollectionImpl(restConnector, EXACT_URL,
                DEAFAULT_PAGE_SIZE);

        // When
        MeasurementFilter filter = new MeasurementFilter().byType(TYPE).byFragmentType(NonRelevantFragmentType.class);
        PagedCollectionResource<MeasurementCollectionRepresentation> result = measurementApi.getMeasurementsByFilter(filter);

        // Then
        assertThat(result, is(expected));
    }

    @Test
    public void shouldReturnMeasurementsBySourceFilter() throws SDKException {
        // Given 
        measurementsApiRepresentation.setMeasurementsForSource(TEMPLATE_URL);

        when(templateUrlParser.replacePlaceholdersWithParams(TEMPLATE_URL, singletonMap("source", SOURCE_GID))).thenReturn(EXACT_URL);

        PagedCollectionResource<MeasurementCollectionRepresentation> expected = new MeasurementCollectionImpl(restConnector, EXACT_URL,
                DEAFAULT_PAGE_SIZE);

        // When
        MeasurementFilter filter = new MeasurementFilter().bySource(source);
        PagedCollectionResource<MeasurementCollectionRepresentation> result = measurementApi.getMeasurementsByFilter(filter);

        // Then
        assertThat(result, is(expected));
    }

    @Test
    public void shouldReturnMeasurementsBySourceAndDateFilter() throws SDKException {
        // Given 
        measurementsApiRepresentation.setMeasurementsForSourceAndDate(TEMPLATE_URL);

        Map<String, String> params = new HashMap<String, String>();
        params.put("dateFrom", DateConverter.date2String(from));
        params.put("dateTo", DateConverter.date2String(to));
        params.put("source", SOURCE_GID);
        when(templateUrlParser.replacePlaceholdersWithParams(TEMPLATE_URL, params)).thenReturn(EXACT_URL);

        PagedCollectionResource<MeasurementCollectionRepresentation> expected = new MeasurementCollectionImpl(restConnector, EXACT_URL,
                DEAFAULT_PAGE_SIZE);

        // When
        MeasurementFilter filter = new MeasurementFilter().byDate(from, to).bySource(source);
        PagedCollectionResource<MeasurementCollectionRepresentation> result = measurementApi.getMeasurementsByFilter(filter);

        // Then
        assertThat(result, is(expected));
    }

    @Test
    public void shouldReturnMeasurementsBySourceAndDateAndFragmentTypeFilter() throws SDKException {
        // Given 
        measurementsApiRepresentation.setMeasurementsForSourceAndDateAndFragmentType(TEMPLATE_URL);

        Map<String, String> params = new HashMap<String, String>();
        params.put("dateFrom", DateConverter.date2String(from));
        params.put("dateTo", DateConverter.date2String(to));
        params.put("fragmentType", ExtensibilityConverter.classToStringRepresentation(NonRelevantFragmentType.class));
        params.put("source", SOURCE_GID);
        when(templateUrlParser.replacePlaceholdersWithParams(TEMPLATE_URL, params)).thenReturn(EXACT_URL);

        PagedCollectionResource<MeasurementCollectionRepresentation> expected = new MeasurementCollectionImpl(restConnector, EXACT_URL,
                DEAFAULT_PAGE_SIZE);

        // When
        MeasurementFilter filter = new MeasurementFilter().byDate(from, to).byFragmentType(NonRelevantFragmentType.class).bySource(source);
        PagedCollectionResource<MeasurementCollectionRepresentation> result = measurementApi.getMeasurementsByFilter(filter);

        // Then
        assertThat(result, is(expected));
    }

    @Test
    public void shouldReturnMeasurementsBySourceAndDateAndFragmentTypeAndTypeFilter() throws SDKException {
        // Given 
        measurementsApiRepresentation.setMeasurementsForSourceAndDateAndFragmentTypeAndType(TEMPLATE_URL);

        Map<String, String> params = new HashMap<String, String>();
        params.put("dateFrom", DateConverter.date2String(from));
        params.put("dateTo", DateConverter.date2String(to));
        params.put("fragmentType", ExtensibilityConverter.classToStringRepresentation(NonRelevantFragmentType.class));
        params.put("type", TYPE);
        params.put("source", SOURCE_GID);
        when(templateUrlParser.replacePlaceholdersWithParams(TEMPLATE_URL, params)).thenReturn(EXACT_URL);

        PagedCollectionResource<MeasurementCollectionRepresentation> expected = new MeasurementCollectionImpl(restConnector, EXACT_URL,
                DEAFAULT_PAGE_SIZE);

        // When
        MeasurementFilter filter = new MeasurementFilter().bySource(source).byDate(from, to).byFragmentType(NonRelevantFragmentType.class)
                .byType(TYPE);
        PagedCollectionResource<MeasurementCollectionRepresentation> result = measurementApi.getMeasurementsByFilter(filter);

        // Then
        assertThat(result, is(expected));
    }

    @Test
    public void shouldReturnMeasurementsBySourceAndDateAndTypeFilter() throws SDKException {
        // Given 
        measurementsApiRepresentation.setMeasurementsForSourceAndDateAndType(TEMPLATE_URL);

        Map<String, String> params = new HashMap<String, String>();
        params.put("dateFrom", DateConverter.date2String(from));
        params.put("dateTo", DateConverter.date2String(to));
        params.put("type", TYPE);
        params.put("source", SOURCE_GID);
        when(templateUrlParser.replacePlaceholdersWithParams(TEMPLATE_URL, params)).thenReturn(EXACT_URL);

        PagedCollectionResource<MeasurementCollectionRepresentation> expected = new MeasurementCollectionImpl(restConnector, EXACT_URL,
                DEAFAULT_PAGE_SIZE);

        // When
        MeasurementFilter filter = new MeasurementFilter().byDate(from, to).byType(TYPE).bySource(source);
        PagedCollectionResource<MeasurementCollectionRepresentation> result = measurementApi.getMeasurementsByFilter(filter);

        // Then
        assertThat(result, is(expected));
    }

    @Test
    public void shouldReturnMeasurementsBySourceAndFragmentTypeFilter() throws SDKException {
        // Given 
        measurementsApiRepresentation.setMeasurementsForSourceAndFragmentType(TEMPLATE_URL);

        Map<String, String> params = new HashMap<String, String>();
        params.put("fragmentType", ExtensibilityConverter.classToStringRepresentation(NonRelevantFragmentType.class));
        params.put("source", SOURCE_GID);
        when(templateUrlParser.replacePlaceholdersWithParams(TEMPLATE_URL, params)).thenReturn(EXACT_URL);

        PagedCollectionResource<MeasurementCollectionRepresentation> expected = new MeasurementCollectionImpl(restConnector, EXACT_URL,
                DEAFAULT_PAGE_SIZE);

        // When
        MeasurementFilter filter = new MeasurementFilter().bySource(source).byFragmentType(NonRelevantFragmentType.class);
        PagedCollectionResource<MeasurementCollectionRepresentation> result = measurementApi.getMeasurementsByFilter(filter);

        // Then
        assertThat(result, is(expected));
    }

    @Test
    public void shouldReturnMeasurementsBySourceAndTypeFilter() throws SDKException {
        // Given 
        measurementsApiRepresentation.setMeasurementsForSourceAndType(TEMPLATE_URL);

        Map<String, String> params = new HashMap<String, String>();
        params.put("type", TYPE);
        params.put("source", SOURCE_GID);
        when(templateUrlParser.replacePlaceholdersWithParams(TEMPLATE_URL, params)).thenReturn(EXACT_URL);

        PagedCollectionResource<MeasurementCollectionRepresentation> expected = new MeasurementCollectionImpl(restConnector, EXACT_URL,
                DEAFAULT_PAGE_SIZE);

        // When
        MeasurementFilter filter = new MeasurementFilter().bySource(source).byType(TYPE);
        PagedCollectionResource<MeasurementCollectionRepresentation> result = measurementApi.getMeasurementsByFilter(filter);

        // Then
        assertThat(result, is(expected));
    }

    @Test
    public void shouldReturnMeasurementsBySourceAndFragmentTypeAndTypeFilter() throws SDKException {
        // Given 
        measurementsApiRepresentation.setMeasurementsForSourceAndFragmentTypeAndType(TEMPLATE_URL);

        Map<String, String> params = new HashMap<String, String>();
        params.put("fragmentType", ExtensibilityConverter.classToStringRepresentation(NonRelevantFragmentType.class));
        params.put("type", TYPE);
        params.put("source", SOURCE_GID);
        when(templateUrlParser.replacePlaceholdersWithParams(TEMPLATE_URL, params)).thenReturn(EXACT_URL);

        PagedCollectionResource<MeasurementCollectionRepresentation> expected = new MeasurementCollectionImpl(restConnector, EXACT_URL,
                DEAFAULT_PAGE_SIZE);

        // When
        MeasurementFilter filter = new MeasurementFilter().bySource(source).byFragmentType(NonRelevantFragmentType.class).byType(TYPE);
        PagedCollectionResource<MeasurementCollectionRepresentation> result = measurementApi.getMeasurementsByFilter(filter);

        // Then
        assertThat(result, is(expected));
    }

    @Test
    public void testCreate() throws SDKException {
        //Given
        MeasurementRepresentation measurement = new MeasurementRepresentation();
        MeasurementRepresentation created = new MeasurementRepresentation();
        when(restConnector.post(MEASUREMENT_COLLECTION_URL, MeasurementMediaType.MEASUREMENT, measurement)).thenReturn(created);

        // When
        MeasurementRepresentation result = measurementApi.create(measurement);

        //then
        assertThat(result, sameInstance(created));
    }

    public static class NonRelevantFragmentType {
    }

}
