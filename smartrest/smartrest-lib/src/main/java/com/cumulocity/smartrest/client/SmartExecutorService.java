package com.cumulocity.smartrest.client;

public interface SmartExecutorService {

    void terminateAll();
    
    void terminateAllIdle();
    
    Thread execute(Runnable task);
    
}
