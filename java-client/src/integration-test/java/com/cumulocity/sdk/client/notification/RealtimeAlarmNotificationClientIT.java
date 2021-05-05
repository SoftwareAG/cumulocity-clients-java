package com.cumulocity.sdk.client.notification;

import com.cumulocity.model.idtype.GId;
import com.cumulocity.rest.representation.alarm.AlarmRepresentation;
import com.cumulocity.rest.representation.builder.ManagedObjectRepresentationBuilder;
import com.cumulocity.rest.representation.inventory.ManagedObjectRepresentation;
import com.cumulocity.sdk.client.SDKException;
import com.cumulocity.sdk.client.alarm.AlarmApi;
import com.cumulocity.sdk.client.common.JavaSdkITBase;
import com.cumulocity.sdk.client.inventory.InventoryApi;
import com.cumulocity.sdk.client.notification.wrappers.RealtimeAlarmMessage;
import org.joda.time.DateTime;
import org.junit.After;
import org.junit.Ignore;
import org.junit.Test;

import static com.cumulocity.rest.representation.builder.RestRepresentationObjectMother.anAlarmRepresentationLike;
import static com.cumulocity.rest.representation.builder.RestRepresentationObjectMother.anMoRepresentationLike;
import static com.cumulocity.rest.representation.builder.SampleAlarmRepresentation.ALARM_REPRESENTATION;
import static com.cumulocity.rest.representation.builder.SampleManagedObjectRepresentation.MO_REPRESENTATION;
import static com.cumulocity.sdk.client.common.Subscribers.getSubscriberForType;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.awaitility.Durations.TEN_SECONDS;

public class RealtimeAlarmNotificationClientIT extends JavaSdkITBase {
    private static final String ALARMS = "/alarms/";
    private static final String ALARMS_WITH_CHILDREN = "/alarmsWithChildren/";

    final InventoryApi inventoryApi = platform.getInventoryApi();
    final AlarmApi alarmApi = platform.getAlarmApi();
    boolean receivedNotification;
    Object notification;

    @After
    public void setUp() {
        notification = null;
        receivedNotification = false;
    }

    @Test
    public void shouldReceiveCreateAlarmNotification() {
        // given
        Subscriber<String, RealtimeAlarmMessage> subscriber = getSubscriberForType(RealtimeAlarmMessage.class, platform);
        ManagedObjectRepresentation mo = inventoryApi.create(aSampleMo().build());

        subscriber.subscribe(ALARMS + mo.getId().getValue(), new RealtimeAlarmNotificationClientIT.Listener<>());
        // when
        AlarmRepresentation alarm = alarmApi.create(createAlarmRep(mo));
        // then
        await().atMost(TEN_SECONDS).until(() -> receivedNotification);
        assertThat(notification).isExactlyInstanceOf(RealtimeAlarmMessage.class);
        assertAlarmNotification((RealtimeAlarmMessage) notification, alarm, "CREATE");
    }

    // Test is failing because source fields in received notification are not the same as in updated alarm
    // https://cumulocity.atlassian.net/browse/MTM-38784
    @Ignore
    @Test
    public void shouldReceiveUpdateAlarmNotification() {
        // given
        Subscriber<String, RealtimeAlarmMessage> subscriber = getSubscriberForType(RealtimeAlarmMessage.class, platform);
        ManagedObjectRepresentation mo = inventoryApi.create(aSampleMo().build());
        AlarmRepresentation alarm = alarmApi.create(createAlarmRep(mo));

        subscriber.subscribe(ALARMS + mo.getId().getValue(), new RealtimeAlarmNotificationClientIT.Listener<>());
        // when
        AlarmRepresentation updatedAlarm = alarmApi.update(updateAlarmRep(alarm.getId()));
        // then
        await().atMost(TEN_SECONDS).until(() -> receivedNotification);
        assertThat(notification).isExactlyInstanceOf(RealtimeAlarmMessage.class);
        assertAlarmNotification((RealtimeAlarmMessage) notification, updatedAlarm, "UPDATE");
    }

