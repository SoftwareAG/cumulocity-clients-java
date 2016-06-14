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
import java.util.Vector;

public class CombinedEvaluator implements SmartResponseEvaluator {
	private static final Logger LOG = LoggerFactory.getLogger(CombinedEvaluator.class);
	
    private final Hashtable callbackMap = new Hashtable();

    public void evaluate(SmartResponse response) {
        SmartRow[] rows = response.getDataRows();
        String xId = null;
        Vector currentRows = new Vector();
        for (int i = 0; i < rows.length; i++) {
            SmartRow row = rows[i];
            if (MessageId.SET_XID_RESPONSE.getValue() == row.getMessageId()) {
                xId = row.getData(0);
                callEvaluator(xId, currentRows);
                currentRows = new Vector();
            } else {
                if (!currentRows.isEmpty() &&
                        (row.getMessageId() != ((SmartRow)currentRows.elementAt(0)).getMessageId()
                        || row.getRowNumber() != ((SmartRow)currentRows.elementAt(0)).getRowNumber())){
                    callEvaluator(xId, currentRows);
                    currentRows = new Vector();
                }
                currentRows.addElement(row);
            }
        }
        callEvaluator(xId, currentRows);
    }

    protected void callEvaluator(String xId, Vector rows){
        if (!rows.isEmpty()){
            SmartRow[] rowsArray = new SmartRow[rows.size()];
            rows.copyInto(rowsArray);
            callEvaluator(xId, rowsArray);
        }
    }

    protected void callEvaluator(String xId, SmartRow[] rows) {
    	CallbackMapKey key = new CallbackMapKey(xId, rows[0].getMessageId());
        final SmartResponseEvaluator evaluator = (SmartResponseEvaluator) callbackMap.get(key);
        if (evaluator != null) {
            final SmartResponse response = new SmartResponseImpl(200, "OK", rows);
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
