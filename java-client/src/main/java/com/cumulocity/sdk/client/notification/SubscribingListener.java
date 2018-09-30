package com.cumulocity.sdk.client.notification;

public interface SubscribingListener {

    void onSubscribingSuccess(String channelId);

    void onSubscribingError(String channelId, String error, Throwable throwable);
}
