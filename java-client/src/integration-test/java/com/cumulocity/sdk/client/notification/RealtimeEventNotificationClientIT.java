package com.cumulocity.sdk.client.notification;

import com.cumulocity.model.idtype.GId;
import com.cumulocity.rest.representation.builder.ManagedObjectRepresentationBuilder;
import com.cumulocity.rest.representation.event.EventRepresentation;
import com.cumulocity.rest.representation.inventory.ManagedObjectRepresentation;
import com.cumulocity.sdk.client.SDKException;
import com.cumulocity.sdk.client.common.JavaSdkITBase;
import com.cumulocity.sdk.client.event.EventApi;
import com.cumulocity.sdk.client.inventory.InventoryApi;
import com.cumulocity.sdk.client.notification.wrappers.RealtimeEventMessage;
import com.cumulocity.sdk.client.notification.wrappers.RealtimeDeleteRepresentationWrapper;
import org.joda.time.DateTime;
import org.junit.After;
import org.junit.Test;

import static com.cumulocity.rest.representation.builder.RestRepresentationObjectMother.anMoRepresentationLike;
import static com.cumulocity.rest.representation.builder.SampleManagedObjectRepresentation.MO_REPRESENTATION;
import static com.cumulocity.sdk.client.common.Subscribers.getSubscriberForType;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.awaitility.Durations.TEN_SECONDS;

public class RealtimeEventNotificationClientIT extends JavaSdkITBase {
    private static final String EVENTS = "/events/";
    private static final String EVENTS_WITH_CHILDREN = "/eventsWithChildren/";

    final InventoryApi inventoryApi = platform.getInventoryApi();
    final EventApi eventApi = platform.getEventApi();
    boolean receivedNotification;
    Object notification;

    @After
    public void setUp() {
        notification = null;
        receivedNotification = false;
    }

    @Test
    public void shouldReceiveCreateEventNotification() {
        // given
        Subscriber<String, RealtimeEventMessage> subscriber = getSubscriberForType(RealtimeEventMessage.class, platform);
        ManagedObjectRepresentation mo = inventoryApi.create(aSampleMo().build());

        subscriber.subscribe(EVENTS + mo.getId().getValue(), new RealtimeEventNotificationClientIT.Listener<>());
        // when
        EventRepresentation event = eventApi.create(createEventRep(mo));
        // then
        await().atMost(TEN_SECONDS).until(() -> receivedNotification);
        assertThat(notification).isExactlyInstanceOf(RealtimeEventMessage.class);
        assertEventNotification((RealtimeEventMessage) notification, event, "CREATE");
    }

    @Test
    public void shouldReceiveUpdateEventNotification() {
        // given
        Subscriber<String, RealtimeEventMessage> subscriber = getSubscriberForType(RealtimeEventMessage.class, platform);
        ManagedObjectRepresentation mo = inventoryApi.create(aSampleMo().build());
        EventRepresentation event = eventApi.create(createEventRep(mo));

        subscriber.subscribe(EVENTS + mo.getId().getValue(), new RealtimeEventNotificationClientIT.Listener<>());
        // when
        EventRepresentation updatedEvent = eventApi.update(updateEventRep(event.getId()));
        // then
        await().atMost(TEN_SECONDS).until(() -> receivedNotification);
        assertThat(notification).isExactlyInstanceOf(RealtimeEventMessage.class);
        assertEventNotification((RealtimeEventMessage) notification, updatedEvent, "UPDATE");
    }

    @Test
    public void shouldReceiveDeleteEventNotification() {
        // given
        Subscriber<String, RealtimeDeleteRepresentationWrapper> subscriber = getSubscriberForType(RealtimeDeleteRepresentationWrapper.class, platform);
        ManagedObjectRepresentation mo = inventoryApi.create(aSampleMo().build());
        EventRepresentation event = eventApi.create(createEventRep(mo));

        subscriber.subscribe(EVENTS + mo.getId().getValue(), new RealtimeEventNotificationClientIT.Listener<>());
        // when
        eventApi.delete(event);
        // then
        await().atMost(TEN_SECONDS).until(() -> receivedNotification);
        assertThat(notification).isExactlyInstanceOf(RealtimeDeleteRepresentationWrapper.class);
        assertDeleteEventNotification((RealtimeDeleteRepresentationWrapper) notification, event);
    }

