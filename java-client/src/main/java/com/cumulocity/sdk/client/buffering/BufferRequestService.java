package com.cumulocity.sdk.client.buffering;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import com.cumulocity.sdk.client.SDKException;

public class BufferRequestService {
    
    private static int MAX_WAIT_FOR_RESPONSE = 120;
    private final PersistentProvider persistentProvider;
    private final ConcurrentMap<Long, BlockingQueue<Result>> responses = new ConcurrentHashMap<Long, BlockingQueue<Result>>();

    public BufferRequestService(PersistentProvider persistentProvider) {
        this.persistentProvider = persistentProvider;
    }
    
    public long create(BufferedRequest request) {
        long requestId = persistentProvider.offer(request);
        responses.put(requestId, new LinkedBlockingQueue<Result>());
        return requestId;
    }
    
    public void addResponse(long requestId, Result result) {
        try {
            responses.get(requestId).put(result);
        } catch (InterruptedException e) {
            throw new RuntimeException("", e);
        }
    }
    
    public Object getResponse(long requestId) throws SDKException {
        try {
            Result result = responses.get(requestId).poll(MAX_WAIT_FOR_RESPONSE, TimeUnit.SECONDS);
            responses.remove(requestId);
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
}
