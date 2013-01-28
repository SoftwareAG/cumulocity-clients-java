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

import static java.util.Collections.singletonMap;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.sameInstance;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.argThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.RETURNS_SMART_NULLS;
import static org.mockito.Mockito.when;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

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
import com.cumulocity.rest.representation.platform.PlatformApiRepresentation;
import com.cumulocity.rest.representation.platform.PlatformMediaType;
import com.cumulocity.sdk.client.PagedCollectionResource;
import com.cumulocity.sdk.client.RestConnector;
import com.cumulocity.sdk.client.SDKException;
import com.cumulocity.sdk.client.TemplateUrlParser;

public class DeviceControlApiImplTest {

    private static final OperationStatus STATUS = OperationStatus.FAILED;

    private static final String EXACT_URL = "exact_url";

    private static final String TEMPLATE_URL = "template_url";

    private static final String DEVICE_CONTROL_COLLECTION_URL = "http://abcd.com/devicecontrol/operations";

    private static final String AGENT_ID = "AgentABC";

    private static final String DEVICE_ID = "123ABC";

    private static final String PLATFORM_API_URL = "platform_api_url";

    private static final int DEAFAULT_PAGE_SIZE = 7;

    private DeviceControlApi deviceControlApi;

    private DeviceControlRepresentation deviceControlApiRepresentation = new DeviceControlRepresentation();

    @Mock
    private RestConnector restConnector;

    @Mock
    private TemplateUrlParser templateUrlParser;

    @Before
    public void setup() throws Exception {
        MockitoAnnotations.initMocks(this);
        deviceControlApi = new DeviceControlApiImpl(restConnector, templateUrlParser, PLATFORM_API_URL, DEAFAULT_PAGE_SIZE);

        OperationCollectionRepresentation ocr = new OperationCollectionRepresentation();
        ocr.setSelf(DEVICE_CONTROL_COLLECTION_URL);
        List<OperationRepresentation> opList = new LinkedList<OperationRepresentation>();
        OperationRepresentation or = new OperationRepresentation();
        opList.add(or);
        ocr.setOperations(opList);
        deviceControlApiRepresentation.setOperations(ocr);
        PlatformApiRepresentation platformApiRepresentation = new PlatformApiRepresentation();
        platformApiRepresentation.setDeviceControl(deviceControlApiRepresentation);
        
        when(restConnector.get(PLATFORM_API_URL, PlatformMediaType.PLATFORM_API, PlatformApiRepresentation.class))
                .thenReturn(platformApiRepresentation);
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
        PagedCollectionResource<OperationCollectionRepresentation> expected = new OperationCollectionImpl(restConnector,
                DEVICE_CONTROL_COLLECTION_URL, DEAFAULT_PAGE_SIZE);

        // When
        PagedCollectionResource<OperationCollectionRepresentation> result = deviceControlApi.getOperations();

        // Then
        assertThat(result, is(expected));
    }

    @Test
    public void shouldGetOperationsByEmptyFilter() throws Exception {
        //Given
        PagedCollectionResource<OperationCollectionRepresentation> expected = new OperationCollectionImpl(restConnector,
                DEVICE_CONTROL_COLLECTION_URL, DEAFAULT_PAGE_SIZE);

        // When
        OperationFilter filter = new OperationFilter();
        PagedCollectionResource<OperationCollectionRepresentation> result = deviceControlApi.getOperationsByFilter(filter);

        // Then
        assertThat(result, is(expected));
    }

    @Test
    public void shouldGetOperationsByAgentFilter() throws Exception {
        // Given
        deviceControlApiRepresentation.setOperationsByAgentId(TEMPLATE_URL);
        when(templateUrlParser.replacePlaceholdersWithParams(TEMPLATE_URL, singletonMap("agentId", AGENT_ID))).thenReturn(EXACT_URL);

        PagedCollectionResource<OperationCollectionRepresentation> expected = new OperationCollectionImpl(restConnector, EXACT_URL,
                DEAFAULT_PAGE_SIZE);

        // When
        OperationFilter filter = new OperationFilter().byAgent(AGENT_ID);
        PagedCollectionResource<OperationCollectionRepresentation> result = deviceControlApi.getOperationsByFilter(filter);

        // Then
        assertThat(result, is(expected));
    }

