package com.cumulocity.me.agent.smartrest.impl;

import com.cumulocity.me.agent.util.PeriodicExecutionException;
import com.cumulocity.me.agent.util.PeriodicTask;

public class SmartrestManagerTask implements PeriodicTask{
    
    private final SmartrestManager smartrestManager;

    public SmartrestManagerTask(SmartrestManager smartrestManager) {
        this.smartrestManager = smartrestManager;
    }

    public void execute() throws PeriodicExecutionException {
        smartrestManager.send();
    } 
}
