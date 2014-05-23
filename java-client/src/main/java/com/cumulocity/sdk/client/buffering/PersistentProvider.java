package com.cumulocity.sdk.client.buffering;


public abstract class PersistentProvider {
    
    private static final long DEFAULT_BUFFER_LIMIT = 10000;
    
    protected long bufferLimit = DEFAULT_BUFFER_LIMIT;
    
    public PersistentProvider() {
    }

    public PersistentProvider(long bufferLimit) {
        this.bufferLimit = bufferLimit;
    }
    
    public abstract long offer(HTTPPostRequest request);
    
    public abstract ProcessingRequest poll();

}
