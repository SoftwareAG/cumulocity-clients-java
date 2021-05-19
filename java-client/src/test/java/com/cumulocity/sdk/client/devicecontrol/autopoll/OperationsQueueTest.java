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

import static org.assertj.core.api.Assertions.assertThat;

import java.util.LinkedList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.cumulocity.model.idtype.GId;
import com.cumulocity.rest.representation.operation.OperationRepresentation;

public class OperationsQueueTest {

    OperationsQueue testObj;

    @BeforeEach
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

        boolean added = testObj.add(op3);

        assertThat(added).isTrue();
        assertThat(testObj).hasSize(3);
    }

    @Test
    public void testAddNonUnique() {
        OperationRepresentation opNonUnique = new OperationRepresentation();
        opNonUnique.setId(new GId("op2"));

        boolean added = testObj.add(opNonUnique);

        assertThat(added).isFalse();
        assertThat(testObj).hasSize(2);
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

        boolean added = testObj.addAll(list);

        assertThat(added).isFalse();
        assertThat(testObj).hasSize(2);
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

        boolean added = testObj.addAll(list);

        assertThat(added).isTrue();
        assertThat(testObj).hasSize(4);
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

        boolean added = testObj.addAll(list);

        assertThat(added).isTrue(); //queue was changed, even if not all elements were added
        assertThat(testObj).hasSize(3);
    }

}
