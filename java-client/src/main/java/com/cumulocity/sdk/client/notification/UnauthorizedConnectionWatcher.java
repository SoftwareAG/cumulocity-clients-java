package com.cumulocity.sdk.client.notification;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

import static org.apache.http.HttpStatus.SC_UNAUTHORIZED;


class UnauthorizedConnectionWatcher {

    private static final Logger log = LoggerFactory.getLogger(UnauthorizedConnectionWatcher.class);

    public static final int DEFAULT_RETRY_COUNT = 5;

    private final List<ConnectionListener> listeners = new ArrayList<>();

    private final int maxRetryCount;

    private int retryCounter;

    UnauthorizedConnectionWatcher() {
        this(DEFAULT_RETRY_COUNT);
    }

    UnauthorizedConnectionWatcher(int maxRetryCount) {
        this.maxRetryCount = maxRetryCount;
        this.retryCounter = maxRetryCount;
    }

    public void unauthorizedAccess() {
        if (shouldRetry()) {
            log.info("bayeux client received 401, still trying '{}' times before stopping reconnection", retryCounter);
        } else {
            log.warn("bayeux client received 401 too many times -> do no longer reconnect");
            for (ConnectionListener listener : listeners) {
                listener.onDisconnection(SC_UNAUTHORIZED);
            }
        }
        retryCounter--;
    }

    public boolean shouldRetry() {
        return retryCounter > 0;
    }

    public void resetCounter() {
        log.debug("Resetting unauthorized retry counter");
        retryCounter = maxRetryCount;
    }

    public void addListener(final ConnectionListener listener) {
        listeners.add(listener);
    }
}
