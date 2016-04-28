package com.cumulocity.me.agent.push;

import com.cumulocity.me.agent.smartrest.MessageId;
import com.cumulocity.me.smartrest.client.SmartResponse;
import com.cumulocity.me.smartrest.client.SmartResponseEvaluator;
import com.cumulocity.me.smartrest.client.impl.SmartResponseImpl;
import com.cumulocity.me.smartrest.client.impl.SmartRow;
import java.util.Hashtable;

public class CombinedEvaluator implements SmartResponseEvaluator {

    private final Hashtable callbackMap = new Hashtable();

    public void evaluate(SmartResponse response) {
        System.out.println("Evaluating response: " + response.getStatus() + ", " + response.getMessage());
        SmartRow[] rows = response.getDataRows();
        System.out.println("data rows: length=" + rows.length);
        String xId = "";
        for (int i = 0; i < rows.length; i++) {
            SmartRow row = rows[i];
            System.out.println(row.toString());
            if (MessageId.SET_XID_RESPONSE.getValue() == row.getMessageId()) {
                xId = row.getData(0);
            } else {
                callEvaluator(xId, row);
            }
        }
    }

    private void callEvaluator(String xId, SmartRow row) {
        CallbackMapKey key = new CallbackMapKey(xId, row.getMessageId());
        SmartResponseEvaluator evaluator = (SmartResponseEvaluator) callbackMap.get(key);
        if (evaluator != null) {
            SmartResponse response = new SmartResponseImpl(200, "OK", new SmartRow[]{row});
            evaluator.evaluate(response);
        }
    }

    public void registerOperation(String xId, int messageId, SmartResponseEvaluator callback) {
        callbackMap.put(new CallbackMapKey(xId, messageId), callback);
    }
}
