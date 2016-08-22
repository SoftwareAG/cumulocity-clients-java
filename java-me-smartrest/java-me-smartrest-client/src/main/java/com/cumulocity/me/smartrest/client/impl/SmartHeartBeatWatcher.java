package com.cumulocity.me.smartrest.client.impl;

import com.cumulocity.me.smartrest.client.SmartConnection;
import net.sf.microlog.core.Logger;
import net.sf.microlog.core.LoggerFactory;

public class SmartHeartBeatWatcher {
    public static final Logger LOG = LoggerFactory.getLogger(SmartHeartBeatWatcher.class);
    public static final int DEFAULT_HEARTBEAT_CHECK_INTERVAL = 12 * 60 * 1000;

    private SmartConnection connection;
    private Thread watcherThread;
    private boolean heartbeat = false;
    private Thread readerThread;
    private final int heartbeatCheckInterval;

    public SmartHeartBeatWatcher (SmartConnection connection, Thread readerThread) {
        this(connection, readerThread, DEFAULT_HEARTBEAT_CHECK_INTERVAL);
    }

    public SmartHeartBeatWatcher(SmartConnection connection, Thread readerThread, int heartbeatCheckInterval) {
        this.connection = connection;
        this.readerThread = readerThread;
        if (heartbeatCheckInterval > 0) {
            this.heartbeatCheckInterval = heartbeatCheckInterval;
        } else {
            this.heartbeatCheckInterval = DEFAULT_HEARTBEAT_CHECK_INTERVAL;
        }
    }

    public void start() {
        LOG.debug("Heartbeat watcher started");
        watcherThread = new Thread(new HeartBeatWatcher());
        watcherThread.start();
    }
    
    public void stop() {
        if (watcherThread.isAlive()) {
            watcherThread.interrupt();
        }
    }
    
    public void heartbeat() {
        heartbeat = true;
        LOG.debug("Heartbeat called");
    }
    
    private class HeartBeatWatcher implements Runnable {
        public void run() {
            do {
                try {
                    Thread.sleep(heartbeatCheckInterval);
                } catch (InterruptedException e) {
                    break;
                }
            } while (checkConnection());
        }
        
        private boolean checkConnection() {
            LOG.debug("Checking realtime connection");
            if (!heartbeat) {
                LOG.info("No heartbeat within interval, closing connection");
                connection.closeConnection();
                readerThread.interrupt();
                try {
                    Thread.sleep(2000);
                } catch (Exception e) {
                }
                return false;
             }
             LOG.debug("Heartbeat found within interval, longpolling OK");
             heartbeat = false;
             return true;
        }
    }
}
