package com.cumulocity.sdk.client.messaging.notifications;

import com.cumulocity.rest.representation.reliable.notification.NotificationSubscriptionCollectionRepresentation;
import com.cumulocity.rest.representation.reliable.notification.NotificationSubscriptionRepresentation;
import com.cumulocity.sdk.client.PagedCollectionIterable;
import com.cumulocity.sdk.client.PagedCollectionRepresentation;
import com.cumulocity.sdk.client.PagedCollectionResource;

public class PagedNotificationSubscriptionCollectionRepresentation extends NotificationSubscriptionCollectionRepresentation
        implements PagedCollectionRepresentation<NotificationSubscriptionRepresentation> {

    private final PagedCollectionResource<NotificationSubscriptionRepresentation, ? extends NotificationSubscriptionCollectionRepresentation> collectionResource;

    public PagedNotificationSubscriptionCollectionRepresentation(
            NotificationSubscriptionCollectionRepresentation collection,
            PagedCollectionResource<NotificationSubscriptionRepresentation, ? extends NotificationSubscriptionCollectionRepresentation> collectionResource) {
        setSubscriptions(collection.getSubscriptions());
        setPageStatistics(collection.getPageStatistics());
        setSelf(collection.getSelf());
        setNext(collection.getNext());
        setPrev(collection.getPrev());
        this.collectionResource = collectionResource;
    }

    @Override
    public Iterable<NotificationSubscriptionRepresentation> allPages() {
        return new PagedCollectionIterable<NotificationSubscriptionRepresentation, NotificationSubscriptionCollectionRepresentation>(
                collectionResource, this);
    }

    @Override
    public Iterable<NotificationSubscriptionRepresentation> elements(int limit) {
        return new PagedCollectionIterable<NotificationSubscriptionRepresentation, NotificationSubscriptionCollectionRepresentation>(
                collectionResource, this, limit);
    }
}
