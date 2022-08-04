package com.cumulocity.sdk.client.common;

import com.cumulocity.sdk.client.SDKException;
import com.cumulocity.sdk.client.notification.SubscribeOperationListener;
import com.cumulocity.sdk.client.notification.Subscription;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class TestSubscriptionListener<T> implements SubscribeOperationListener, com.cumulocity.sdk.client.notification.SubscriptionListener<String, T> {
    private boolean subscribed = false;
    private final List<T> notifications = new ArrayList<>();

    public T getNotification() {
        return notifications.get(0);
    }

    public boolean notificationReceived() {
        return !notifications.isEmpty();
    }

    @Override
    public void onNotification(Subscription<String> subscription, T received) {
        this.notifications.add(received);
    }

    @Override
    public void onError(Subscription<String> subscription, Throwable ex) {
        throw new SDKException("Error occurred");
    }

    @Override
    public void onSubscribingSuccess(String channelId) {
        this.subscribed = true;
    }

    @Override
    public void onSubscribingError(String channelId, String error, Throwable throwable) {
        throw new SDKException("Error occurred");
    }
}
