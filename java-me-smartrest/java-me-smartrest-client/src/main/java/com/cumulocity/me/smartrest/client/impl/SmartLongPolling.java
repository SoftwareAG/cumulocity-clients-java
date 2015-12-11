package com.cumulocity.me.smartrest.client.impl;

class SmartLongPolling implements Runnable {

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
                            break longpolling;
                        }
                        client.connect();
                    } while(client.getReconnectAdvice() == 2);
                    if (client.getReconnectAdvice() == 0) {
                        break longpolling;
                    }
                    client.handshake();
                    client.subscribe();
                } while(client.getReconnectAdvice() == 1);
            }
    }
}
