package com.cumulocity.sdk.client.notification;

import com.cumulocity.rest.representation.builder.ManagedObjectRepresentationBuilder;
import com.cumulocity.rest.representation.inventory.ManagedObjectRepresentation;
import com.cumulocity.rest.representation.measurement.MeasurementRepresentation;
import com.cumulocity.sdk.client.SDKException;
import com.cumulocity.sdk.client.common.JavaSdkITBase;
import com.cumulocity.sdk.client.inventory.InventoryApi;
import com.cumulocity.sdk.client.measurement.MeasurementApi;
import com.cumulocity.sdk.client.notification.wrappers.RealtimeMeasurementMessage;
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

public class RealtimeMeasurementNotificationClientIT extends JavaSdkITBase {
    private static final String MEASUREMENTS = "/measurements/";

    final InventoryApi inventoryApi = platform.getInventoryApi();
    final MeasurementApi measurementApi = platform.getMeasurementApi();
    boolean receivedNotification;
    Object notification;

    @After
    public void setUp() {
        notification = null;
        receivedNotification = false;
    }

    @Test
    public void shouldReceiveCreateMeasurementNotification() {
        // given
        Subscriber<String, RealtimeMeasurementMessage> subscriber = getSubscriberForType(RealtimeMeasurementMessage.class, platform);
        ManagedObjectRepresentation mo = inventoryApi.create(aSampleMo().build());

        subscriber.subscribe(MEASUREMENTS + mo.getId().getValue(), new RealtimeMeasurementNotificationClientIT.Listener<>());
        // when
        MeasurementRepresentation measurement = measurementApi.create(createMeasurementRep(mo));
        // then
        await().atMost(TEN_SECONDS).until(() -> receivedNotification);
        assertThat(notification).isExactlyInstanceOf(RealtimeMeasurementMessage.class);
        assertMeasurementNotification((RealtimeMeasurementMessage) notification, measurement, "CREATE");
    }

    @Test
    public void shouldReceiveDeleteMeasurementNotification() {
        // given
        Subscriber<String, RealtimeDeleteRepresentationWrapper> subscriber = getSubscriberForType(RealtimeDeleteRepresentationWrapper.class, platform);
        ManagedObjectRepresentation mo = inventoryApi.create(aSampleMo().build());
        MeasurementRepresentation measurement = measurementApi.create(createMeasurementRep(mo));

        subscriber.subscribe(MEASUREMENTS + mo.getId().getValue(), new RealtimeMeasurementNotificationClientIT.Listener<>());
        // when
        measurementApi.delete(measurement);
        // then
        await().atMost(TEN_SECONDS).until(() -> receivedNotification);
        assertThat(notification).isExactlyInstanceOf(RealtimeDeleteRepresentationWrapper.class);
        assertDeleteMeasurementNotification((RealtimeDeleteRepresentationWrapper) notification, measurement);
    }

    private static ManagedObjectRepresentationBuilder aSampleMo() {
        return anMoRepresentationLike(MO_REPRESENTATION);
    }

    private MeasurementRepresentation createMeasurementRep(ManagedObjectRepresentation source) {
        MeasurementRepresentation rep = new MeasurementRepresentation();
        rep.setType("com.type1");
        rep.setDateTime(new DateTime());
        rep.setSource(source);
        return rep;
    }

    private void assertMeasurementNotification(RealtimeMeasurementMessage notification, MeasurementRepresentation source, String realtimeAction) {
        assertThat(notification.getData().getId()).isEqualTo(source.getId());
        assertThat(notification.getData().getType()).isEqualTo(source.getType());
        assertThat(notification.getData().getSource()).isEqualToComparingFieldByField(source.getSource());
        assertThat(notification.getAttrs().get("realtimeAction")).isEqualTo(realtimeAction);
    }

    private void assertDeleteMeasurementNotification(RealtimeDeleteRepresentationWrapper notification, MeasurementRepresentation source) {
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
