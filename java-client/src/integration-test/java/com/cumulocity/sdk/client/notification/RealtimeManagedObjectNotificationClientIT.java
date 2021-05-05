package com.cumulocity.sdk.client.notification;

import com.cumulocity.rest.representation.builder.ManagedObjectRepresentationBuilder;
import com.cumulocity.rest.representation.inventory.ManagedObjectRepresentation;
import com.cumulocity.sdk.client.SDKException;
import com.cumulocity.sdk.client.common.JavaSdkITBase;
import com.cumulocity.sdk.client.inventory.InventoryApi;
import com.cumulocity.sdk.client.notification.wrappers.RealtimeManagedObjectMessage;
import com.cumulocity.sdk.client.notification.wrappers.RealtimeDeleteRepresentationWrapper;
import org.junit.After;
import org.junit.Test;

import static com.cumulocity.rest.representation.builder.RestRepresentationObjectMother.anMoRepresentationLike;
import static com.cumulocity.rest.representation.builder.SampleManagedObjectRepresentation.MO_REPRESENTATION;
import static com.cumulocity.sdk.client.common.Subscribers.getSubscriberForType;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.awaitility.Durations.TEN_SECONDS;

public class RealtimeManagedObjectNotificationClientIT extends JavaSdkITBase {
    private static final String MANAGEDOBJECTS = "/managedobjects/";

    final InventoryApi inventoryApi = platform.getInventoryApi();
    boolean receivedNotification;
    Object notification;

    @After
    public void setUp() {
        notification = null;
        receivedNotification = false;
    }

    @Test
    public void shouldReceiveCreateMONotification() {
        // given
        Subscriber<String, RealtimeManagedObjectMessage> subscriber = getSubscriberForType(RealtimeManagedObjectMessage.class, platform);
        subscriber.subscribe(MANAGEDOBJECTS + "*", new Listener<>());
        // when
        ManagedObjectRepresentation managedObjectRep = inventoryApi.create(aSampleMo().build());
        // then
        await().atMost(TEN_SECONDS).until(() -> receivedNotification);
        assertThat(notification).isExactlyInstanceOf(RealtimeManagedObjectMessage.class);
        assertManagedObjectNotification((RealtimeManagedObjectMessage) notification, managedObjectRep, "CREATE");
    }

    @Test
    public void shouldReceiveUpdateMONotification() {
        // given
        Subscriber<String, RealtimeManagedObjectMessage> subscriber = getSubscriberForType(RealtimeManagedObjectMessage.class, platform);
        ManagedObjectRepresentation mo = inventoryApi.create(aSampleMo().build());
        ManagedObjectRepresentation managedObjectRepresentation = new ManagedObjectRepresentation();
        managedObjectRepresentation.setName("New Name");
        managedObjectRepresentation.setId(mo.getId());
        subscriber.subscribe(MANAGEDOBJECTS + mo.getId().getValue(), new Listener<>());
        // when
        ManagedObjectRepresentation updatedMO = inventoryApi.update(managedObjectRepresentation);
        // then
        await().atMost(TEN_SECONDS).until(() -> receivedNotification);
        assertThat(notification).isExactlyInstanceOf(RealtimeManagedObjectMessage.class);
        assertManagedObjectNotification((RealtimeManagedObjectMessage) notification, updatedMO, "UPDATE");
    }

    @Test
    public void shouldReceiveDeleteMONotification() {
        // given
        Subscriber<String, RealtimeDeleteRepresentationWrapper> subscriber = getSubscriberForType(RealtimeDeleteRepresentationWrapper.class, platform);
        ManagedObjectRepresentation mo = inventoryApi.create(aSampleMo().build());
        subscriber.subscribe(MANAGEDOBJECTS + mo.getId().getValue(), new Listener<>());
        // when
        inventoryApi.delete(mo.getId());
        // then
        await().atMost(TEN_SECONDS).until(() -> receivedNotification);
        assertThat(notification).isExactlyInstanceOf(RealtimeDeleteRepresentationWrapper.class);
        assertDeleteManagedObjectNotification((RealtimeDeleteRepresentationWrapper) notification, mo);
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

    private class Listener<T> implements SubscriptionListener<String, T> {
        @Override
        public void onError(Subscription<String> subscription, Throwable ex) {
            throw new SDKException("Error occurred");
        }

        @Override
        public void onNotification(Subscription<String> subscription, T received) {
            receivedNotification = true;
            notification = received;
        }
    }
}