package com.cumulocity.me.smartrest.client.impl;

import net.sf.microlog.core.Logger;
import net.sf.microlog.core.LoggerFactory;

class SmartLongPolling implements Runnable {

    private static final Logger LOG = LoggerFactory.getLogger(SmartLongPolling.class);

    private SmartCometClient client;

    private volatile boolean flag;

    public synchronized void setFlag(boolean flag) {
        this.flag = flag;
    }

    public SmartLongPolling(SmartCometClient client) {
        this.client = client;
        this.flag = true;
    }

    public void run() {
        longpolling:
            if (flag) {
                do {
                    do {
                        try {
                            Thread.sleep(client.getInterval());
                        } catch (InterruptedException e) {
                            LOG.info("Realtime connect interrupted");
                        }
                        LOG.debug("Connecting realtime");
                        client.connect();
                        LOG.debug("Realtime connect returned");
                    } while(client.getReconnectAdvice() == 2);
                    if (client.getReconnectAdvice() == 0) {
                        break longpolling;
                    }
                    LOG.info("Realtime session expired, handshake again");
                    client.handshake();
                    client.subscribe();
                } while(client.getReconnectAdvice() == 1);
            }
    }
}
