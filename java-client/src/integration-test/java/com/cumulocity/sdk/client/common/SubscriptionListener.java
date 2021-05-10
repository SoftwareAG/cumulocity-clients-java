package com.cumulocity.sdk.client.common;

import com.cumulocity.sdk.client.SDKException;
import com.cumulocity.sdk.client.notification.SubscribeOperationListener;
import com.cumulocity.sdk.client.notification.Subscription;
import lombok.Getter;

@Getter
public class SubscriptionListener<T> implements SubscribeOperationListener, com.cumulocity.sdk.client.notification.SubscriptionListener<String, T> {
    private boolean subscribed = false;
    private T notification;

    public boolean notificationReceived() {
        return notification != null;
    }

    @Override
    public void onNotification(Subscription<String> subscription, T received) {
        this.notification = received;
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
