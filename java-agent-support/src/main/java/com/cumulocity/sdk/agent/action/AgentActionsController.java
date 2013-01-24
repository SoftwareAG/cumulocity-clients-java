/*
 * Copyright 2012 Nokia Siemens Networks 
 */
package com.cumulocity.sdk.agent.action;

import java.util.concurrent.ScheduledFuture;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.TaskScheduler;

/**
 * Controls the agent execution.
 */
public class AgentActionsController {

    private static final Logger LOG = LoggerFactory.getLogger(AgentActionsController.class);

    private TaskScheduler scheduler;
    
    private AgentActionsChain startupChain;
    
    private AgentActionsChain cyclicChain;
    
    private int cyclicChainDelay;
    
    private ScheduledFuture<?> scheduledCyclicChain;

    /**
     * @param scheduler the scheduler to use to schedule the cyclic chain. 
     */
    @Autowired
    public AgentActionsController(TaskScheduler scheduler) {
		this.scheduler = scheduler;
	}

	/**
     * Sets the chain of actions to run on agent startup.
     * @param startupChain the chain of startup actions.
     */
    public void setStartupChain(AgentActionsChain startupChain) {
		this.startupChain = startupChain;
	}
    
    /**
     * Sets the chain of actions to run on periodically on fixed delay.
     * @param cyclicChain the chain of periodical actions.
     */
    public void setCyclicChain(AgentActionsChain cyclicChain) {
		this.cyclicChain = cyclicChain;
	}
    
    /**
     * Sets the delay between each execution of periodical actions chain.
     * @param cyclicChainDelay the delay between executions.
     */
    public void setCyclicChainDelay(int cyclicChainDelay) {
		this.cyclicChainDelay = cyclicChainDelay;
	}
    
    /**
     * Returns the number of times the startup actions chain was executed. Delegates to the chain itself to get the result.
     * @return <tt>0</tt> until the startup actions chain was completed, <tt>1</tt> afterwards.
     */
    public int startupChainNumOfExecutions() {
		return startupChain.getNumOfExecutions();
	}
    
    /**
     * Returns the number of times the cyclic actions chain was executed. Delegates to the chain itself to get the result.
     * @return the number of executions of the cyclic actions chain.
     */
    public int cyclicChainNumOfExecutions() {
		return cyclicChain.getNumOfExecutions();
	}

    /**
     * Starts the agent.
     */
    @PostConstruct
    public void start() {
    	LOG.info("Starting agent...");
    	
    	startupChain.run();
    	
    	scheduledCyclicChain = scheduler.scheduleWithFixedDelay(cyclicChain, cyclicChainDelay);
    	
    	LOG.info("Started agent.");
    }
    
    /**
     * Stops the agent.
     */
    @PreDestroy
    public void stop() {
    	scheduledCyclicChain.cancel(true);
    }
}
