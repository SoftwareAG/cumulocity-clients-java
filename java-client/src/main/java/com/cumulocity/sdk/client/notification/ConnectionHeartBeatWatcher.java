package com.cumulocity.sdk.client.notification;

import static java.util.concurrent.TimeUnit.MINUTES;
import static org.joda.time.Minutes.minutes;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;

import org.joda.time.Interval;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

final class ConnectionHeartBeatWatcher {

    private static final Logger log = LoggerFactory.getLogger(ConnectionHeartBeatWatcher.class);

    private final MessageExchange handler;

    private final ScheduledExecutorService executorService;

    private ScheduledFuture<?> task;

    ConnectionHeartBeatWatcher(MessageExchange responseHandler, ScheduledExecutorService executorService) {
        this.handler = responseHandler;
        this.executorService = executorService;
    }

    public void start() {
        task = executorService.scheduleWithFixedDelay(new Runnable() {
            public void run() {
                if (isExeededInterval()) {
                    onConnectionIdle();
                } else {
                    onConnectionActive();
                }

            }

            private void onConnectionActive() {
                log.debug("the connection is still active because last message was  {} ", handler.getLastHeartBeat());
            }

            private void onConnectionIdle() {
                log.warn("canceling the long poll request because of inactivity");
                handler.cancel();
            }

            private boolean isExeededInterval() {
                return !new Interval(handler.getLastHeartBeat(), minutes(12)).containsNow();
            }
        }, 12, 11, MINUTES);
    }

    public void stop() {
        task.cancel(true);
    }
}
