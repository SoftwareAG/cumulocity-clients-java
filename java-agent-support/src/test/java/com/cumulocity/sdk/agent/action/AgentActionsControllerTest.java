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

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;
import org.springframework.scheduling.TaskScheduler;

import com.cumulocity.sdk.agent.action.AgentAction;
import com.cumulocity.sdk.agent.action.AgentActionsChain;
import com.cumulocity.sdk.agent.action.AgentActionsController;

public class AgentActionsControllerTest {
	
	private TaskScheduler scheduler;
	private AgentActionsController testObj;
	
	@Before
	public void setUp() {
		scheduler = mock(TaskScheduler.class);
		testObj = new AgentActionsController(scheduler);
	}

    @Test
    public void shouldRunAllControllers() throws Exception {
        // Given
        AgentAction action1 = mock(AgentAction.class);
        AgentAction action2 = mock(AgentAction.class);
        AgentActionsChain chain = new AgentActionsChain();
        chain.setActions(Arrays.asList(action1, action2));
        testObj.setStartupChain(chain);
        // When
        testObj.start();
        // Then
        verify(action1).run();
        verify(action2).run();
    }
}
