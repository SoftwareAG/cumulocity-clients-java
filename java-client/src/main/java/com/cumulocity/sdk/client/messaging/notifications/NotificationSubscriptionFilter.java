package com.cumulocity.sdk.client.messaging.notifications;

import com.cumulocity.model.idtype.GId;
import com.cumulocity.sdk.client.Filter;
import com.cumulocity.sdk.client.ParamSource;

public class NotificationSubscriptionFilter extends Filter {

    @ParamSource
    private String source;

    @ParamSource
    private String context;

    @ParamSource
    private String subscription;

    @ParamSource
    private String typeFilter;

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

    /**
     * Specifies the {@code subscription} query parameter.
     * Applicable only for {@link NotificationSubscriptionApi#getSubscriptionsByFilter(NotificationSubscriptionFilter)}.
     *
     * @param subscription subscription name by which filtering will be done.
     * @return the filter with {@code subscription} set
     */
    public NotificationSubscriptionFilter bySubscription(String subscription) {
        this.subscription = subscription;
        return this;
    }

    /**
     * @return the {@code subscription} parameter of the query
     */
    public String getSubscription() {
        return subscription;
    }

    /**
     * Specifies the {@code typeFilter} query parameter.
     * Applicable only for {@link NotificationSubscriptionApi#getSubscriptionsByFilter(NotificationSubscriptionFilter)}.
     *
     * @param typeFilter single type name which will be used to filter subscriptions having this type under `subscriptionFilter.typeFilter` field.
     * @return the filter with {@code typeFilter} set
     */
    public NotificationSubscriptionFilter byTypeFilter(String typeFilter) {
        this.typeFilter = typeFilter;
        return this;
    }

    /**
     * @return the {@code typeFilter} parameter of the query
     */
    public String getTypeFilter() {
        return typeFilter;
    }
}
