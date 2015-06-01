package com.cumulocity.sdk.client.buffering;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class BufferRequestServiceImpl implements BufferRequestService {

    private final PersistentProvider persistentProvider;

    private final ConcurrentMap<Long, Future> futures = new ConcurrentHashMap<Long, Future>();

    public BufferRequestServiceImpl(PersistentProvider persistentProvider) {
        this.persistentProvider = persistentProvider;
    }

    public Future create(BufferedRequest request) {
        long requestId = persistentProvider.generateId();
        Future future = initAsyncResponse(requestId);
        persistentProvider.offer(new ProcessingRequest(requestId, request));
        return future;
    }

    private Future initAsyncResponse(long requestId) {
        Future future = new Future();
        futures.put(requestId, future);
        return future;
    }

    public void addResponse(long requestId, Result result) {
        Future future = futures.get(requestId);
        if (future != null) {
            future.setResponse(result);
            futures.remove(requestId);
        }
    }
}
