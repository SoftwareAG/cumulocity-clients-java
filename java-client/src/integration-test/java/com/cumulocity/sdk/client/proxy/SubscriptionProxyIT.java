package com.cumulocity.sdk.client.proxy;

import com.cumulocity.rest.representation.alarm.AlarmRepresentation;
import com.cumulocity.rest.representation.builder.ManagedObjectRepresentationBuilder;
import com.cumulocity.rest.representation.inventory.ManagedObjectRepresentation;
import com.cumulocity.sdk.client.SDKException;
import com.cumulocity.sdk.client.common.TestSubscriptionListener;
import com.cumulocity.sdk.client.notification.Subscriber;
import com.cumulocity.sdk.client.notification.wrappers.RealtimeAlarmMessage;
import org.junit.Test;

import static com.cumulocity.rest.representation.builder.RestRepresentationObjectMother.anAlarmRepresentationLike;
import static com.cumulocity.rest.representation.builder.RestRepresentationObjectMother.anMoRepresentationLike;
import static com.cumulocity.rest.representation.builder.SampleAlarmRepresentation.ALARM_REPRESENTATION;
import static com.cumulocity.rest.representation.builder.SampleManagedObjectRepresentation.MO_REPRESENTATION;
import static com.cumulocity.sdk.client.common.Subscribers.getSubscriberForType;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.awaitility.Awaitility.await;
import static org.awaitility.Durations.TEN_SECONDS;
import static org.junit.Assert.*;

public class SubscriptionProxyIT extends BaseProxyIT {
    @Test
    public void shouldReceiveNotificationThroughNonAuthenticatedProxy() {
        // Given
        givenAuthenticatedProxyAndProxiedPlatform(null, null);
        Subscriber<String, RealtimeAlarmMessage> subscriber = getSubscriberForType(RealtimeAlarmMessage.class, proxiedPlatform);
        ManagedObjectRepresentation mo = platform.getInventoryApi().create(aSampleMo().build());
        TestSubscriptionListener<RealtimeAlarmMessage> subscriptionListener = new TestSubscriptionListener<>();

        // When
        subscriber.subscribe("/alarms/" + mo.getId().getValue(), subscriptionListener, subscriptionListener, true);
        await().atMost(TEN_SECONDS).until(subscriptionListener::isSubscribed);
        AlarmRepresentation alarm = platform.getAlarmApi().create(createAlarmRep(mo));

        // Then
        await().atMost(TEN_SECONDS).until(subscriptionListener::notificationReceived);
        assertThat(subscriptionListener.getNotification()).isExactlyInstanceOf(RealtimeAlarmMessage.class);
        assertAlarmNotification(subscriptionListener.getNotification(), alarm, "CREATE");
    }

    @Test
    public void shouldReceiveNotificationThroughAuthenticatedProxy() {
        // Given
        givenAuthenticatedProxyAndProxiedPlatform(PROXY_AUTH_USERNAME, PROXY_AUTH_PASSWORD);
        Subscriber<String, RealtimeAlarmMessage> subscriber = getSubscriberForType(RealtimeAlarmMessage.class, proxiedPlatform);
        ManagedObjectRepresentation mo = platform.getInventoryApi().create(aSampleMo().build());
        TestSubscriptionListener<RealtimeAlarmMessage> subscriptionListener = new TestSubscriptionListener<>();

        // When
        subscriber.subscribe("/alarms/" + mo.getId().getValue(), subscriptionListener, subscriptionListener, true);
        await().atMost(TEN_SECONDS).until(subscriptionListener::isSubscribed);
        AlarmRepresentation alarm = platform.getAlarmApi().create(createAlarmRep(mo));

        // Then
        await().atMost(TEN_SECONDS).until(subscriptionListener::notificationReceived);
        assertThat(subscriptionListener.getNotification()).isExactlyInstanceOf(RealtimeAlarmMessage.class);
        assertAlarmNotification(subscriptionListener.getNotification(), alarm, "CREATE");
    }

    @Test
    public void shouldFailToSubscribeWhenInvalidAuthentication() {
        // Given
        givenAuthenticatedProxy(PROXY_AUTH_USERNAME, PROXY_AUTH_PASSWORD);
        givenAuthenticatedProxiedPlatform("invalid-username", "invalid-password");
        Subscriber<String, RealtimeAlarmMessage> subscriber = getSubscriberForType(RealtimeAlarmMessage.class, proxiedPlatform);
        ManagedObjectRepresentation mo = platform.getInventoryApi().create(aSampleMo().build());
        TestSubscriptionListener<RealtimeAlarmMessage> subscriptionListener = new TestSubscriptionListener<>();

        // When
        assertThatThrownBy(() -> subscriber.subscribe("/alarms/" + mo.getId().getValue(), subscriptionListener, subscriptionListener, true))
                // Then
                .isInstanceOf(SDKException.class);
        assertFalse(subscriptionListener.isSubscribed());
    }

    private AlarmRepresentation createAlarmRep(ManagedObjectRepresentation source) {
        return anAlarmRepresentationLike(ALARM_REPRESENTATION).withSource(source).build();
    }

    private static ManagedObjectRepresentationBuilder aSampleMo() {
        return anMoRepresentationLike(MO_REPRESENTATION);
    }

    private void assertAlarmNotification(RealtimeAlarmMessage notification, AlarmRepresentation source, String realtimeAction) {
        assertThat(notification.getData().getId()).isEqualTo(source.getId());
        assertThat(notification.getData().getType()).isEqualTo(source.getType());
        assertThat(notification.getData().getSource()).isEqualToComparingFieldByField(source.getSource());
        assertThat(notification.getAttrs().get("realtimeAction")).isEqualTo(realtimeAction);
    }
}
