package com.cumulocity.sdk.client.buffering;


public abstract class PersistentProvider {
    
    protected static final long DEFAULT_BUFFER_LIMIT = 10000;
    
    protected long bufferLimit = DEFAULT_BUFFER_LIMIT;
    
    public PersistentProvider() {
    }

    public PersistentProvider(long bufferLimit) {
        this.bufferLimit = bufferLimit;
    }
    
    public abstract long generateId();
    
    public abstract void offer(ProcessingRequest request);
    
    public abstract ProcessingRequest poll();

}
