package com.cumulocity.me.smartrest.client.impl;

import com.cumulocity.me.sdk.SmartResponseEvaluator;
import com.cumulocity.me.smartrest.client.SmartRequest;
import com.cumulocity.me.smartrest.client.SmartResponse;

public class SmartRequestAsyncRunner implements Runnable {

    private SmartRequestConnector connector;
    
    private SmartRequest request;
    
    private SmartResponseEvaluator evaluator;
    
    public SmartRequestAsyncRunner(SmartRequestConnector connector, SmartRequest request, 
            SmartResponseEvaluator evaluator) {
        this.connector = connector;
        this.request = request;
        this.evaluator = evaluator;
    }
    
    public void run() {
        SmartResponse response = connector.executeRequest(request);
        connector.close();
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
