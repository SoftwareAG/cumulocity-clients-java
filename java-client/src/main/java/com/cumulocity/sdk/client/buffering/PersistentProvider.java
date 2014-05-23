package com.cumulocity.sdk.client.buffering;


public interface PersistentProvider {
    
    long offer(HTTPPostRequest request);
    
    ProcessingRequest poll();

}
