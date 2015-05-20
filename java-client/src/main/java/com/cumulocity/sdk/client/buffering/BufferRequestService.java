package com.cumulocity.sdk.client.buffering;

public interface BufferRequestService {

    Future create(BufferedRequest request);
    
    void addResponse(long requestId, Result result);
}
