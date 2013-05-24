package com.cumulocity.sdk.client.notification;

public interface Subscription<T>{
    
    T getObject();
    
    void unsubscribe();
}