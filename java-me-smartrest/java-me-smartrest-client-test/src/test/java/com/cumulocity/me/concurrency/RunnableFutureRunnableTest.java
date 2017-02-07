package com.cumulocity.me.concurrency;

import com.cumulocity.me.concurrent.exception.ExecutionException;
import com.cumulocity.me.concurrent.exception.TimeoutException;
import com.cumulocity.me.concurrent.impl.RunnableFutureRunnable;
import com.cumulocity.me.concurrent.impl.SmartExecutorServiceImpl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class RunnableFutureRunnableTest {
    private SmartExecutorServiceImpl executorService = new SmartExecutorServiceImpl();
    private RunnableFutureRunnable future;

    @Before
    public void before() {
        future = null;
    }

    @Test(expected = TimeoutException.class)
    public void shouldTimeoutCorrectly() throws Exception {
        //given
        initFuture(null);
        //when
        future.get(500);

        //then
    }

    @Test()
    public void shouldBlockUntilExecuted() throws Exception {
        //given
        initFuture(null);
        final Exception[] possibleException = new Exception[1];
        //when
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Assert.assertFalse(future.isDone());
                    future.execute();
                    Assert.assertTrue(future.isDone());
                } catch (Exception e) {
                    possibleException[0] = e;
                }
            }
        }).start();
        //then

        Assert.assertNull(possibleException[0]);
        Assert.assertNull(future.get());
        Assert.assertTrue(future.isDone());
    }

    @Test(expected = ExecutionException.class)
    public void shouldThrowExecutionException() throws Exception {
        //given
        RuntimeException exception = new RuntimeException();
        initFuture(exception);

        //when
        future.execute();
        Assert.assertTrue(future.isDone());

        future.get();
    }

    private void initFuture(final RuntimeException toThrow) {
        future = new RunnableFutureRunnable(executorService, new Runnable(){
            @Override
            public void run() {
                if (toThrow != null) {
                    throw toThrow;
                }
            }
        });
    }
}
