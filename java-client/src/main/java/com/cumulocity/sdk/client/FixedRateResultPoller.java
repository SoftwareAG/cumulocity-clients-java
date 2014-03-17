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

    public FixedRateResultPoller(GetResultTask<K> task, long pollInterval, long pollTimeout) {
        super(new ScheduledThreadPoolExecutor(1), pollInterval);
        this.pollTimeout = pollTimeout;
        this.results = aQueue();
        setPollingTask(wrapAsRunnable(task));
    }
    
    public K startAndPoll() {
        start();
        try {
            return results.poll(pollTimeout, TimeUnit.MICROSECONDS);
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
            LOG.error("STH", ex);
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
