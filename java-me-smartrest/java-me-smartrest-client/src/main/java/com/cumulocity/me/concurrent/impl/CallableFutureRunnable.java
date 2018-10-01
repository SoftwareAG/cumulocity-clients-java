package com.cumulocity.me.concurrent.impl;

import com.cumulocity.me.concurrent.exception.ExecutionException;
import com.cumulocity.me.concurrent.model.Callable;

public class CallableFutureRunnable extends FutureRunnable {
    private final Callable callable;

    public CallableFutureRunnable(SmartExecutorServiceImpl executorService, Callable callable) {
        super(executorService);
        this.callable = callable;
    }

    public void execute() {
        try {
            result = callable.call();
        } catch (Throwable throwable) {
            this.failure = new ExecutionException(throwable);
        }
    }
}
