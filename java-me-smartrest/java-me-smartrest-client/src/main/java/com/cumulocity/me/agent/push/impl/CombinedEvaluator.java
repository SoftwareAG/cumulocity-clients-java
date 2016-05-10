package com.cumulocity.me.agent.push.impl;

import com.cumulocity.me.agent.push.model.CallbackMapKey;
import com.cumulocity.me.agent.smartrest.model.MessageId;
import com.cumulocity.me.smartrest.client.SmartResponse;
import com.cumulocity.me.smartrest.client.SmartResponseEvaluator;
import com.cumulocity.me.smartrest.client.impl.SmartResponseImpl;
import com.cumulocity.me.smartrest.client.impl.SmartRow;

import net.sf.microlog.core.Logger;
import net.sf.microlog.core.LoggerFactory;

import java.util.Hashtable;

public class CombinedEvaluator implements SmartResponseEvaluator {
	private static final Logger LOG = LoggerFactory.getLogger(CombinedEvaluator.class);
	
    private final Hashtable callbackMap = new Hashtable();

    public void evaluate(SmartResponse response) {
        SmartRow[] rows = response.getDataRows();
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

    protected void callEvaluator(String xId, SmartRow row) {
    	CallbackMapKey key = new CallbackMapKey(xId, row.getMessageId());
        final SmartResponseEvaluator evaluator = (SmartResponseEvaluator) callbackMap.get(key);
        if (evaluator != null) {
            final SmartResponse response = new SmartResponseImpl(200, "OK", new SmartRow[]{row});
            new Thread(new Runnable() {
				public void run() {
					evaluator.evaluate(response);
				}
			}).start();
        }
    }

    public void registerOperation(String xId, int messageId, SmartResponseEvaluator callback) {
        callbackMap.put(new CallbackMapKey(xId, messageId), callback);
    }
}