    @Test
    public void shouldReceiveCreateChildEventNotification() {
        // given
        Subscriber<String, RealtimeEventMessage> subscriber = getSubscriberForType(RealtimeEventMessage.class, platform);
        ManagedObjectRepresentation parentMO = inventoryApi.create(aSampleMo().build());
        ManagedObjectRepresentation childMo = aSampleChildMo(parentMO.getId());

        subscriber.subscribe(EVENTS_WITH_CHILDREN + parentMO.getId().getValue(), new RealtimeEventNotificationClientIT.Listener<>());
        // when
        EventRepresentation event = eventApi.create(createEventRep(childMo));
        // then
        await().atMost(TEN_SECONDS).until(() -> receivedNotification);
        assertThat(notification).isExactlyInstanceOf(RealtimeEventMessage.class);
        assertEventNotification((RealtimeEventMessage) notification, event, "CREATE");
    }

    @Test
    public void shouldReceiveUpdateChildEventNotification() {
        // given
        Subscriber<String, RealtimeEventMessage> subscriber = getSubscriberForType(RealtimeEventMessage.class, platform);
        ManagedObjectRepresentation parentMO = inventoryApi.create(aSampleMo().build());
        ManagedObjectRepresentation childMo = aSampleChildMo(parentMO.getId());
        EventRepresentation event = eventApi.create(createEventRep(childMo));

        subscriber.subscribe(EVENTS_WITH_CHILDREN + parentMO.getId().getValue(), new RealtimeEventNotificationClientIT.Listener<>());
        // when
        EventRepresentation updatedEvent = eventApi.update(updateEventRep(event.getId()));
        // then
        await().atMost(TEN_SECONDS).until(() -> receivedNotification);
        assertThat(notification).isExactlyInstanceOf(RealtimeEventMessage.class);
        assertEventNotification((RealtimeEventMessage) notification, updatedEvent, "UPDATE");
    }

    @Test
    public void shouldReceiveDeleteChildEventNotification() {
        // given
        Subscriber<String, RealtimeDeleteRepresentationWrapper> subscriber = getSubscriberForType(RealtimeDeleteRepresentationWrapper.class, platform);
        ManagedObjectRepresentation parentMO = inventoryApi.create(aSampleMo().build());
        ManagedObjectRepresentation childMo = aSampleChildMo(parentMO.getId());
        EventRepresentation event = eventApi.create(createEventRep(childMo));

        subscriber.subscribe(EVENTS_WITH_CHILDREN + parentMO.getId().getValue(), new RealtimeEventNotificationClientIT.Listener<>());
        // when
        eventApi.delete(event);
        // then
        await().atMost(TEN_SECONDS).until(() -> receivedNotification);
        assertThat(notification).isExactlyInstanceOf(RealtimeDeleteRepresentationWrapper.class);
        assertDeleteEventNotification((RealtimeDeleteRepresentationWrapper) notification, event);
    }

    private static ManagedObjectRepresentationBuilder aSampleMo() {
        return anMoRepresentationLike(MO_REPRESENTATION);
    }

    private ManagedObjectRepresentation aSampleChildMo(GId parentMoId) {
        ManagedObjectRepresentation child1 = inventoryApi.create(aSampleMo().withName("child1").build());
        return inventoryApi.getManagedObjectApi(parentMoId).addChildDevice(child1.getId()).getManagedObject();
    }

    private EventRepresentation createEventRep(ManagedObjectRepresentation source) {
        EventRepresentation eventRepresentation = new EventRepresentation();
        eventRepresentation.setText("Event text");
        eventRepresentation.setSource(source);
        eventRepresentation.setType("Event_type");
        eventRepresentation.setDateTime(new DateTime());
        return eventRepresentation;
    }

    private EventRepresentation updateEventRep(GId eventId) {
        EventRepresentation eventRepresentation = new EventRepresentation();
        eventRepresentation.setId(eventId);
        eventRepresentation.setText("New event text");
        return eventRepresentation;
    }

    private void assertEventNotification(RealtimeEventMessage notification, EventRepresentation source, String realtimeAction) {
        assertThat(notification.getData().getId()).isEqualTo(source.getId());
        assertThat(notification.getData().getType()).isEqualTo(source.getType());
        assertThat(notification.getData().getSource()).isEqualToComparingFieldByField(source.getSource());
        assertThat(notification.getAttrs().get("realtimeAction")).isEqualTo(realtimeAction);
    }

    private void assertDeleteEventNotification(RealtimeDeleteRepresentationWrapper notification, EventRepresentation source) {
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
