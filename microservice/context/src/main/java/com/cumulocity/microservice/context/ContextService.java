package com.cumulocity.microservice.context;

import java.util.concurrent.Callable;

public interface ContextService<C> {

    C getContext();

    void runWithinContext(C context, Runnable task);

    <V> V callWithinContext(C context, Callable<V> task);
    
    Runnable withinContext(C context, Runnable task);
    
    <V> Callable<V> withinContext(C context, Callable<V> task);
}
