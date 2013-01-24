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

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Answers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.cumulocity.rest.representation.measurement.MeasurementRepresentation;
import com.cumulocity.sdk.agent.model.DevicesManagingAgent;
import com.cumulocity.sdk.client.Platform;

public class UploadDeviceMeasurementsActionTest {

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private Platform platform;

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private DevicesManagingAgent<?> agent;

    private UploadDeviceMeasurementsAction testObj;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        testObj = new UploadDeviceMeasurementsAction(platform, agent);
    }

    @Test
    public void testPerformStartupAction() throws Exception {
        //given
        Queue<MeasurementRepresentation> queue = new ConcurrentLinkedQueue<MeasurementRepresentation>();
        MeasurementRepresentation mr = new MeasurementRepresentation();
        queue.add(mr);
        when(agent.getMeasurementsQueue()).thenReturn(queue);

        //when
        testObj.run();

        //then
        verify(platform.getMeasurementApi(), times(1)).create(mr);
    }
}