    @Test
    public void shouldGetOperationsByDeviceFilter() throws Exception {
        // Given
        deviceControlApiRepresentation.setOperationsByDeviceId(TEMPLATE_URL);
        when(templateUrlParser.replacePlaceholdersWithParams(TEMPLATE_URL, singletonMap("deviceId", DEVICE_ID))).thenReturn(EXACT_URL);

        PagedCollectionResource<OperationCollectionRepresentation> expected = new OperationCollectionImpl(restConnector, EXACT_URL,
                DEAFAULT_PAGE_SIZE);

        // When
        OperationFilter filter = new OperationFilter().byDevice(DEVICE_ID);
        PagedCollectionResource<OperationCollectionRepresentation> result = deviceControlApi.getOperationsByFilter(filter);

        // Then
        assertThat(result, is(expected));
    }

    @Test
    public void shouldGetOperationsByAgentAndStatusFilter() throws Exception {
        // Given
        deviceControlApiRepresentation.setOperationsByAgentIdAndStatus(TEMPLATE_URL);
        Map<String, String> params = new HashMap<String, String>();
        params.put("status", STATUS.toString());
        params.put("agentId", AGENT_ID);
        when(templateUrlParser.replacePlaceholdersWithParams(TEMPLATE_URL, params)).thenReturn(EXACT_URL);

        PagedCollectionResource<OperationCollectionRepresentation> expected = new OperationCollectionImpl(restConnector, EXACT_URL,
                DEAFAULT_PAGE_SIZE);

        // When
        OperationFilter filter = new OperationFilter().byStatus(STATUS).byAgent(AGENT_ID);
        PagedCollectionResource<OperationCollectionRepresentation> result = deviceControlApi.getOperationsByFilter(filter);

        // Then
        assertThat(result, is(expected));
    }

    @Test
    public void shouldGetOperationsByDeviceAndStatusFilter() throws Exception {
        // Given
        deviceControlApiRepresentation.setOperationsByDeviceIdAndStatus(TEMPLATE_URL);
        Map<String, String> params = new HashMap<String, String>();
        params.put("status", STATUS.toString());
        params.put("deviceId", DEVICE_ID);
        when(templateUrlParser.replacePlaceholdersWithParams(TEMPLATE_URL, params)).thenReturn(EXACT_URL);

        PagedCollectionResource<OperationCollectionRepresentation> expected = new OperationCollectionImpl(restConnector, EXACT_URL,
                DEAFAULT_PAGE_SIZE);

        // When
        OperationFilter filter = new OperationFilter().byStatus(STATUS).byDevice(DEVICE_ID);
        PagedCollectionResource<OperationCollectionRepresentation> result = deviceControlApi.getOperationsByFilter(filter);

        // Then
        assertThat(result, is(expected));
    }

    @Test
    public void shouldGetOperationsByStatusFilter() throws Exception {
        // Given
        deviceControlApiRepresentation.setOperationsByStatus(TEMPLATE_URL);
        when(templateUrlParser.replacePlaceholdersWithParams(TEMPLATE_URL, singletonMap("status", STATUS.toString())))
                .thenReturn(EXACT_URL);

        PagedCollectionResource<OperationCollectionRepresentation> expected = new OperationCollectionImpl(restConnector, EXACT_URL,
                DEAFAULT_PAGE_SIZE);

        // When
        OperationFilter filter = new OperationFilter().byStatus(STATUS);
        PagedCollectionResource<OperationCollectionRepresentation> result = deviceControlApi.getOperationsByFilter(filter);

        // Then
        assertThat(result, is(expected));
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowUnsupportedOperationExceptionWhenSearchingByAgentAndDeviceAndStatusFilter() throws Exception {
        // When
        OperationFilter filter = new OperationFilter().byAgent("").byDevice("").byStatus(STATUS);
        deviceControlApi.getOperationsByFilter(filter);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowUnsupportedOperationExceptionWhenSearchingByAgentAndDeviceFilter() throws Exception {
        // When
        OperationFilter filter = new OperationFilter().byAgent("").byDevice("");
        deviceControlApi.getOperationsByFilter(filter);
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
