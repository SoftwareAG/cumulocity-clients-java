package com.cumulocity.me.http;

import com.cumulocity.me.lang.Map;

public interface WebRequest {

    WebMethod getMethod();
    
    String getPath();
    
    Map getHeaders();

    byte[] getData();
}