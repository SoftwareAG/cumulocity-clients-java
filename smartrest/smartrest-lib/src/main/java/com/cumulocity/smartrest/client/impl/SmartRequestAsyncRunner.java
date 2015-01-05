package com.cumulocity.smartrest.client.impl;

import com.cumulocity.smartrest.client.SmartConnection;
import com.cumulocity.smartrest.client.SmartRequest;
import com.cumulocity.smartrest.client.SmartResponse;
import com.cumulocity.smartrest.client.SmartResponseEvaluator;

class SmartRequestAsyncRunner implements Runnable {

    private SmartConnection connection;
    
    private String xid;
    
    private SmartRequest request;
    
    private SmartResponseEvaluator evaluator;
    
    public SmartRequestAsyncRunner(SmartConnection connection, String xid, SmartRequest request, 
            SmartResponseEvaluator evaluator) {
        this.connection = connection;
        this.xid = xid;
        this.request = request;
        this.evaluator = evaluator;
    }
    
    public void run() {
        SmartResponse response = connection.executeRequest(xid, request);
        evaluator.evaluate(response);
    }
}
