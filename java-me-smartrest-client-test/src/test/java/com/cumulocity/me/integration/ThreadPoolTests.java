package com.cumulocity.me.integration;

import java.util.concurrent.locks.ReentrantLock;

import org.junit.Ignore;
import org.junit.Test;

import com.cumulocity.me.sdk.SDKException;
import com.cumulocity.me.smartrest.client.impl.SmartExecutorServiceImpl;

@Ignore
public class ThreadPoolTests {

    private SmartExecutorServiceImpl executor;
    
    
    @Test
    public void test() throws InterruptedException {
        executor = new SmartExecutorServiceImpl(2);
        
        Task task1 = new Task(1,2000);
        Task task2 = new Task(2,7000);
        Task task3 = new Task(3,7000);
        Task task4 = new Task(4,2000);
        Task task5 = new Task(5,2000);
        Task task6 = new Task(6,2000);
        Task task7 = new Task(7,5000);
        
        System.out.println("task1:");
        executor.execute(task1);
        
        Thread.sleep(5000);

        System.out.println("task2:");
        executor.execute(task2);
        System.out.println("task3:");
        executor.execute(task3);
        try {
            System.out.println("task4:");
            executor.execute(task4);
        } catch (SDKException e) {
            System.out.println(e);
        }
            
        Thread.sleep(10000);
        
        System.out.println("task5:");
        executor.execute(task5);
        
        Thread.sleep(5000);
        
        executor.terminateAll();
        
        Thread.sleep(2000);
        
        System.out.println("task6:");
        executor.execute(task6);
        System.out.println("task7:");
        executor.execute(task7);
        
        Thread.sleep(3000);
        
        executor.terminateAllIdle();
        
        Thread.sleep(5000);
        
    }
    
    
    private class Task implements Runnable {

        private int number;
        
        private int sleep;
        
        public Task(int number, int sleep) {
            this.number = number;
            this.sleep = sleep;
        }
        
        public void run() {
            System.out.println("start task " + number);
            try {
                Thread.currentThread();
                Thread.sleep(sleep);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("stop task " + number);
        }
        
    }
}
