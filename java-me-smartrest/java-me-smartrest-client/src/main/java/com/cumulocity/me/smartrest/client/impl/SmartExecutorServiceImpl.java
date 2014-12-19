package com.cumulocity.me.smartrest.client.impl;

import java.util.Vector;

import com.cumulocity.me.sdk.SDKException;
import com.cumulocity.me.smartrest.client.SmartExecutorService;


/**
 * TODO: add queue for threads instead of fix limit
 * TODO: don't return thread and instead mimic Future from java sdk
 */

public class SmartExecutorServiceImpl implements SmartExecutorService {
    
    public static int MAX_THREADS_DEFAULT = 5;
    
    private Vector threadPool;
    
    private int maxThreads;
    
    public SmartExecutorServiceImpl() {
        threadPool = new Vector(MAX_THREADS_DEFAULT);
        this.maxThreads = MAX_THREADS_DEFAULT;
    }
    
    public SmartExecutorServiceImpl(int maxThreads) {
        threadPool = new Vector(maxThreads);
        this.maxThreads = maxThreads;
    }
    
    public void terminateAll() {
        for(int i = 0; i < threadPool.size(); i++) {
            ((SmartThread) threadPool.elementAt(i)).interrupt();
        }
        threadPool.removeAllElements();
    }
    
    public void terminateAllIdle() {
        for(int i = 0; i < threadPool.size(); i++) {
            SmartThread thread = (SmartThread) threadPool.elementAt(i);
            if (thread.isIdle()) {
                thread.interrupt();
                threadPool.removeElementAt(i);
            }
        }
    }
    
    public Thread execute(Runnable task) {
        SmartThread thread = checkForIdleThread(); 
        if (thread != null) {
            synchronized (thread) {
                thread.giveNewTask(task);
                thread.setIdle(false);
                thread.notify();
            }
            return thread;
        } 
        if(threadPool.size() < maxThreads) {
            thread = new SmartThread(task);
            threadPool.addElement(thread);
            thread.start();
            return thread;
        }
        throw new SDKException("ThreadPool cannot execute more tasks");
    }
    
    private SmartThread checkForIdleThread() {
        for(int i = 0; i < threadPool.size(); i++) {
            SmartThread thread = (SmartThread) threadPool.elementAt(i);
            if (!thread.isAlive()) {
                threadPool.removeElementAt(i);
            }
            if (thread.isIdle()) {
                return thread;
            }
        }
        return null;
    }
    
    private class SmartThread extends Thread {
        
        private boolean idle;
        
        private Runnable task;
        
        public SmartThread(Runnable task) {
            this.task = task;
            this.idle = false;
        }
        
        public void run() {
            endThread:
            do {
                synchronized (this) {
                    while(idle) {
                        try {
                            this.wait();
                        } catch (InterruptedException e) {
                            idle = true;
                            break endThread;
                        }
                    }
                    task.run();
                    idle = true;
                }
            }while(true);
        }
        
        public void interrupt() {
            super.interrupt();
            this.idle = true;
        }
        
        private boolean isIdle() {
            return idle;
        }
        
        private synchronized void setIdle(boolean idle) {
            this.idle = idle;
        }
        
        private synchronized void giveNewTask(Runnable task) {
            this.task = task;
        }
    }
}
