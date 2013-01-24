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
