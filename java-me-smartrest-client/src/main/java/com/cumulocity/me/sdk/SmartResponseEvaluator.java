package com.cumulocity.me.sdk;

import com.cumulocity.me.smartrest.client.SmartResponse;

public interface SmartResponseEvaluator {

    void evaluate(SmartResponse response);
}
