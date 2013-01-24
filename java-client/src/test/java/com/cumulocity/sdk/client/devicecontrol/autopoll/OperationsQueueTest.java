package com.cumulocity.sdk.client.devicecontrol.autopoll;

import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.hamcrest.Matchers.equalTo;

import java.util.LinkedList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.cumulocity.model.idtype.GId;
import com.cumulocity.rest.representation.operation.OperationRepresentation;
import com.cumulocity.sdk.client.devicecontrol.autopoll.OperationsQueue;

public class OperationsQueueTest {
    OperationsQueue testObj;
    
    @Before
    public void setUp() {
        testObj = new OperationsQueue();
        OperationRepresentation op1 = new OperationRepresentation();
        op1.setId(new GId("op1"));
        OperationRepresentation op2 = new OperationRepresentation();
        op2.setId(new GId("op2"));
        testObj.add(op1);
        testObj.add(op2);
    }
    
    @Test
    public void testAddUnique() {
        OperationRepresentation op3 = new OperationRepresentation();
        op3.setId(new GId("op3"));
        assertTrue(testObj.add(op3));
        assertThat(testObj.size(), equalTo(3));
    }

    @Test
    public void testAddNonUnique() {
        OperationRepresentation opNonUnique = new OperationRepresentation();
        opNonUnique.setId(new GId("op2"));
        assertFalse(testObj.add(opNonUnique));
        assertThat(testObj.size(), equalTo(2));
    }
    
    @Test
    public void testAddNonUniqueList() {
        OperationRepresentation opNonUnique1 = new OperationRepresentation();
        opNonUnique1.setId(new GId("op1"));
        OperationRepresentation opNonUnique2 = new OperationRepresentation();
        opNonUnique2.setId(new GId("op2"));
        List<OperationRepresentation> list = new LinkedList<OperationRepresentation>();
        list.add(opNonUnique1);
        list.add(opNonUnique2);
        
        assertFalse(testObj.addAll(list));
        assertThat(testObj.size(), equalTo(2));
    }
    
    @Test
    public void testAddUniqueList() {
        OperationRepresentation opUnique1 = new OperationRepresentation();
        opUnique1.setId(new GId("op3"));
        OperationRepresentation opUnique2 = new OperationRepresentation();
        opUnique2.setId(new GId("op4"));
        List<OperationRepresentation> list = new LinkedList<OperationRepresentation>();
        list.add(opUnique1);
        list.add(opUnique2);
        
        assertTrue(testObj.addAll(list));
        assertThat(testObj.size(), equalTo(4));
    }
    
    @Test
    public void testAddMixedList() {
        OperationRepresentation opUnique1 = new OperationRepresentation();
        opUnique1.setId(new GId("op3"));
        OperationRepresentation opNonUnique2 = new OperationRepresentation();
        opNonUnique2.setId(new GId("op1"));
        List<OperationRepresentation> list = new LinkedList<OperationRepresentation>();
        list.add(opUnique1);
        list.add(opNonUnique2);
        
        assertTrue(testObj.addAll(list)); //queue was changed, even if not all elements were added
        assertThat(testObj.size(), equalTo(3));
    }

}
