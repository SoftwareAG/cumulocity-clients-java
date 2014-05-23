package com.cumulocity.sdk.client.buffering;

import static org.fest.assertions.Assertions.assertThat;

import org.junit.Test;

import com.cumulocity.rest.representation.inventory.ManagedObjectRepresentation;
import com.cumulocity.sdk.client.SDKException;

public class BufferRequestServiceTest {

    BufferRequestService bufferRequestService = new BufferRequestService(new MemoryBasedPersistentProvider());

    @Test
    public void shouldReturnId() {
        assertThat(bufferRequestService.create(new HTTPPostRequest())).isEqualTo(1);
        assertThat(bufferRequestService.create(new HTTPPostRequest())).isEqualTo(2);
        assertThat(bufferRequestService.create(new HTTPPostRequest())).isEqualTo(3);
    }
    
    @Test
    public void shouldReturnResponse() {
        long requestId = bufferRequestService.create(new HTTPPostRequest());
        Result result = new Result();
        result.setResponse(new ManagedObjectRepresentation());
        bufferRequestService.addResponse(requestId, result);
        
        assertThat(bufferRequestService.getResponse(requestId)).isInstanceOf(ManagedObjectRepresentation.class);
    }
    
    @Test(expected = SDKException.class)
    public void shouldThrowException() {
        long requestId = bufferRequestService.create(new HTTPPostRequest());
        Result result = new Result();
        result.setException(new SDKException(""));
        bufferRequestService.addResponse(requestId, result);
        
        bufferRequestService.getResponse(requestId);
    }
}
