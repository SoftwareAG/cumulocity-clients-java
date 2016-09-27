package com.cumulocity.sdk.client.notification;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UnauthorizedConnectionWatcher {

    private final Logger log = LoggerFactory.getLogger(UnauthorizedConnectionWatcher.class);

    private static final int RETRY_COUNT_AFTER_UNAUTHORIZED = 5;

    private int retryCounter = 0;

    public boolean shouldRetry() {
        log.info("Received unauthorized access, still trying '{}' times before stopping reconnection to bayeux", (RETRY_COUNT_AFTER_UNAUTHORIZED - retryCounter - 1));
        retryCounter++;
        return retryCounter < RETRY_COUNT_AFTER_UNAUTHORIZED;
    }

    public void resetCounter() {
        log.debug("Resetting unauthorized retry counter");
        retryCounter = 0;
    }

}
