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
package com.cumulocity.sdk.agent.action;

import static com.cumulocity.model.operation.OperationStatus.EXECUTING;
import static com.cumulocity.model.operation.OperationStatus.FAILED;
import static com.cumulocity.model.operation.OperationStatus.SUCCESSFUL;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Answers;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.cumulocity.model.idtype.GId;
import com.cumulocity.model.operation.OperationStatus;
import com.cumulocity.rest.representation.operation.OperationRepresentation;
import com.cumulocity.sdk.agent.driver.DeviceDriver;
import com.cumulocity.sdk.agent.driver.DeviceException;
import com.cumulocity.sdk.agent.model.Device;
import com.cumulocity.sdk.agent.model.DevicesManagingAgent;
import com.cumulocity.sdk.client.Platform;
import com.cumulocity.sdk.client.SDKException;

public class ExecuteDeviceOperationsActionTest {

    private static final GId OPERATION_ID = new GId("opertion_id");

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private Platform platform;

    @Mock
    private OperationRepresentation operationRep;

    @Mock
    private DevicesManagingAgent<Device> agent;

    @Mock
    private DeviceDriver<Device> deviceDriver;

    private ExecuteDeviceOperationsAction<Device> action;

    private Queue<OperationRepresentation> queue = new ConcurrentLinkedQueue<OperationRepresentation>();

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        queue.add(operationRep);
        when(agent.getOperationsQueue()).thenReturn(queue);

        action = spy(new ExecuteDeviceOperationsAction<Device>(platform, agent));
        action.setDeviceDriver(deviceDriver);

        when(operationRep.getId()).thenReturn(OPERATION_ID);
        when(platform.getDeviceControlApi().getOperation(OPERATION_ID)).thenReturn(operationRep);
    }

    @After
    public void queueShouldBeEmpty() {
        assertThat(queue.size(), is(0));
    }

    @Test
    public void shouldBeInSuccessfulStateWhenOperationCanbeHandled() throws Exception {
        // given
        when(deviceDriver.isOperationSupported(operationRep)).thenReturn(true);

        // when
        action.run();

        // then
        assertThatStatusIsUpdated(EXECUTING, SUCCESSFUL);
        verify(deviceDriver).handleSupportedOperation(operationRep);
        verify(operationRep, never()).setFailureReason(anyString());
    }

    @Test
    public void shouldFailWhenOperationIsSupportedButCantBeHandled() throws Exception {
        // given
        DeviceException exception = new DeviceException("something went terribly wrong");
        doReturn(true).when(deviceDriver).isOperationSupported(operationRep);
        doThrow(exception).when(deviceDriver).handleSupportedOperation(operationRep);

        // when
        action.run();

        // then
        assertThatStatusIsUpdated(EXECUTING, FAILED);
        verify(operationRep).setFailureReason(exception.getMessage());
    }

    @Test
    public void shouldFailWhenOperationIsNotSupported() throws Exception {
        // given
        when(deviceDriver.isOperationSupported(operationRep)).thenReturn(false);

        // when
        action.run();

        // then
        verify(deviceDriver, never()).handleSupportedOperation(operationRep);
        assertThatStatusIsUpdated(EXECUTING, FAILED);
    }

    @Test
    public void shouldFailWhenUnableToUpdateOperationStatus() throws Exception {
        // given
        when(platform.getDeviceControlApi().update(operationRep)).thenThrow(new SDKException(""));

        // when
        action.run();

        // then
        verify(deviceDriver, never()).isOperationSupported(operationRep);
        verify(deviceDriver, never()).handleSupportedOperation(operationRep);
        assertThatStatusIsUpdated(EXECUTING, FAILED);
    }

    private void assertThatStatusIsUpdated(OperationStatus... statuses) throws Exception {
        InOrder inOrder = inOrder(platform.getDeviceControlApi(), operationRep);
        for (OperationStatus status : statuses) {
            inOrder.verify(operationRep).setStatus(status.toString());
            inOrder.verify(platform.getDeviceControlApi()).update(operationRep);
        }
    }
}
