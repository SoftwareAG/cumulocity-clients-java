package com.cumulocity.me.agent.push;

import com.cumulocity.me.smartrest.client.SmartResponseEvaluator;

public interface DevicePushService {

    public String[] getSupportedOperations();
    public void registerOperation(String name, String xId, int messageId, SmartResponseEvaluator callback); 
}
