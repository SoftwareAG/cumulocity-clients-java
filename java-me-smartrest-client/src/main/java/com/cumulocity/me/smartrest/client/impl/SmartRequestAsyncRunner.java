package com.cumulocity.me.smartrest.client.impl;

import com.cumulocity.me.sdk.SmartResponseEvaluator;
import com.cumulocity.me.smartrest.client.SmartConnection;
import com.cumulocity.me.smartrest.client.SmartRequest;
import com.cumulocity.me.smartrest.client.SmartResponse;

public class SmartRequestAsyncRunner implements Runnable {

    private SmartConnection connection;
    
    private SmartRequest request;
    
    private SmartResponseEvaluator evaluator;
    
    public SmartRequestAsyncRunner(SmartConnection connection, SmartRequest request, 
            SmartResponseEvaluator evaluator) {
        this.connection = connection;
        this.request = request;
        this.evaluator = evaluator;
    }
    
    public void run() {
        SmartResponse response = connection.executeRequest(request);
        connection.closeConnection();
        evaluator.evaluate(response);
    }

    public void start() {
        Thread thread = new Thread(this);
        try {
          thread.start();
        } catch (Exception error) {
        }
    }


}
