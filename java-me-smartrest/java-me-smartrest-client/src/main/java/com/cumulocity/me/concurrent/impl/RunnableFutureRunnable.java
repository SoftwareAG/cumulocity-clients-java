package com.cumulocity.me.concurrent.impl;

import com.cumulocity.me.concurrent.exception.ExecutionException;

public class RunnableFutureRunnable extends FutureRunnable {
    private final Runnable runnable;

    public RunnableFutureRunnable(SmartExecutorServiceImpl executorService, Runnable runnable) {
        super(executorService);
        this.runnable = runnable;
    }

    public void execute() {
        try {
            runnable.run();
        } catch (Throwable throwable) {
            this.failure = new ExecutionException(throwable);
        }
        done = true;
    }
}