    @Test
    public void shouldReceiveCreateChildAlarmNotification() {
        // given
        Subscriber<String, RealtimeAlarmMessage> subscriber = getSubscriberForType(RealtimeAlarmMessage.class, platform);
        ManagedObjectRepresentation parentMO = inventoryApi.create(aSampleMo().build());
        ManagedObjectRepresentation childMo = aSampleChildMo(parentMO.getId());

        subscriber.subscribe(ALARMS_WITH_CHILDREN + parentMO.getId().getValue(), new RealtimeAlarmNotificationClientIT.Listener<>());
        // when
        AlarmRepresentation alarm = alarmApi.create(createAlarmRep(childMo));
        // then
        await().atMost(TEN_SECONDS).until(() -> receivedNotification);
        assertThat(notification).isExactlyInstanceOf(RealtimeAlarmMessage.class);
        assertAlarmNotification((RealtimeAlarmMessage) notification, alarm, "CREATE");
    }

    // Test is failing because source fields in received notification are not the same as in updated alarm
    // https://cumulocity.atlassian.net/browse/MTM-38784
    @Ignore
    @Test
    public void shouldReceiveUpdateChildAlarmNotification() {
        // given
        Subscriber<String, RealtimeAlarmMessage> subscriber = getSubscriberForType(RealtimeAlarmMessage.class, platform);
        ManagedObjectRepresentation parentMO = inventoryApi.create(aSampleMo().build());
        ManagedObjectRepresentation childMo = aSampleChildMo(parentMO.getId());
        AlarmRepresentation alarm = alarmApi.create(createAlarmRep(childMo));

        subscriber.subscribe(ALARMS_WITH_CHILDREN + parentMO.getId().getValue(), new RealtimeAlarmNotificationClientIT.Listener<>());
        // when
        AlarmRepresentation updatedAlarm = alarmApi.update(updateAlarmRep(alarm.getId()));
        // then
        await().atMost(TEN_SECONDS).until(() -> receivedNotification);
        assertThat(notification).isExactlyInstanceOf(RealtimeAlarmMessage.class);
        assertAlarmNotification((RealtimeAlarmMessage) notification, updatedAlarm, "UPDATE");
    }

    private static ManagedObjectRepresentationBuilder aSampleMo() {
        return anMoRepresentationLike(MO_REPRESENTATION);
    }

    private ManagedObjectRepresentation aSampleChildMo(GId parentMoId) {
        ManagedObjectRepresentation child1 = inventoryApi.create(aSampleMo().withName("child1").build());
        return inventoryApi.getManagedObjectApi(parentMoId).addChildDevice(child1.getId()).getManagedObject();
    }

    private AlarmRepresentation createAlarmRep(ManagedObjectRepresentation source) {
        return anAlarmRepresentationLike(ALARM_REPRESENTATION)
                .withType("com_nsn_bts_TrxFaulty")
                .withStatus("ACTIVE")
                .withSeverity("major")
                .withSource(source)
                .withText("Alarm for mo")
                .withDateTime(new DateTime()).build();
    }

    private AlarmRepresentation updateAlarmRep(GId alarmId) {
        AlarmRepresentation alarmRepresentation = new AlarmRepresentation();
        alarmRepresentation.setId(alarmId);
        alarmRepresentation.setText("New alarm text");
        alarmRepresentation.setSeverity("minor");
        return alarmRepresentation;
    }

    private void assertAlarmNotification(RealtimeAlarmMessage notification, AlarmRepresentation source, String realtimeAction) {
        assertThat(notification.getData().getId()).isEqualTo(source.getId());
        assertThat(notification.getData().getType()).isEqualTo(source.getType());
        assertThat(notification.getData().getSource()).isEqualToComparingFieldByField(source.getSource());
        assertThat(notification.getAttrs().get("realtimeAction")).isEqualTo(realtimeAction);
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
