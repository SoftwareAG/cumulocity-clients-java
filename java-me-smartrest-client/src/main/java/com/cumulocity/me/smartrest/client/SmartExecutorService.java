package com.cumulocity.me.smartrest.client;

public interface SmartExecutorService {

    void terminateAll();
    
    void terminateAllIdle();
    
    Thread execute(Runnable task);
    
}
