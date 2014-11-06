package com.cumulocity.me.smartrest.client.impl;

import com.cumulocity.me.smartrest.client.SmartConnection;

public class SmartHeartBeatWatcher {
    
    public static int HEARTBEAT_CHECK_INTERVAL = 60000;
    
    private SmartConnection connection;
    private Thread watcherThread;
    private boolean heartbeat;
    private Thread readerThread;
    
    public SmartHeartBeatWatcher (SmartConnection connection, Thread readerThread) {
        this.connection = connection;
        this.readerThread = readerThread;
    }
    
    public void start() {
        watcherThread = new Thread(new HeartBeatWatcher());
        watcherThread.start();
        System.out.println("watch: "+ watcherThread.getName());
        System.out.println("read: "+ readerThread.getName());
    }
    
    public void stop() {
        if (watcherThread.isAlive()) {
            watcherThread.interrupt();
        }
    }
    
    public synchronized void heartbeat() {
        heartbeat = true;
    }
    
    private class HeartBeatWatcher implements Runnable {
        public void run() {
            do {
                try {
                    Thread.sleep(HEARTBEAT_CHECK_INTERVAL);
                } catch (InterruptedException e) {
                    break;
                }
            } while (checkConnection());
        }
        
        private boolean checkConnection() {
            System.out.println("check connection");
            synchronized (connection) {
                if (!heartbeat) {
                    System.out.println("Interrupt: "+ readerThread.getName());
                    connection.closeConnection();
                    readerThread.interrupt();
                    return false;
                 }
                 heartbeat = false;
                 return true;
            }
        }
    }
}
