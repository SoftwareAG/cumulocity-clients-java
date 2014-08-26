package com.cumulocity.me.smartrest.client.impl;

class SmartLongPolling extends Thread {

    private SmartCometClient client;

    public SmartLongPolling(SmartCometClient client) {
        this.client = client;
    }
    
    public void start() {
        Thread thread = new Thread(new LongPollingRunner());
        try {
            thread.start();
        } catch (Exception error) {
        }
    }
    
    private class LongPollingRunner implements Runnable {
        public void run() {
            longpolling:
                do {
                    do {
                        try {
                            Thread.sleep(client.getInterval());
                        } catch (InterruptedException e) {
                            break longpolling;
                        }
                        client.connect();
                    } while(client.getReconnectAdvice() == 2);
                    client.handshake();
                    client.subscribe();
                } while(client.getReconnectAdvice() == 1);
        }
    }
}
