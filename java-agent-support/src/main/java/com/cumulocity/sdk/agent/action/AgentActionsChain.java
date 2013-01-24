/*
 * Copyright 2012 Nokia Siemens Networks 
 */
package com.cumulocity.sdk.agent.action;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Represents a chain of {@link AgentAction} that are executed sequentially as a batch.
 */
public class AgentActionsChain implements Runnable {

	private static final Logger LOG = LoggerFactory.getLogger(AgentActionsChain.class);
	
	private volatile int numOfExecutions = 0;
	
	private List<AgentAction> actions;
	
	/**
	 * Sets the actions to be executed.
	 * @param actions the actions.
	 */
	public void setActions(List<AgentAction> actions) {
		this.actions = actions;
	}
	
	/**
	 * Gets the number of times the chain was fully executed.
	 * @return the number of full executions.
	 */
	public int getNumOfExecutions() {
		return numOfExecutions;
	}
	
	@Override
	public void run() {
		LOG.info(String.format("Starting execution #%d of chain of %d actions...", numOfExecutions + 1, actions.size()));
		try {
			for (AgentAction action : actions) {
				action.run();
				LOG.info(String.format("Action %s run successfully.", action.getClass().getName()));
			}
		} finally {
			numOfExecutions++;
		}
		LOG.info(String.format("Execution #%d of chain of %d actions ended.", numOfExecutions, actions.size()));
	}
}
