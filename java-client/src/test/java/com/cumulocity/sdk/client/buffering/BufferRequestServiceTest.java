package com.cumulocity.sdk.client.buffering;

import static org.fest.assertions.Assertions.assertThat;

import org.junit.Test;

import com.cumulocity.rest.representation.inventory.ManagedObjectRepresentation;
import com.cumulocity.sdk.client.SDKException;

public class BufferRequestServiceTest {

    private static final long QUEUE_CAPACITY = 5;
    BufferRequestService bufferRequestService = new BufferRequestService(new MemoryBasedPersistentProvider(QUEUE_CAPACITY));

    @Test
    public void shouldReturnNextId() {
        assertThat(bufferRequestService.create(new BufferedRequest())).isEqualTo(1);
        assertThat(bufferRequestService.create(new BufferedRequest())).isEqualTo(2);
        assertThat(bufferRequestService.create(new BufferedRequest())).isEqualTo(3);
    }
    
    @Test
    public void shouldReturnResponse() {
        long requestId = bufferRequestService.create(new BufferedRequest());
        Result result = new Result();
        result.setResponse(new ManagedObjectRepresentation());
        bufferRequestService.addResponse(requestId, result);
        
        assertThat(bufferRequestService.getResponse(requestId)).isInstanceOf(ManagedObjectRepresentation.class);
    }
    
    @Test(expected = SDKException.class)
    public void shouldThrowException() {
        long requestId = bufferRequestService.create(new BufferedRequest());
        Result result = new Result();
        result.setException(new SDKException(""));
        bufferRequestService.addResponse(requestId, result);
        
        bufferRequestService.getResponse(requestId);
    }
    
    @Test(expected = IllegalStateException.class)
    public void shouldThrowExceptionWhenQueueIsFull() {
        for (int i = 0; i < QUEUE_CAPACITY + 1; i++) {
            bufferRequestService.create(new BufferedRequest());
        }
    }
}
