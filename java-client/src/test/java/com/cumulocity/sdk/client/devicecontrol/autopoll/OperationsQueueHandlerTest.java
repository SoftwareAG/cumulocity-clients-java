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
package com.cumulocity.sdk.client.devicecontrol.autopoll;

import com.cumulocity.model.idtype.GId;
import com.cumulocity.model.operation.OperationStatus;
import com.cumulocity.rest.representation.operation.OperationRepresentation;
import com.cumulocity.sdk.client.devicecontrol.DeviceControlApi;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatcher;
import org.mockito.InOrder;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.LinkedList;
import java.util.List;

import static com.jayway.awaitility.Awaitility.await;
import static com.jayway.awaitility.Awaitility.to;
import static com.jayway.awaitility.Duration.TWO_SECONDS;
import static java.util.Arrays.asList;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class OperationsQueueHandlerTest {

    private static final int TWO_SECONDS_IN_MILLIS = 2000;

    private OperationsQueueHandler testObj;

    private OperationProcessor operationProcessor;

    private OperationsQueue queue;

    private DeviceControlApi deviceControlApi;

    private OperationRepresentation operationRep;

    private List<String> statusesRecordedOnDeviceControlApiMock = new LinkedList<String>();

    @Before
    public void setUp() throws Exception {
        operationProcessor = mock(OperationProcessor.class);
        deviceControlApi = mock(DeviceControlApi.class);
        operationRep = mock(OperationRepresentation.class);
        queue = new OperationsQueue();
        testObj = new OperationsQueueHandler(operationProcessor, queue, deviceControlApi);
        testObj.queuePollTimeOut = 200;

        when(deviceControlApi.getOperation(any(GId.class))).thenReturn(operationRep);
        when(deviceControlApi.update(any(OperationRepresentation.class))).thenAnswer(new Answer<OperationRepresentation>() {
            @Override
            public OperationRepresentation answer(InvocationOnMock invocation) throws Throwable {
                OperationRepresentation passedOperation = (OperationRepresentation) invocation.getArguments()[0];
                statusesRecordedOnDeviceControlApiMock.add(passedOperation.getStatus());
                return passedOperation;
            }
        });
    }

    @Test
    public void testOperationProcessingSuccessful() throws Exception {
        //GIVEN
        GId id = new GId("op");

        OperationRepresentation op = new OperationRepresentation();
        op.setId(id);
        queue.add(op);
        when(operationProcessor.process(op)).thenReturn(true);

        //WHEN
        testObj.start();

        //THEN
        // Mockito 1.8.5: Timeout is not implemented to work with InOrder;
        verify(deviceControlApi, timeout(TWO_SECONDS_IN_MILLIS).times(2)).update(argThat(hasId(id)));

        InOrder inOrder = inOrder(deviceControlApi, operationProcessor);
        inOrder.verify(deviceControlApi).update(argThat(hasId(id)));
        inOrder.verify(operationProcessor).process(argThat(hasId(id)));
        inOrder.verify(deviceControlApi).update(argThat(hasId(id)));

        // checking that correct statuses were set
        assertThat(statusesRecordedOnDeviceControlApiMock,
                is(asList(OperationStatus.EXECUTING.toString(), OperationStatus.SUCCESSFUL.toString())));
    }

    @Test
    public void testOperationProcessingFailed() throws Exception {
        //GIVEN
        GId id = new GId("op");
        OperationRepresentation op = new OperationRepresentation();
        op.setId(id);
        queue.add(op);
        when(operationProcessor.process(op)).thenReturn(false);

        //WHEN
        testObj.start();

        //THEN
        // Mockito 1.8.5: Timeout is not implemented to work with InOrder;
        verify(deviceControlApi, timeout(TWO_SECONDS_IN_MILLIS).times(2)).update(argThat(hasId(id)));

        InOrder inOrder = inOrder(deviceControlApi, operationProcessor);
        inOrder.verify(deviceControlApi).update(argThat(hasId(id)));
        inOrder.verify(operationProcessor).process(argThat(hasId(id)));
        inOrder.verify(deviceControlApi).update(argThat(hasId(id)));

        // checking that correct statuses were set
        assertThat(statusesRecordedOnDeviceControlApiMock,
                is(asList(OperationStatus.EXECUTING.toString(), OperationStatus.FAILED.toString())));
    }

    @Test
    public void testStartStop() throws Exception {
        // Given
        OperationRepresentation op = new OperationRepresentation();
        op.setId(new GId("op"));

        // When
        queue.add(op);

        // Then
        assertThat(queue.size(), equalTo(1));

        // When
        testObj.start();
        await().atMost(TWO_SECONDS).untilCall(to(testObj).isRunning(), is(true));

        // Then
        await().atMost(TWO_SECONDS).until(queue::size, is(0));

        // When
        testObj.stop();
        await().atMost(TWO_SECONDS).untilCall(to(testObj).isRunning(), is(false));
        queue.add(op);

        // Then
        await().atMost(TWO_SECONDS).until(queue::size, is(1));

        // When
        testObj.start();

        // Then
        await().atMost(TWO_SECONDS).until(queue::size, is(0));
    }

    private ArgumentMatcher<OperationRepresentation> hasId(final GId id) {
        return new ArgumentMatcher<OperationRepresentation>() {
            @Override
            public boolean matches(OperationRepresentation argument) {
                return argument.getId().equals(id);
            }

            @Override
            public String toString() {
                return "Operation representation should match id " + id;
            }
        };
    }
}
