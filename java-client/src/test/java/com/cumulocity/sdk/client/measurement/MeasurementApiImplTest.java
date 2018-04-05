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

import com.cumulocity.model.idtype.GId;
import com.cumulocity.rest.representation.inventory.ManagedObjectRepresentation;
import com.cumulocity.rest.representation.measurement.MeasurementCollectionRepresentation;
import com.cumulocity.rest.representation.measurement.MeasurementMediaType;
import com.cumulocity.rest.representation.measurement.MeasurementRepresentation;
import com.cumulocity.rest.representation.measurement.MeasurementsApiRepresentation;
import com.cumulocity.sdk.client.RestConnector;
import com.cumulocity.sdk.client.SDKException;
import com.cumulocity.sdk.client.UrlProcessor;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.sameInstance;
import static org.mockito.Mockito.*;

public class MeasurementApiImplTest {

    private static final String SOURCE_GID = "gid1";

    private static final String MEASUREMENT_COLLECTION_URL = "path_to_measurement";

    private static final String TEMPLATE_URL = "template_url";

    private static final String TYPE = "type1";

    private static final int DEAFAULT_PAGE_SIZE = 11;

    private MeasurementApi measurementApi;

    private MeasurementsApiRepresentation measurementsApiRepresentation = new MeasurementsApiRepresentation();

    private MeasurementCollectionRepresentation measurementCollectionRepresentation = new MeasurementCollectionRepresentation();

    private ManagedObjectRepresentation source = new ManagedObjectRepresentation();

    @Mock
    private RestConnector restConnector;

    @Mock
    private UrlProcessor urlProcessor;

    @Before
    public void setup() throws Exception {
        MockitoAnnotations.initMocks(this);
        measurementApi = new MeasurementApiImpl(restConnector, urlProcessor, measurementsApiRepresentation, DEAFAULT_PAGE_SIZE);
        source.setId(new GId(SOURCE_GID));
        measurementCollectionRepresentation.setSelf(MEASUREMENT_COLLECTION_URL);
        measurementsApiRepresentation.setMeasurements(measurementCollectionRepresentation);
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
    public void shouldDeleteByTypeFilter() throws Exception {
        // Given
        MeasurementFilter typeFilter = new MeasurementFilter().byType(TYPE);
        String measurementsByTypeUrl = MEASUREMENT_COLLECTION_URL + "?type=" + TYPE;
        when(urlProcessor.replaceOrAddQueryParam(MEASUREMENT_COLLECTION_URL, typeFilter.getQueryParams()))
                .thenReturn(measurementsByTypeUrl);

        // When
        measurementApi.deleteMeasurementsByFilter(typeFilter);

        // Then
        verify(restConnector, times(1)).delete(measurementsByTypeUrl);
    }

    @Test
    public void shouldDeleteByEmptyFilter() throws Exception {
        // Given
        MeasurementFilter emptyFilter = new MeasurementFilter();
        String measurementsUrl = MEASUREMENT_COLLECTION_URL;
        when(urlProcessor.replaceOrAddQueryParam(MEASUREMENT_COLLECTION_URL, emptyFilter.getQueryParams()))
                .thenReturn(measurementsUrl);

        // When
        measurementApi.deleteMeasurementsByFilter(emptyFilter);

        // Then
        verify(restConnector, times(1)).delete(measurementsUrl);
    }

    @Test(expected=IllegalArgumentException.class)
    public void testDeleteByNullFilter() {
        measurementApi.deleteMeasurementsByFilter(null);
    }

    @Test
    public void shouldReturnMeasurements() throws SDKException {
        // Given
        MeasurementCollection expected = new MeasurementCollectionImpl(restConnector,
                MEASUREMENT_COLLECTION_URL, DEAFAULT_PAGE_SIZE);

        // When
        MeasurementCollection result = measurementApi.getMeasurements();

        // Then
        assertThat(result, is(expected));
    }

    @Test
    public void shouldReturnMeasurementsByEmptyFilter() throws SDKException {
        // Given
        when(urlProcessor.replaceOrAddQueryParam(MEASUREMENT_COLLECTION_URL, Collections.<String, String>emptyMap())).thenReturn(MEASUREMENT_COLLECTION_URL);
        MeasurementCollection expected = new MeasurementCollectionImpl(restConnector,
                MEASUREMENT_COLLECTION_URL, DEAFAULT_PAGE_SIZE);

        // When
        MeasurementCollection result = measurementApi
                .getMeasurementsByFilter(new MeasurementFilter());

        // Then
        assertThat(result, is(expected));
    }

    @Test
    public void shouldReturnMeasurementsByTypeFilter() throws Exception {
        // Given 
        MeasurementFilter filter = new MeasurementFilter().byType(TYPE);
        measurementsApiRepresentation.setMeasurementsForType(TEMPLATE_URL);
        String measurementsByTypeUrl = MEASUREMENT_COLLECTION_URL + "?type=" + TYPE;
        when(urlProcessor.replaceOrAddQueryParam(MEASUREMENT_COLLECTION_URL, filter.getQueryParams())).thenReturn(measurementsByTypeUrl);

        MeasurementCollection expected = new MeasurementCollectionImpl(restConnector, measurementsByTypeUrl,
                DEAFAULT_PAGE_SIZE);

        // When
        MeasurementCollection result = measurementApi.getMeasurementsByFilter(filter);

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

    @Test
    public void shouldCreateBulk() throws SDKException {
        //Given
        MeasurementCollectionRepresentation toCreate = new MeasurementCollectionRepresentation();
        MeasurementCollectionRepresentation created = new MeasurementCollectionRepresentation();
        when(restConnector.post(MEASUREMENT_COLLECTION_URL, MeasurementMediaType.MEASUREMENT_COLLECTION, toCreate)).thenReturn(created);

        // When
        MeasurementCollectionRepresentation result = measurementApi.createBulk(toCreate);

        //then
        assertThat(result, sameInstance(created));
    }

    @Test
    public void shouldCreateBulkWithoutResponse() throws SDKException {
        //Given
        MeasurementCollectionRepresentation toCreate = new MeasurementCollectionRepresentation();

        // When
        measurementApi.createBulkWithoutResponse(toCreate);

        //then
        verify(restConnector).postWithoutResponse(MEASUREMENT_COLLECTION_URL, MeasurementMediaType.MEASUREMENT_COLLECTION, toCreate);
    }

    public static class NonRelevantFragmentType {
    }

}
