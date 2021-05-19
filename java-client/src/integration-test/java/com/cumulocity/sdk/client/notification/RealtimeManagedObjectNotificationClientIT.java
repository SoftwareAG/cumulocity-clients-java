package com.cumulocity.sdk.client.notification;

import com.cumulocity.rest.representation.builder.ManagedObjectRepresentationBuilder;
import com.cumulocity.rest.representation.inventory.ManagedObjectRepresentation;
import com.cumulocity.sdk.client.common.JavaSdkITBase;
import com.cumulocity.sdk.client.common.TestSubscriptionListener;
import com.cumulocity.sdk.client.inventory.InventoryApi;
import com.cumulocity.sdk.client.notification.wrappers.RealtimeDeleteRepresentationWrapper;
import com.cumulocity.sdk.client.notification.wrappers.RealtimeManagedObjectMessage;
import org.junit.jupiter.api.Test;

import static com.cumulocity.rest.representation.builder.RestRepresentationObjectMother.anMoRepresentationLike;
import static com.cumulocity.rest.representation.builder.SampleManagedObjectRepresentation.MO_REPRESENTATION;
import static com.cumulocity.sdk.client.common.Subscribers.getSubscriberForType;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.awaitility.Durations.TEN_SECONDS;

public class RealtimeManagedObjectNotificationClientIT extends JavaSdkITBase {
    private static final String MANAGEDOBJECTS = "/managedobjects/";

    final InventoryApi inventoryApi = platform.getInventoryApi();

    @Test
    public void shouldReceiveCreateMONotification() {
        // given
        Subscriber<String, RealtimeManagedObjectMessage> subscriber = getSubscriberForType(RealtimeManagedObjectMessage.class, platform);
        TestSubscriptionListener<RealtimeManagedObjectMessage> subscriptionListener = new TestSubscriptionListener<>();

        // when
        subscriber.subscribe(MANAGEDOBJECTS + "*", subscriptionListener, subscriptionListener, true);
        await().atMost(TEN_SECONDS).until(subscriptionListener::isSubscribed);
        ManagedObjectRepresentation managedObjectRep = inventoryApi.create(aSampleMo().build());

        // then
        await().atMost(TEN_SECONDS).until(subscriptionListener::isSubscribed);
        assertThat(subscriptionListener.getNotification()).isExactlyInstanceOf(RealtimeManagedObjectMessage.class);
        assertManagedObjectNotification(subscriptionListener.getNotification(), managedObjectRep, "CREATE");
    }

    @Test
    public void shouldReceiveUpdateMONotification() {
        // given
        Subscriber<String, RealtimeManagedObjectMessage> subscriber = getSubscriberForType(RealtimeManagedObjectMessage.class, platform);
        ManagedObjectRepresentation mo = inventoryApi.create(aSampleMo().build());
        ManagedObjectRepresentation managedObjectRepresentation = new ManagedObjectRepresentation();
        managedObjectRepresentation.setName("New Name");
        managedObjectRepresentation.setId(mo.getId());
        TestSubscriptionListener<RealtimeManagedObjectMessage> subscriptionListener = new TestSubscriptionListener<>();

        // when
        subscriber.subscribe(MANAGEDOBJECTS + mo.getId().getValue(), subscriptionListener, subscriptionListener, true);
        await().atMost(TEN_SECONDS).until(subscriptionListener::isSubscribed);
        ManagedObjectRepresentation updatedMO = inventoryApi.update(managedObjectRepresentation);

        // then
        await().atMost(TEN_SECONDS).until(subscriptionListener::notificationReceived);
        assertThat(subscriptionListener.getNotification()).isExactlyInstanceOf(RealtimeManagedObjectMessage.class);
        assertManagedObjectNotification(subscriptionListener.getNotification(), updatedMO, "UPDATE");
    }

    @Test
    public void shouldReceiveDeleteMONotification() {
        // given
        Subscriber<String, RealtimeDeleteRepresentationWrapper> subscriber = getSubscriberForType(RealtimeDeleteRepresentationWrapper.class, platform);
        ManagedObjectRepresentation mo = inventoryApi.create(aSampleMo().build());
        TestSubscriptionListener<RealtimeDeleteRepresentationWrapper> subscriptionListener = new TestSubscriptionListener<>();

        // when
        subscriber.subscribe(MANAGEDOBJECTS + mo.getId().getValue(), subscriptionListener, subscriptionListener, true);
        await().atMost(TEN_SECONDS).until(subscriptionListener::isSubscribed);
        inventoryApi.delete(mo.getId());

        // then
        await().atMost(TEN_SECONDS).until(subscriptionListener::notificationReceived);
        assertThat(subscriptionListener.getNotification()).isExactlyInstanceOf(RealtimeDeleteRepresentationWrapper.class);
        assertDeleteManagedObjectNotification(subscriptionListener.getNotification(), mo);
    }

    private static ManagedObjectRepresentationBuilder aSampleMo() {
        return anMoRepresentationLike(MO_REPRESENTATION);
    }

    private void assertManagedObjectNotification(RealtimeManagedObjectMessage notification, ManagedObjectRepresentation source, String realtimeAction) {
        assertThat(notification.getData().getId()).isEqualTo(source.getId());
        assertThat(notification.getData().getType()).isEqualTo(source.getType());
        assertThat(notification.getAttrs().get("realtimeAction")).isEqualTo(realtimeAction);
    }

    private void assertDeleteManagedObjectNotification(RealtimeDeleteRepresentationWrapper notification, ManagedObjectRepresentation source) {
        assertThat(notification.getData()).isEqualTo(source.getId().getValue());
        assertThat(notification.getAttrs().get("realtimeAction")).isEqualTo("DELETE");
    }
}
