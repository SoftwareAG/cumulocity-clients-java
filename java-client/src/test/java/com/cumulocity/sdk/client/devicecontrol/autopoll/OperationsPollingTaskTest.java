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
