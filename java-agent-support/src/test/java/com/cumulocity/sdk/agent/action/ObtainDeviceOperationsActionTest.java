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

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Answers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.cumulocity.model.idtype.GId;
import com.cumulocity.model.operation.OperationStatus;
import com.cumulocity.rest.representation.operation.OperationCollectionRepresentation;
import com.cumulocity.rest.representation.operation.OperationRepresentation;
import com.cumulocity.sdk.agent.model.DevicesManagingAgent;
import com.cumulocity.sdk.client.PagedCollectionResource;
import com.cumulocity.sdk.client.Platform;
import com.cumulocity.sdk.client.devicecontrol.OperationFilter;

public class ObtainDeviceOperationsActionTest {

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private Platform platform;

    @Mock
    private DevicesManagingAgent<?> agent;

    @Mock
    private PagedCollectionResource<OperationCollectionRepresentation> operationCollection;

    private Queue<OperationRepresentation> queue = new LinkedList<OperationRepresentation>();

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testRun() throws Exception {
        // given
        when(agent.getOperationsQueue()).thenReturn(queue);
        when(platform.getDeviceControlApi().getOperationsByFilter(any(OperationFilter.class))).thenReturn(operationCollection);
        when(agent.getGlobalId()).thenReturn(new GId("test"));
        OperationRepresentation or = new OperationRepresentation();
        when(operationCollection.get()).thenReturn(asOperationCollection(or));

        // when
        new ObtainDeviceOperationsAction(platform, agent).run();

        // then
        assertThat(queue.size(), is(1));
        assertSame(or, queue.peek());
        verify(platform.getDeviceControlApi()).getOperationsByFilter(argThat(matchesAgentAndStatus("test", OperationStatus.PENDING)));
    }

    private Matcher<OperationFilter> matchesAgentAndStatus(final String agentId, final OperationStatus status) {
        return new TypeSafeMatcher<OperationFilter>() {

            @Override
            public void describeTo(Description desc) {
                desc.appendText("OperationFilter is not matching expected agentId and status");
            }

            @Override
            public boolean matchesSafely(OperationFilter item) {
                if (agentId.equals(item.getAgent()) && status.equals(item.getStatus())) {
                    return true;
                }

                return false;
            }
        };
    }

    private static OperationCollectionRepresentation asOperationCollection(OperationRepresentation or) {
        OperationCollectionRepresentation ocr = new OperationCollectionRepresentation();
        ocr.setOperations(Arrays.asList(or));
        return ocr;
    }
}
