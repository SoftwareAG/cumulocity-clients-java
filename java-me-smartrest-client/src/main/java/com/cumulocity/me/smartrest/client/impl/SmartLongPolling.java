package com.cumulocity.me.smartrest.client.impl;

class SmartLongPolling implements Runnable {

    private SmartCometClient client;

    public SmartLongPolling(SmartCometClient client) {
        this.client = client;
    }
    
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
