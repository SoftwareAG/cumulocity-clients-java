package com.cumulocity.me.agent.smartrest.impl;

import com.cumulocity.me.agent.smartrest.model.BulkRequest;
import com.cumulocity.me.agent.smartrest.model.RequestBufferItem;
import com.cumulocity.me.agent.smartrest.model.RequestBufferItemList;
import com.cumulocity.me.smartrest.client.SmartRequest;
import com.cumulocity.me.smartrest.client.SmartResponseEvaluator;

public class RequestBuffer {
    private volatile RequestBufferItemList buffer = new RequestBufferItemList();
    private int extractedUntil;
    
    public void add(SmartRequest request, String xId, SmartResponseEvaluator callback){ 
    	buffer.addElement(new RequestBufferItem(xId, request, callback));
    }

    public BulkRequest extractBulkRequest(){
        if (buffer.isEmpty()) {
            return null;
        }
        extractedUntil = buffer.size();
        RequestBufferItem[] toExtract = new RequestBufferItem[extractedUntil];
        for (int i = 0; i < toExtract.length; i++) {
            toExtract[i] = buffer.elementAt(i);
        }
        return new BulkRequest(toExtract);
    }
    
    public void acknowledgeExtracted(){
        for (int i = 0; i < extractedUntil; i++) {
            buffer.removeElementAt(0);
        }
    }
}
