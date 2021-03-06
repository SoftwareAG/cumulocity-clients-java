package com.cumulocity.sdk.client.notification;

public interface SubscribeOperationListener {

    void onSubscribingSuccess(String channelId);

    void onSubscribingError(String channelId, String error, Throwable throwable);
}
