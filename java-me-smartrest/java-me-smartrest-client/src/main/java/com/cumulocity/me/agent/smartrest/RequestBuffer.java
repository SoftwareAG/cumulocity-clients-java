package com.cumulocity.me.agent.smartrest;

import com.cumulocity.me.smartrest.client.SmartRequest;
import com.cumulocity.me.smartrest.client.SmartResponseEvaluator;

public class RequestBuffer {
    private RequestBufferItemList buffer = new RequestBufferItemList();
    private int extractedUntil;
    
    public synchronized void add(SmartRequest request, String xId, SmartResponseEvaluator callback){ 
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
