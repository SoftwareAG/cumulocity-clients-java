package com.cumulocity.me.http.impl;

import com.cumulocity.me.http.WebMethod;
import com.cumulocity.me.http.WebRequest;
import com.cumulocity.me.lang.Map;

public class WebRequestImpl implements WebRequest {

    private final WebMethod method;
    private final String path;
    private final Map headers;
    private final byte[] data;
    
    public WebRequestImpl(WebMethod method, String path, Map headers, byte[] data) {
        this.method = method;
        this.path = path;
        this.headers = headers;
        this.data = data;
    }

    public WebMethod getMethod() {
        return method;
    }

    public String getPath() {
        return path;
    }

    public Map getHeaders() {
        return headers;
    }
    
    public byte[] getData() {
        return data;
    }
}