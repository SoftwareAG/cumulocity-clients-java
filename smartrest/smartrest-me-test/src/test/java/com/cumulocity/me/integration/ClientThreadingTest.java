package com.cumulocity.me.integration;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.cumulocity.SDKException;
import com.cumulocity.smartrest.client.SmartExecutorService;
import com.cumulocity.smartrest.client.impl.SmartExecutorServiceImpl;

@Ignore
public class ClientThreadingTest {
    
    private SmartExecutorService executorService;
    
    private int startcount;
    
    @Before
    public void init() {
        executorService = new SmartExecutorServiceImpl();
    }
    
    @Test
    public void correctNumber() {
        try {
            startcount = Thread.activeCount();
            executorService.execute(spawnRunnable(1, 10000));
            Assert.assertEquals(startcount + 1, Thread.activeCount());
            executorService.execute(spawnRunnable(2, 10000));
            Assert.assertEquals(startcount + 2, Thread.activeCount());
            executorService.execute(spawnRunnable(3, 10000));
            Assert.assertEquals(startcount + 3, Thread.activeCount());
            executorService.execute(spawnRunnable(4, 10000));
            Assert.assertEquals(startcount + 4, Thread.activeCount());
            executorService.execute(spawnRunnable(5, 5000));
            Assert.assertEquals(startcount + 5, Thread.activeCount());
            Thread.sleep(6000);
            executorService.execute(spawnRunnable(6, 3000));
            Assert.assertEquals(startcount + 5, Thread.activeCount());
            Thread.sleep(4000);
            executorService.execute(spawnRunnable(7, 3000));
            Assert.assertEquals(startcount + 5, Thread.activeCount());
            Thread.sleep(6000);
            executorService.execute(spawnRunnable(8, 1));
            Assert.assertEquals(startcount + 5, Thread.activeCount());
            executorService.execute(spawnRunnable(9, 1));
            Assert.assertEquals(startcount + 5, Thread.activeCount());
        } catch (Exception e) {
            Assert.assertTrue(false);
        }
    }
    
    @Test
    public void tooManyThreads() {
        try {
            startcount = Thread.activeCount();
            executorService.execute(spawnRunnable(1, 10000));
            Assert.assertEquals(startcount + 1, Thread.activeCount());
            executorService.execute(spawnRunnable(2, 10000));
            Assert.assertEquals(startcount + 2, Thread.activeCount());
            executorService.execute(spawnRunnable(3, 10000));
            Assert.assertEquals(startcount + 3, Thread.activeCount());
            executorService.execute(spawnRunnable(4, 10000));
            Assert.assertEquals(startcount + 4, Thread.activeCount());
            executorService.execute(spawnRunnable(5, 5000));
            Assert.assertEquals(startcount + 5, Thread.activeCount());
            Thread.sleep(6000);
            executorService.execute(spawnRunnable(6, 1000));
            Assert.assertEquals(startcount + 5, Thread.activeCount());
            Thread.sleep(2000);
            executorService.execute(spawnRunnable(7, 3000));
            Assert.assertEquals(startcount + 5, Thread.activeCount());
            executorService.execute(spawnRunnable(8, 3000));
            Thread.sleep(2000);
            Assert.assertTrue(false);
        } catch (SDKException e) {
            System.out.println(e);
            Assert.assertTrue(true);
        } catch (Exception e) {
            Assert.assertTrue(false);
        }
    }
    
    @After
    public void waitAfterTest() {
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    
    public Runnable spawnRunnable(final int number, final int timeout) {
        return new Runnable() {
            
            @Override
            public void run() {
                System.out.println("start runnable: " + number);
                try {
                    Thread.sleep(timeout);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("stop runnable: " + number);
            }
        };
    }

}
