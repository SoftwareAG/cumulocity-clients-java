/*
 * Copyright (C) 2013 Cumulocity GmbH
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of 
 * this software and associated documentation files (the "Software"),
 * to deal in the Software without restriction, including without limitation the rights to use,
 * copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software,
 * and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
 * OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE,
 * ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
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

    public static final int HEARTBEAT_INTERVAL = 12;

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
                return !new Interval(lastHeartBeat, minutes(HEARTBEAT_INTERVAL)).containsNow();
            }
        }, 12, 11, MINUTES);
    }

    public void heartBeat() {
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
