package com.cumulocity.smartrest.client;

public interface SmartConnection {

    public static final String SMARTREST_API_PATH = "/s/";
    public static final int BOOTSTRAP_REQUEST_CODE = 61;
    public static final int BOOTSTRAP_RESPONSE_CODE = 70;
    
    void setupConnection(String params);
    
    void setupConnection(int mode, boolean timeout);
    
    String bootstrap(String id);
    
    String registerTemplate(String xid, String templates);
    
    SmartResponse executeRequest(String xid, SmartRequest request);
    
    SmartResponse executeLongPollingRequest(String xid, SmartRequest request);
    
    void executeRequestAsync(String xid, SmartRequest request, SmartResponseEvaluator evaluator);
    
    void closeConnection();

}
