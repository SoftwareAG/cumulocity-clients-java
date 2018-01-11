package com.cumulocity.microservice.context;

import java.util.concurrent.Callable;

/**
 * A service to get and enter context to run tasks with.
 * The service is aimed to be used for the tasks which contains Cumulocity API calls.
 * Context contains credentials to use as authorization to Cumulocity platform. 
 * @param <C> Generic type for the context.
 */
public interface ContextService<C> {

    /**
     * Gets entered context
     * @return entered context
     * @throws IllegalStateException if not within any context
     */
    C getContext();

    /**
     * Returns <code>true</code> if context is found.
     * @return  <code>true</code> if context is found;
     *          <code>false</code> otherwise
     */
    boolean isInContext();

    /**
     * Enters context to run with Runnable task
     * @param context the credentials to access the Cumulocity platform
     * @param task Runnable task
     */
    void runWithinContext(C context, Runnable task);

    /**
     * Enters context to run with Callable task
     * @param context the credentials to access the Cumulocity platform
     * @param task Callable task
     * @return the returned value from the Callable task
     */
    <V> V callWithinContext(C context, Callable<V> task);
    
    /**
     * Enters context to run with Runnable task and returns the task with context
     * @param context the credentials to access the Cumulocity platform
     * @param task Runnable task
     * @return Runnable task with context
     */
    Runnable withinContext(C context, Runnable task);
    
    /**
     * Enters context to run with Runnable task and returns the task with context
     * @param context the credentials to access the Cumulocity platform
     * @param task Callable task
     * @return Callable task with context
     */
    <V> Callable<V> withinContext(C context, Callable<V> task);
}
