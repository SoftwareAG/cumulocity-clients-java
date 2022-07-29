package com.cumulocity.sdk.client.common;

import com.cumulocity.sdk.client.PlatformParameters;
import com.cumulocity.sdk.client.notification.RealtimeNotificationSubscriber;
import lombok.experimental.UtilityClass;

@UtilityClass
public class Subscribers {

    public static <T> RealtimeNotificationSubscriber<T> getSubscriberForType(Class<T> type, PlatformParameters platform) {
        return new RealtimeNotificationSubscriber<>(platform, type);
    }
}
