package com.cumulocity.sdk.client.buffering;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicLong;


public class MemoryBasedPersistentProvider implements PersistentProvider {

    private final AtomicLong counter = new AtomicLong(1);
    private final BlockingQueue<ProcessingRequest> requestQueue = new LinkedBlockingQueue<ProcessingRequest>();
    
    @Override
    public long offer(HTTPPostRequest request) {
        try {
            long requestId = counter.getAndIncrement();
            requestQueue.put(new ProcessingRequest(requestId, request));
            return requestId;
        } catch (InterruptedException e) {
            throw new RuntimeException("", e);
        }
    }

    @Override
    public ProcessingRequest poll() {
        try {
            return requestQueue.take();
        } catch (InterruptedException e) {
            throw new RuntimeException("", e);
        }
    }
}
