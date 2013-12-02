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
package com.cumulocity.sdk.client.devicecontrol;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.sameInstance;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.argThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.hamcrest.CoreMatchers;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.cumulocity.model.idtype.GId;
import com.cumulocity.model.operation.OperationStatus;
import com.cumulocity.rest.representation.operation.DeviceControlMediaType;
import com.cumulocity.rest.representation.operation.DeviceControlRepresentation;
import com.cumulocity.rest.representation.operation.OperationCollectionRepresentation;
import com.cumulocity.rest.representation.operation.OperationRepresentation;
import com.cumulocity.sdk.client.PagedCollectionResource;
import com.cumulocity.sdk.client.RestConnector;
import com.cumulocity.sdk.client.SDKException;
import com.cumulocity.sdk.client.UrlProcessor;

public class DeviceControlApiImplTest {

    private static final String TEMPLATE_URL = "template_url";

    private static final String DEVICE_CONTROL_COLLECTION_URL = "http://abcd.com/devicecontrol/operations";

    private static final String DEVICE_ID = "123ABC";

    private static final int DEAFAULT_PAGE_SIZE = 7;

    private DeviceControlApi deviceControlApi;

    private DeviceControlRepresentation deviceControlApiRepresentation = new DeviceControlRepresentation();

    @Mock
    private RestConnector restConnector;

    @Mock
    private UrlProcessor urlProcessor;

    @Before
    public void setup() throws Exception {
        MockitoAnnotations.initMocks(this);

        OperationCollectionRepresentation ocr = new OperationCollectionRepresentation();
        ocr.setSelf(DEVICE_CONTROL_COLLECTION_URL);
        List<OperationRepresentation> opList = new LinkedList<OperationRepresentation>();
        ocr.setOperations(opList);
        deviceControlApiRepresentation.setOperations(ocr);

        deviceControlApi = new DeviceControlApiImpl(null, restConnector, urlProcessor, deviceControlApiRepresentation, DEAFAULT_PAGE_SIZE);
    }

    @Test
    public void shouldGetOperation() throws SDKException {
        // Given 
        GId gid = new GId("value");
        OperationRepresentation op = new OperationRepresentation();
        when(restConnector.get(DEVICE_CONTROL_COLLECTION_URL + "/value", DeviceControlMediaType.OPERATION, OperationRepresentation.class))
                .thenReturn(op);

        // When
        OperationRepresentation retrieved = deviceControlApi.getOperation(gid);

        // Then
        assertThat(retrieved, sameInstance(op));
    }

    @Test
    public void shouldCreateOperation() throws Exception {
        //Given
        OperationRepresentation operation = new OperationRepresentation();
        OperationRepresentation created = new OperationRepresentation();

        when(restConnector.post(DEVICE_CONTROL_COLLECTION_URL, DeviceControlMediaType.OPERATION, operation)).thenReturn(created);

        //when
        OperationRepresentation result = deviceControlApi.create(operation);

        // then 
        assertThat(result, sameInstance(created));
    }

    @Test
    public void shouldUpdateOperation() throws SDKException {
        //Given
        OperationRepresentation op = new OperationRepresentation();
        op.setStatus(OperationStatus.EXECUTING.toString());
        op.setId(new GId("myId"));
        op.setCreationTime(new Date());
        OperationRepresentation updated = new OperationRepresentation();
        when(restConnector.put(eq(DEVICE_CONTROL_COLLECTION_URL + "/myId"), eq(DeviceControlMediaType.OPERATION),
                argThat(hasOnlyUpdateFields(op)))).thenReturn(updated);

        //when
        OperationRepresentation result = deviceControlApi.update(op);

        // then 
        assertThat(result, sameInstance(updated));
    }

    @Test
    public void shouldGetOperations() throws Exception {
        //Given
        OperationCollection expected = new OperationCollectionImpl(restConnector,
                DEVICE_CONTROL_COLLECTION_URL, DEAFAULT_PAGE_SIZE);

        // When
        OperationCollection result = deviceControlApi.getOperations();

        // Then
        assertThat(result, is(expected));
    }

    @Test
    public void shouldGetOperationsByEmptyFilter() throws Exception {
        //Given
        when(urlProcessor.replaceOrAddQueryParam(DEVICE_CONTROL_COLLECTION_URL, Collections.<String, String>emptyMap())).thenReturn(DEVICE_CONTROL_COLLECTION_URL);
        OperationCollection expected = new OperationCollectionImpl(restConnector,
                DEVICE_CONTROL_COLLECTION_URL, DEAFAULT_PAGE_SIZE);

        // When
        OperationFilter filter = new OperationFilter();
        OperationCollection result = deviceControlApi.getOperationsByFilter(filter);

        // Then
        assertThat(result, is(expected));
    }

    @Test
    public void shouldGetOperationsByDeviceFilter() throws Exception {
        // Given
        OperationFilter filter = new OperationFilter().byDevice(DEVICE_ID);
        deviceControlApiRepresentation.setOperationsByDeviceId(TEMPLATE_URL);
        String operationsByDeviceIdUrl = DEVICE_CONTROL_COLLECTION_URL + "?deviceId="+ DEVICE_ID;
        when(urlProcessor.replaceOrAddQueryParam(DEVICE_CONTROL_COLLECTION_URL, filter.getQueryParams())).thenReturn(operationsByDeviceIdUrl);

        OperationCollection expected = new OperationCollectionImpl(restConnector, operationsByDeviceIdUrl,
                DEAFAULT_PAGE_SIZE);

        // When
        OperationCollection result = deviceControlApi.getOperationsByFilter(filter);

        // Then
        assertThat(result, is(expected));
    }

    private Matcher<OperationRepresentation> hasOnlyUpdateFields(final OperationRepresentation op) {
        return new TypeSafeMatcher<OperationRepresentation>() {

            @Override
            public void describeTo(Description description) {
                description.appendText("an operationRep representation having only updatable fields, as ").appendValue(op);
            }

            @Override
            public boolean matchesSafely(OperationRepresentation item) {
                CoreMatchers.nullValue().matches(item.getDeviceExternalIDs());
                return item.getDeviceExternalIDs() == null &&
                        item.getId() == null &&
                        item.getCreationTime() == null &&
                        item.getStatus().equals(op.getStatus()) &&
                        (OperationStatus.FAILED.name().equals(item.getStatus()) ? item.getFailureReason().equals(op.getFailureReason()) : item.getFailureReason() == null) &&
                        item.getAttrs().equals(op.getAttrs());
            }
        };
    }

}
