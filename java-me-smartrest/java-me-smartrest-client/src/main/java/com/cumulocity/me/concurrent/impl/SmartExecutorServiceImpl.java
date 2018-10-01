package com.cumulocity.me.concurrent.impl;

import java.util.Vector;

import com.cumulocity.me.concurrent.model.Callable;
import com.cumulocity.me.concurrent.model.Future;
import com.cumulocity.me.concurrent.SmartExecutorService;

public class SmartExecutorServiceImpl implements SmartExecutorService {
    
    public static final int MAX_THREADS_DEFAULT = 5;
    public static final int SCHEDULE_INTERVAL_DEFAULT = 500;

    private final int maxThreads;
    private volatile int currentThreads;
    private final Vector queue = new Vector();

    public SmartExecutorServiceImpl() {
        this(MAX_THREADS_DEFAULT, SCHEDULE_INTERVAL_DEFAULT);
    }

    public SmartExecutorServiceImpl(int maxThreads, int scheduleInterval) {
        this.maxThreads = maxThreads;
        startPeriodicScheduler(scheduleInterval);
    }

    private void startPeriodicScheduler(final int interval) {
        new Thread(new Runnable() {
            private volatile boolean run = true;
            public void run() {
                while (run) {
                    try {
                        Thread.sleep(interval);
                    } catch (InterruptedException e) {
                        run = false;
                    }
                    tryScheduleNext();
                }
            }
        }).start();
    }

    public Future execute(Callable callable) {
        FutureRunnable future = new CallableFutureRunnable(this, callable);
        queue(future);
        return future;
    }

    public Future execute(Runnable runnable) {
        FutureRunnable future = new RunnableFutureRunnable(this, runnable);
        queue(future);
        return future;
    }

    private void queue(FutureRunnable future) {
        queue.addElement(future);
        tryScheduleNext();
    }

    private synchronized void tryScheduleNext() {
        if (currentThreads < maxThreads && queue.size() > 0 && queue.elementAt(0) != null) {
            FutureRunnable future = (FutureRunnable) queue.elementAt(0);
            queue.removeElementAt(0);
            schedule(future);
        }
    }

    private synchronized void schedule(FutureRunnable future) {
        currentThreads++;
        new Thread(future).start();
    }

    synchronized void decrementCurrentThreads() {
        currentThreads--;
    }

    public synchronized int getCurrentThreads() {
        return currentThreads;
    }
}
