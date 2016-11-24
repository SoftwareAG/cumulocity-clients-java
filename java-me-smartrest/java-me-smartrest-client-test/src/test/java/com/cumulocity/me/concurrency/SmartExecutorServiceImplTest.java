package com.cumulocity.me.concurrency;

import com.cumulocity.me.concurrent.impl.SmartExecutorServiceImpl;
import com.cumulocity.me.concurrent.model.Future;
import org.junit.Assert;
import org.junit.Test;

public class SmartExecutorServiceImplTest {

    SmartExecutorServiceImpl executorService = new SmartExecutorServiceImpl();

    @Test(timeout = 5000)
    public void shouldScheduleAllTasks() throws Exception{
        //given
        int amount = 10;
        final int[] executed = new int[1];
        final Future[] futures = new Future[amount];

        //when
        for (int i = 0; i < amount; i++) {
            futures[i] = executorService.execute(new Runnable() {
                @Override
                public void run() {
                    executed[0]++;
                }
            });
        }
        for (int i = 0; i < futures.length; i++) {
            futures[i].get();
        }

        //then
        Assert.assertEquals(amount, executed[0]);
    }

    @Test()
    public void shouldHaveCorrectCount() throws Exception{
        //given
        Assert.assertEquals(0, executorService.getCurrentThreads());
        shouldScheduleAllTasks();
        Assert.assertEquals(0, executorService.getCurrentThreads());
        //when
        Future future = executorService.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        //then
        Assert.assertEquals(1, executorService.getCurrentThreads());
        future.get();
        Assert.assertEquals(0, executorService.getCurrentThreads());
    }
}
