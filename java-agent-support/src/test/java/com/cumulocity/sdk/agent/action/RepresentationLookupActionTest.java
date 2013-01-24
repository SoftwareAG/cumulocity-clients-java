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

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Answers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.cumulocity.model.idtype.GId;
import com.cumulocity.rest.representation.inventory.ManagedObjectRepresentation;
import com.cumulocity.sdk.agent.model.AbstractAgent;
import com.cumulocity.sdk.client.Platform;
import com.cumulocity.sdk.client.SDKException;

public class RepresentationLookupActionTest {

    private static final GId GID = new GId("id");

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private Platform platform;

    @Mock
    private AbstractAgent agent;

    private RepresentationLookupAction testObj;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        testObj = new RepresentationLookupAction(platform, agent);
    }

    @Test
    public void shouldSetAgentRepresentation() throws Exception {
        // Given
        ManagedObjectRepresentation managedObjectRepresentation = new ManagedObjectRepresentation();
        when(platform.getInventoryApi().getManagedObject(GID).get()).thenReturn(managedObjectRepresentation);
        when(agent.getGlobalId()).thenReturn(GID);
        // When
        testObj.run();
        // Then
        verify(agent).setAgentRepresentation(managedObjectRepresentation);
    }

    @Test
    public void shouldThrowRuntimeExceptionWhenGlobalIdIsNotKnown() throws Exception {
        exception.expect(RuntimeException.class);
        exception.expectMessage("Agents globalId is unknown");

        testObj.run();
    }

    @Test
    public void shouldThrowRuntimeExceptionOnSDKException() throws Exception {
        when(agent.getGlobalId()).thenReturn(GID);
        when(platform.getInventoryApi().getManagedObject(GID)).thenThrow(new SDKException(""));
        exception.expect(RuntimeException.class);
        exception.expectMessage("Cannot fetch agent representation for " + GID);

        testObj.run();
    }

}
