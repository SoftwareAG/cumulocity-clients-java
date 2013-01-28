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

import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

import java.util.LinkedList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.cumulocity.rest.representation.operation.OperationCollectionRepresentation;
import com.cumulocity.rest.representation.operation.OperationRepresentation;
import com.cumulocity.sdk.client.PagedCollectionResource;

public class OperationsPollingTaskTest {
    OperationsPollingTask testObj;

    @Mock
    PagedCollectionResource<OperationCollectionRepresentation> operationCollection;

    @Mock
    OperationsQueue queue;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        testObj = new OperationsPollingTask(operationCollection, queue);
    }

    @Test
    public void test() throws Exception {
        //GIVEN
        OperationCollectionRepresentation repr = mock(OperationCollectionRepresentation.class);
        List<OperationRepresentation> opList = new LinkedList<OperationRepresentation>();

        when(operationCollection.get()).thenReturn(repr);
        when(repr.getOperations()).thenReturn(opList);

        //WHEN
        testObj.run();

        //THEN
        InOrder inOrder = inOrder(operationCollection, queue);
        inOrder.verify(operationCollection, times(1)).get();
        inOrder.verify(queue, times(1)).addAll(opList);
    }
}
