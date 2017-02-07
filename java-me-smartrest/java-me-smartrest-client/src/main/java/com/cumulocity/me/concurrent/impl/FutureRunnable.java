package com.cumulocity.me.concurrent.impl;

import com.cumulocity.me.concurrent.exception.ExecutionException;
import com.cumulocity.me.concurrent.exception.TimeoutException;
import com.cumulocity.me.concurrent.model.Future;

public abstract class FutureRunnable implements Future, Runnable {

    private final SmartExecutorServiceImpl executorService;

    protected boolean done;
    protected Object result;
    protected ExecutionException failure;

    protected FutureRunnable(SmartExecutorServiceImpl executorService) {
        this.executorService = executorService;
    }

    public boolean isDone() {
        return done;
    }

    public Object get() throws ExecutionException, InterruptedException {
        while (!isDone()) {
            Thread.sleep(500);
        }
        checkFailure();
        return result;
    }

    public Object get(int timeout) throws ExecutionException, TimeoutException, InterruptedException {
        long start = System.currentTimeMillis();
        while (!isDone()) {
            checkTimeout(start, timeout);
            Thread.sleep(500);
        }
        checkFailure();
        return result;
    }

    public final void run() {
        execute();
        executorService.decrementCurrentThreads();
    }

    private void checkTimeout(long start, int length) throws TimeoutException {
        long now = System.currentTimeMillis();
        if (now - start > length) {
            throw new TimeoutException();
        }
    }

    private void checkFailure() throws ExecutionException {
        if (failure != null) {
            throw failure;
        }
    }

    protected abstract void execute();
}
