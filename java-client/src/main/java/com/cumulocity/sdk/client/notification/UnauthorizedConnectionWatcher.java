package com.cumulocity.sdk.client.notification;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

import static org.apache.commons.httpclient.HttpStatus.SC_UNAUTHORIZED;

class UnauthorizedConnectionWatcher {

    private final Logger log = LoggerFactory.getLogger(UnauthorizedConnectionWatcher.class);

    private static final int RETRY_COUNT_AFTER_UNAUTHORIZED = 5;

    private final List<ConnectionListener> listeners = new ArrayList<>();

    private int retryCounter = RETRY_COUNT_AFTER_UNAUTHORIZED;

    public void unauthorizedAccess() {
        retryCounter--;
        if (shouldRetry()) {
            log.info("bayeux client received 401, still trying '{}' times before stopping reconnection", retryCounter);
        } else {
            log.warn("bayeux client received 401 too many times -> do no longer reconnect");
            for (ConnectionListener listener : listeners) {
                listener.onDisconnection(SC_UNAUTHORIZED);
            }
        }
    }

    public boolean shouldRetry() {
        return retryCounter > 0;
    }

    public void resetCounter() {
        log.debug("Resetting unauthorized retry counter");
        retryCounter = RETRY_COUNT_AFTER_UNAUTHORIZED;
    }

    public void addListener(final ConnectionListener listener) {
        listeners.add(listener);
    }
}
