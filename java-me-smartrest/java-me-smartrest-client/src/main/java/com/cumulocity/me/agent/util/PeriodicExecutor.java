package com.cumulocity.me.agent.util;

public class PeriodicExecutor {

	private int interval;
    private volatile boolean run;
    private PeriodicTask task;
    int failureCount = 0;
    Exception lastException;

    public PeriodicExecutor(int interval, PeriodicTask task) {
        this.interval = interval;
        this.task = task;
    }

    public int getInterval() {
        return interval;
    }

    public void setInterval(int interval) {
        this.interval = interval;
    }

    public void start() {
        run = true;
        new Thread(new Runnable() {

            public void run() {
                while (run) {
                    long startTime = System.currentTimeMillis();
                    try { 
                       task.execute();
                    } catch (PeriodicExecutionException e) {
                        failureCount++;
                        lastException = e;
                    }
                    if (run) {
                        long endTime = System.currentTimeMillis();
                        long duration = endTime - startTime;
                        if (duration < interval) {
                            try {
                                Thread.sleep(interval - duration);
                            } catch (InterruptedException ex) {
                                ex.printStackTrace();
                            }
                        }
                    }
                }
            }
        }).start();
    }

    public void stop() {
        run = false;
    }
}
