package com.cumulocity.sdk.client.polling;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.Ignore;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import com.cumulocity.sdk.client.polling.AlteringRateResultPoller.GetResultTask;

public class AlteringRateResultPollerTest {
    
    private GetResultTask<String> task = mock(GetResultTask.class);

    @Ignore
    @Test
    public void shouldExecuteAllPlannedCallsThenExpire() throws Exception {
        final AtomicInteger counter = new AtomicInteger(3);
        doAnswer(new Answer<Long>() {

            @Override
            public Long answer(InvocationOnMock invocation) throws Throwable {                
                counter.decrementAndGet();
                return null;
            }
            
        }).when(task).tryGetResult();
        
        PollingStrategy pollingStrategy = new PollingStrategy(1000L, MILLISECONDS, 100L, 200L, 300L);
        pollingStrategy.setRepeatLast(false);
        AlteringRateResultPoller<String> poller = new AlteringRateResultPoller<String>(task, pollingStrategy);
        poller.startAndPoll();
        
        assertThat(counter.get()).isEqualTo(0);
    }
    
    @Test
    public void shouldPollUntilAccessResult() throws Exception {
        final AtomicInteger counter = new AtomicInteger(3);
        doAnswer(new Answer<String>() {
            
            @Override
            public String answer(InvocationOnMock invocation) throws Throwable {
                counter.decrementAndGet();
                if(counter.get() == 0){
                    return "OK";
                }
                return null;
            }
            
        }).when(task).tryGetResult();
        
        PollingStrategy pollingStrategy = new PollingStrategy(null, MILLISECONDS, 1L);
        pollingStrategy.setRepeatLast(true);
        AlteringRateResultPoller<String> poller = new AlteringRateResultPoller<String>(task, pollingStrategy);
        String result = poller.startAndPoll();
        
        assertThat(result).isEqualTo("OK");
    }

}
