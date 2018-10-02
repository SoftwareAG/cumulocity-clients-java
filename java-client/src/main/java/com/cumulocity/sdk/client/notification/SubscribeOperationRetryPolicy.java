package com.cumulocity.sdk.client.notification;

import static com.cumulocity.sdk.client.notification.SubscribeOperationRetryPolicy.Mode.AUTO;
import static com.cumulocity.sdk.client.notification.SubscribeOperationRetryPolicy.Mode.MANUAL;

/**
 * Define how many times a subscribing on a channel can retried. Maximum is {@value MAX_RETRIES}.
 * If mode is {@link Mode#AUTO}, the sdk will try to retry automatically if the operation is detected as failed.
 */
public final class SubscribeOperationRetryPolicy {

    private static final int MAX_RETRIES = 10;

    /** Then you need to handle retry manually by implementing @{@link SubscribeOperationListener#onSubscribingError(String, String, Throwable)} */
    public static final SubscribeOperationRetryPolicy NO_RETRY = new SubscribeOperationRetryPolicy(MANUAL, 0);

    public static final SubscribeOperationRetryPolicy ONE_RETRY = new SubscribeOperationRetryPolicy(MANUAL, 1);

    public static final SubscribeOperationRetryPolicy TWO_RETRIES = new SubscribeOperationRetryPolicy(MANUAL, 2);


    /**
     * Manual X retries, x <= {@value MAX_RETRIES}.
     *
     * @param retries
     * @return
     */
    public static final SubscribeOperationRetryPolicy manualXRetries(int retries) {
        if(retries > MAX_RETRIES) {
            throw new IllegalArgumentException("Maximum: 10, actual: " + retries);
        }
        return new SubscribeOperationRetryPolicy(MANUAL, retries);
    }

    /**
     * Auto retries
     *
     * @return
     */
    public static final SubscribeOperationRetryPolicy auto() {
        return new SubscribeOperationRetryPolicy(AUTO);
    }

    /**
     * Auto retries
     *
     * @return
     */
    public static final SubscribeOperationRetryPolicy semi(int manualRetries) {
        return new SubscribeOperationRetryPolicy(AUTO, manualRetries);
    }

    private SubscribeOperationRetryPolicy(Mode mode, int retries) {
        this.mode = mode;
        this.retries = retries;
    }

    private SubscribeOperationRetryPolicy(Mode mode) {
        this.mode = mode;
        this.retries = 0;
    }

    private int retries = 0;

    private Mode mode = AUTO;

    public int getRetries() {
        return retries;
    }

    public SubscribeOperationRetryPolicy useOnce() {
        this.retries --;
        return this;
    }

    public Mode getMode() {
        return mode;
    }

    public enum Mode {
        MANUAL,
        AUTO
    }
}
