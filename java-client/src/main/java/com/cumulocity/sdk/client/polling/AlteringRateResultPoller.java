package com.cumulocity.sdk.client.polling;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cumulocity.sdk.client.SDKException;

public class AlteringRateResultPoller<K> implements ResultPoller<K> {
    
    private static final Logger LOG = LoggerFactory.getLogger(AlteringRateResultPoller.class);
    
    public static interface GetResultTask<K> {
        K tryGetResult();        
    }

    private final PollingStrategy pollingStrategy;
    private final ScheduledThreadPoolExecutor pollingExecutor;
    private final Runnable pollingTask;
    private CountDownLatch latch;
    private volatile K result;
    private Exception lastException;

    public AlteringRateResultPoller(GetResultTask<K> task, PollingStrategy strategy) {
        this.pollingStrategy = strategy;
        this.pollingExecutor = new ScheduledThreadPoolExecutor(1);
        this.pollingTask = wrapAsRunnable(task);
    }
    
    @Override
    public boolean start() {
        if (pollingTask == null) {
            LOG.error("Poller start requested without pollingTask being set");
            return false;
        }

        scheduleNextTaskExecution();
        return true;
    }

    private void scheduleNextTaskExecution() {  
        if(!pollingStrategy.isEmpty()) {
            pollingExecutor.schedule(pollingTask, pollingStrategy.popNext(), TimeUnit.MILLISECONDS);
        }
        
    }

    @Override
    public void stop() {
        //shutdown operationsPollingExecutor if it's running or if it's no shutting down just now
        pollingExecutor.shutdown();
    }
            
    @Override    
    public K startAndPoll() {
        latch = new CountDownLatch(1);
        start();
        try {
            waitForResult();
            if(result == null && lastException != null) { 
                LOG.error("Timeout occured, last exception: " + lastException);
            }
            return result;
        } catch (InterruptedException e) {
            throw new SDKException("Error polling data", e);
        } finally {
            stop();
        }
    }

    private void waitForResult() throws InterruptedException {
        if(pollingStrategy.getTimeout() == null) {
            latch.await();                
        } else {
            latch.await(pollingStrategy.getTimeout(), TimeUnit.MILLISECONDS);                
        }
    }

    private Runnable wrapAsRunnable(final GetResultTask<K> task) {
        return new Runnable() {
            
            @Override
            public void run() {
                appendResult(task);
                
            }
        };
    }
    
    private void appendResult(final GetResultTask<K> task) {
        if(result != null) {
            return;
        }
        try {
            result = task.tryGetResult();
            if(result == null) {
                scheduleNextTaskExecution();
            } else {
                latch.countDown();                        
            }
        } catch (Exception ex) {
            lastException = ex;
            LOG.info("Polling with wrong result: " + digest(ex.getMessage()));
            if(!pollingStrategy.isEmpty()) {
                LOG.info("Try again in " + pollingStrategy.peakNext() / 1000 + " seconds.");
            }
            scheduleNextTaskExecution();
        }
    }

    private static String digest(String message) {
        if (message == null || message.length() < 200) {
            return message;
        } else {
            return message.substring(0, Math.min(message.length() - 1, 200));
        } 
    }
}
