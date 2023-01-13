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

import com.cumulocity.model.idtype.GId;
import com.cumulocity.model.operation.OperationStatus;
import com.cumulocity.rest.representation.operation.DeviceControlMediaType;
import com.cumulocity.rest.representation.operation.DeviceControlRepresentation;
import com.cumulocity.rest.representation.operation.OperationCollectionRepresentation;
import com.cumulocity.rest.representation.operation.OperationRepresentation;
import com.cumulocity.sdk.client.RestConnector;
import com.cumulocity.sdk.client.SDKException;
import com.cumulocity.sdk.client.UrlProcessor;
import org.hamcrest.CoreMatchers;
import org.joda.time.DateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatcher;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.argThat;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.when;

public class DeviceControlApiImplTest {

    private static final String TEMPLATE_URL = "template_url";

    private static final String DEVICE_CONTROL_COLLECTION_URL = "http://abcd.com/devicecontrol/operations";

    private static final String DEVICE_ID = "123ABC";

    private static final int DEFAULT_PAGE_SIZE = 7;

    private DeviceControlApi deviceControlApi;

    private DeviceControlRepresentation deviceControlApiRepresentation = new DeviceControlRepresentation();

    @Mock
    private RestConnector restConnector;

    @Mock
    private UrlProcessor urlProcessor;

    @BeforeEach
    public void setup() throws Exception {
        MockitoAnnotations.initMocks(this);

        OperationCollectionRepresentation ocr = new OperationCollectionRepresentation();
        ocr.setSelf(DEVICE_CONTROL_COLLECTION_URL);
        List<OperationRepresentation> opList = new LinkedList<OperationRepresentation>();
        ocr.setOperations(opList);
        deviceControlApiRepresentation.setOperations(ocr);

        deviceControlApi = new DeviceControlApiImpl(null, restConnector, urlProcessor, deviceControlApiRepresentation, DEFAULT_PAGE_SIZE);
    }

    @Test
    public void shouldGetOperation() throws SDKException {
        //given
        GId gid = new GId("value");
        OperationRepresentation op = new OperationRepresentation();
        when(restConnector.get(DEVICE_CONTROL_COLLECTION_URL + "/value", DeviceControlMediaType.OPERATION, OperationRepresentation.class))
                .thenReturn(op);

        //when
        OperationRepresentation retrieved = deviceControlApi.getOperation(gid);

        //then
        assertThat(retrieved).isSameAs(op);
    }

    @Test
    public void shouldCreateOperation() {
        //given
        OperationRepresentation operation = new OperationRepresentation();
        OperationRepresentation created = new OperationRepresentation();

        when(restConnector.post(DEVICE_CONTROL_COLLECTION_URL, DeviceControlMediaType.OPERATION, operation)).thenReturn(created);

        //when
        OperationRepresentation result = deviceControlApi.create(operation);

        //then
        assertThat(result).isSameAs(created);
    }

    @Test
    public void shouldUpdateOperation() throws SDKException {
        //given
        OperationRepresentation op = new OperationRepresentation();
        op.setStatus(OperationStatus.EXECUTING.toString());
        op.setId(new GId("myId"));
        op.setCreationDateTime(new DateTime());
        OperationRepresentation updated = new OperationRepresentation();
        when(restConnector.put(eq(DEVICE_CONTROL_COLLECTION_URL + "/myId"), eq(DeviceControlMediaType.OPERATION),
                argThat(hasOnlyUpdateFields(op)))).thenReturn(updated);

        //when
        OperationRepresentation result = deviceControlApi.update(op);

        //then
        assertThat(result).isSameAs(updated);
    }

    @Test
    public void shouldGetOperations() {
        //given
        OperationCollection expected = new OperationCollectionImpl(restConnector, DEVICE_CONTROL_COLLECTION_URL, DEFAULT_PAGE_SIZE);

        //when
        OperationCollection result = deviceControlApi.getOperations();

        //then
        assertThat(result).isEqualTo(expected);
    }

    @Test
    public void shouldGetOperationsByEmptyFilter() {
        //given
        when(urlProcessor.replaceOrAddQueryParam(DEVICE_CONTROL_COLLECTION_URL, Collections.<String, String>emptyMap())).thenReturn(DEVICE_CONTROL_COLLECTION_URL);
        OperationCollection expected = new OperationCollectionImpl(restConnector,
                DEVICE_CONTROL_COLLECTION_URL, DEFAULT_PAGE_SIZE);

        //when
        OperationFilter filter = new OperationFilter();
        OperationCollection result = deviceControlApi.getOperationsByFilter(filter);

        //then
        assertThat(result).isEqualTo(expected);
    }

    @Test
    public void shouldGetOperationsByDeviceFilter() {
        //given
        OperationFilter filter = new OperationFilter().byDevice(DEVICE_ID);
        deviceControlApiRepresentation.setOperationsByDeviceId(TEMPLATE_URL);
        String operationsByDeviceIdUrl = DEVICE_CONTROL_COLLECTION_URL + "?deviceId="+ DEVICE_ID;
        when(urlProcessor.replaceOrAddQueryParam(DEVICE_CONTROL_COLLECTION_URL, filter.getQueryParams())).thenReturn(operationsByDeviceIdUrl);

        OperationCollection expected = new OperationCollectionImpl(restConnector, operationsByDeviceIdUrl,
                DEFAULT_PAGE_SIZE);

        //when
        OperationCollection result = deviceControlApi.getOperationsByFilter(filter);

        //then
        assertThat(result).isEqualTo(expected);
    }

    private ArgumentMatcher<OperationRepresentation> hasOnlyUpdateFields(final OperationRepresentation op) {
        return new ArgumentMatcher<OperationRepresentation>() {

            @Override
            public String toString() {
                return "an operationRep representation having only updatable fields, as " +op;
            }

            @Override
            public boolean matches(OperationRepresentation item) {
                CoreMatchers.nullValue().matches(item.getDeviceExternalIDs());
                return item.getDeviceExternalIDs() == null &&
                        item.getId() == null &&
                        item.getCreationDateTime() == null &&
                        item.getStatus().equals(op.getStatus()) &&
                        (OperationStatus.FAILED.name().equals(item.getStatus()) ? item.getFailureReason().equals(op.getFailureReason()) : item.getFailureReason() == null) &&
                        item.getAttrs().equals(op.getAttrs());
            }

        };
    }

}
