package com.cumulocity.me.smartrest.client.impl;

import com.cumulocity.me.smartrest.client.SmartConnection;
import net.sf.microlog.core.Logger;
import net.sf.microlog.core.LoggerFactory;

public class SmartHeartBeatWatcher {
    public static final Logger LOG = LoggerFactory.getLogger(SmartHeartBeatWatcher.class);
    public static final int HEARTBEAT_CHECK_INTERVAL = 12 * 60 * 1000;
    
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
    }
    
    public void stop() {
        if (watcherThread.isAlive()) {
            watcherThread.interrupt();
        }
    }
    
    public synchronized void heartbeat() {
        heartbeat = true;
        LOG.debug("Heartbeat called");
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
            synchronized (connection) {
                if (!heartbeat) {
                    connection.closeConnection();
                    readerThread.interrupt();
                    try {
                    	Thread.sleep(2000);
                    } catch (Exception e) {
					}
                    return false;
                 }
                 heartbeat = false;
                 return true;
            }
        }
    }
}
