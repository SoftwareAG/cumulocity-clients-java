package com.cumulocity.me.smartrest.client;

public interface SmartConnection {

    public static final String SMARTREST_API_PATH = "/s/";
    
    void setupConnection(String params);
    
    void setupConnection(int mode, boolean timeout);
    
    String bootstrap();
    
    String templateRegistration(String templates);
    
    SmartResponse executeRequest(SmartRequest request);
    
    void closeConnection();

}
