package com.cumulocity.sdk.client.messaging.notifications;

import com.cumulocity.model.idtype.GId;
import com.cumulocity.sdk.client.Filter;
import com.cumulocity.sdk.client.ParamSource;

public class NotificationSubscriptionFilter extends Filter {

    @ParamSource
    private String source;

    @ParamSource
    private String context;

    /**
     * Specifies the {@code source} query parameter
     *
     * @param source the managed object that has been subscribed to
     * @return the filter with {@code source} set
     */
    public NotificationSubscriptionFilter bySource(GId source) {
        this.source = source.getValue();
        return this;
    }

    /**
     * @return the {@code source} parameter of the query
     */
    public String getSource() {
        return source;
    }

    /**
     * Specifies the {@code context} query parameter
     *
     * @param context the context to which the subscription is associated with.
     * @return the filter with {@code context} set
     */
    public NotificationSubscriptionFilter byContext(String context) {
        this.context = context;
        return this;
    }

    /**
     * @return the {@code context} parameter of the query
     */
    public String getContext() {
        return context;
    }
}
