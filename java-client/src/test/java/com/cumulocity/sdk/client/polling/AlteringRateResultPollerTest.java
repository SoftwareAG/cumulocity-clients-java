package com.cumulocity.sdk.client.polling;

import static java.util.Arrays.asList;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import com.cumulocity.sdk.client.polling.AlteringRateResultPoller.GetResultTask;

public class AlteringRateResultPollerTest {
    
    private GetResultTask<String> task = mock(GetResultTask.class);
    
    @Test
    public void shouldExecuteAllPlannedCallsThenExpire() throws Exception {
        final CountDownLatch latch = new CountDownLatch(3);
        doAnswer(new Answer<Long>() {

            @Override
            public Long answer(InvocationOnMock invocation) throws Throwable {                
                latch.countDown();
                return null;
            }
            
        }).when(task).tryGetResult();
        
        PollingStrategy pollingStrategy = new PollingStrategy(false, 1000L, MILLISECONDS, 100L, 200L, 300L);
        AlteringRateResultPoller<String> poller = new AlteringRateResultPoller<String>(task, pollingStrategy);
        poller.startAndPoll();
        
        latch.await(1, SECONDS);
        assertThat(latch.getCount()).isEqualTo(0);
    }
    
    @Test
    public void shouldPollUntilAccessResult() throws Exception {
        final CountDownLatch latch = new CountDownLatch(3);
        doAnswer(new Answer<String>() {
            
            @Override
            public String answer(InvocationOnMock invocation) throws Throwable {
                latch.countDown();
                if(latch.getCount() == 0){
                    return "OK";
                }
                return null;
            }
            
        }).when(task).tryGetResult();
        
        PollingStrategy pollingStrategy = new PollingStrategy(true, null, MILLISECONDS, 1L);
        AlteringRateResultPoller<String> poller = new AlteringRateResultPoller<String>(task, pollingStrategy);
        String result = poller.startAndPoll();
        
        latch.await(1, TimeUnit.SECONDS);
        assertThat(result).isEqualTo("OK");
    }

}
