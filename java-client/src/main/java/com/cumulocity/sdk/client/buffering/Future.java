package com.cumulocity.sdk.client.buffering;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import com.cumulocity.sdk.client.SDKException;

public class Future {
    
    private static int MAX_WAIT_FOR_RESPONSE = 120;
    
    private final CountDownLatch latch = new CountDownLatch(1);

    private volatile Result result;
    
    /**
     * Waits for the response to complete
     * @return
     * @throws SDKException
     */
    public Object get() throws SDKException {
        try {
            latch.await(MAX_WAIT_FOR_RESPONSE, TimeUnit.SECONDS);
            if (result == null) {
                return result;
            }
            if (result.getException() != null) {
                throw result.getException();
            }
            return result.getResponse();
        } catch (InterruptedException e) {
            throw new RuntimeException("", e);
        }
    }
    
    public void setResponse(Result result) {
        this.result = result;
        latch.countDown();
    }
}
