package com.cumulocity.sdk.client.notification;

import static java.util.concurrent.TimeUnit.MINUTES;
import static org.joda.time.Minutes.minutes;

import java.util.Collection;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;

import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class ConnectionHeartBeatWatcher {

    private static final Logger log = LoggerFactory.getLogger(ConnectionHeartBeatWatcher.class);

    private final ScheduledExecutorService executorService;

    private ScheduledFuture<?> task;

    private DateTime lastHeartBeat = new DateTime();

    private final Collection<ConnectionIdleListener> listeners = new CopyOnWriteArrayList<ConnectionIdleListener>();

    ConnectionHeartBeatWatcher(ScheduledExecutorService executorService) {
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
                log.debug("the connection is still active, last message was  {} ", lastHeartBeat);
            }

            private void onConnectionIdle() {
                log.warn("canceling the long poll request because of inactivity");
                for (ConnectionIdleListener listener : listeners) {
                    listener.onConnectionIdle();
                }
            }

            private boolean isExeededInterval() {
                return !new Interval(lastHeartBeat, minutes(12)).containsNow();
            }
        }, 12, 11, MINUTES);
    }

    public void heatBeat() {
        lastHeartBeat = new DateTime();
    }

    public void stop() {
        task.cancel(true);
    }

    public void addConnectionListener(ConnectionIdleListener listener) {
        listeners.add(listener);
    }

    public void removeConnectionListener(ConnectionIdleListener listener) {
        listeners.remove(listener);
    }
}
