package com.cumulocity.me.smartrest.client;

public interface SmartConnection {

    public static final String SMARTREST_API_PATH = "/s/";
    public static final String DEVICEBOOTSTRAP_AUTHENTICATION = "Basic bWFuYWdlbWVudC9kZXZpY2Vib290c3RyYXA6RmhkdDFiYjFm";
    public static final int BOOTSTRAP_REQUEST_CODE = 61;
    public static final int BOOTSTRAP_RESPONSE_CODE = 70;
    
    void setupConnection(String params);
    
    void setupConnection(int mode, boolean timeout);
    
    String bootstrap(String id);
    
    String templateRegistration(String templates);
    
    SmartResponse executeRequest(SmartRequest request);
    
    void closeConnection();

}
