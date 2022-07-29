package com.cumulocity.rest.representation.reliable.notification;

import com.cumulocity.rest.representation.BaseCollectionRepresentation;
import org.svenson.JSONProperty;
import org.svenson.JSONTypeHint;

import java.util.Iterator;
import java.util.List;

public class NotificationSubscriptionCollectionRepresentation extends BaseCollectionRepresentation<NotificationSubscriptionRepresentation> {

    private List<NotificationSubscriptionRepresentation> subscriptions;

    public void setSubscriptions(List<NotificationSubscriptionRepresentation> subscriptions) {
        this.subscriptions = subscriptions;
    }

    @JSONTypeHint(NotificationSubscriptionRepresentation.class)
    public List<NotificationSubscriptionRepresentation> getSubscriptions() {
        return subscriptions;
    }

    @Override
    @JSONProperty(ignore = true)
    public Iterator<NotificationSubscriptionRepresentation> iterator() {
        return subscriptions.iterator();
    }
}

