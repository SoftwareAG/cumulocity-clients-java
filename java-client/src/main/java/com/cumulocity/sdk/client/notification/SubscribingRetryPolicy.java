package com.cumulocity.sdk.client.notification;

/**
 * Define how many times a subscribing on a channel can retried. Maximum is {@value MAX_RETRIES}.
 */
public final class SubscribingRetryPolicy {

    private static final int MAX_RETRIES = 10;

    /** Then you need to handle retry manually by implementing @{@link SubscribeOperationListener#onSubscribingError(String, String, Throwable)} */
    public static final SubscribingRetryPolicy NO_RETRY = new SubscribingRetryPolicy(0);

    public static final SubscribingRetryPolicy ONE_RETRY = new SubscribingRetryPolicy(1);

    public static final SubscribingRetryPolicy TWO_RETRIES = new SubscribingRetryPolicy(2);


    /**
     * X retries, x <= {@value MAX_RETRIES}.
     *
     * @param retries
     * @return
     */
    public static final SubscribingRetryPolicy xRetries(int retries) {
        if(retries > MAX_RETRIES) {
            throw new IllegalArgumentException("Maximum: 10, actual: " + retries);
        }
        return new SubscribingRetryPolicy(retries);
    }

    private SubscribingRetryPolicy(int retries) {
        this.retries = retries;
    }

    private int retries = 0;

    public int getRetries() {
        return retries;
    }
}
