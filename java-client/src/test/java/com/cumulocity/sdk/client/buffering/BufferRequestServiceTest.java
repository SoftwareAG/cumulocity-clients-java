package com.cumulocity.sdk.client.buffering;

import static org.fest.assertions.Assertions.assertThat;

import org.junit.Test;

import com.cumulocity.rest.representation.inventory.ManagedObjectRepresentation;
import com.cumulocity.sdk.client.SDKException;

public class BufferRequestServiceTest {

    private static final int REQUEST_ID = 1;
    private static final long QUEUE_CAPACITY = 5;
    BufferRequestService bufferRequestService = new BufferRequestService(new TestPersistentProvider(QUEUE_CAPACITY));

    @Test
    public void shouldReturnResponse() {
        Future future = bufferRequestService.create(new BufferedRequest());
        Result result = new Result();
        result.setResponse(new ManagedObjectRepresentation());
        bufferRequestService.addResponse(REQUEST_ID, result);
        
        assertThat(future.get()).isInstanceOf(ManagedObjectRepresentation.class);
    }
    
    @Test(expected = SDKException.class)
    public void shouldThrowException() {
        Future future = bufferRequestService.create(new BufferedRequest());
        Result result = new Result();
        result.setException(new SDKException(""));
        bufferRequestService.addResponse(REQUEST_ID, result);
        
        future.get();
    }
    
    @Test(expected = IllegalStateException.class)
    public void shouldThrowExceptionWhenQueueIsFull() {
        for (int i = 0; i < QUEUE_CAPACITY + 1; i++) {
            bufferRequestService.create(new BufferedRequest());
        }
    }
    
    public class TestPersistentProvider extends MemoryBasedPersistentProvider {
        
        public TestPersistentProvider(long bufferLimit) {
            super(bufferLimit);
        }

        @Override
        public long generateId() {
            return REQUEST_ID;
        }

        @Override
        public void offer(ProcessingRequest request) {
            super.offer(request);

        }

        @Override
        public ProcessingRequest poll() {
            return super.poll();
        }

    }
}
