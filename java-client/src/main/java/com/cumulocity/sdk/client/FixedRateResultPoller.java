package com.cumulocity.sdk.client;

import java.util.Comparator;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FixedRateResultPoller<K> extends FixedRatePoller {
    
    private static final Logger LOG = LoggerFactory.getLogger(FixedRateResultPoller.class);
    
    public static interface GetResultTask<K> {
        K tryGetResult();        
    }

    private PriorityBlockingQueue<K> results;
    private long pollTimeout;
    private long pollInterval;
    private Exception lastException;

    public FixedRateResultPoller(GetResultTask<K> task, long pollInterval, long pollTimeout) {
        super(new ScheduledThreadPoolExecutor(1), pollInterval);
        this.pollInterval = pollInterval;
        this.pollTimeout = pollTimeout;
        this.results = aQueue();
        setPollingTask(wrapAsRunnable(task));
    }
    
    public K startAndPoll() {
        start();
        try {
            K result = results.poll(pollTimeout, TimeUnit.MILLISECONDS);
            if(result == null && lastException != null) { 
                LOG.error("Timeout occured, last exception: " + lastException);
            }
            return result;
        } catch (InterruptedException e) {
            throw new SDKException("Error polling data", e);
        } finally {
            results.clear();
            stop();
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
        if(!results.isEmpty()) {
            return;
        }
        try {
            K result = task.tryGetResult();
            if(result != null) {
                results.add(result);                        
            }
        } catch (Exception ex) {
            lastException = ex;
            LOG.info("Polling with wrong result: " + digest(ex.getMessage()));
            LOG.info("Try again in " + pollInterval / 1000 + " seconds.");
        }
    }

    private static String digest(String message) {
        if (message == null || message.length() < 200) {
            return message;
        } else {
            return message.substring(0, Math.min(message.length() - 1, 200));
        } 
    }

    private PriorityBlockingQueue<K> aQueue() {
        return new PriorityBlockingQueue<K>(1, new Comparator<K>() {
            @Override
            public int compare(K o1, K o2) {
                return 0;
            }
        });
    }
}
