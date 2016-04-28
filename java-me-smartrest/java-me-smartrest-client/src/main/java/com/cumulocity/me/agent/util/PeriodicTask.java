package com.cumulocity.me.agent.util;

public interface PeriodicTask {
    public void execute() throws PeriodicExecutionException;
}
