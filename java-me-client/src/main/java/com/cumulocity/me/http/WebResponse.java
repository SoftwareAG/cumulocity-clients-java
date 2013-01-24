package com.cumulocity.me.http;

import com.cumulocity.me.lang.Map;

public interface WebResponse {
    
    boolean isSuccessful();

    int getStatus();
    
    String getMessage();
    
    Map getHeaders();
    
    byte[] getData();
}