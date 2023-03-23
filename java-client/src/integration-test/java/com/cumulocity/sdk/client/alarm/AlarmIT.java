/*
 * Copyright (C) 2013 Cumulocity GmbH
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"),
 * to deal in the Software without restriction, including without limitation the rights to use,
 * copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software,
 * and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
 * OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE,
 * ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.cumulocity.sdk.client.alarm;

import com.cumulocity.model.event.CumulocityAlarmStatuses;
import com.cumulocity.rest.representation.alarm.AlarmCollectionRepresentation;
import com.cumulocity.rest.representation.alarm.AlarmRepresentation;
import com.cumulocity.rest.representation.builder.ManagedObjectRepresentationBuilder;
import com.cumulocity.rest.representation.inventory.ManagedObjectRepresentation;
import com.cumulocity.sdk.client.common.JavaSdkITBase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.cumulocity.model.DateConverter.string2Date;
import static com.cumulocity.model.util.DateTimeUtils.nowDateTimeLocal;
import static com.cumulocity.rest.representation.builder.RestRepresentationObjectMother.anAlarmRepresentationLike;
import static com.cumulocity.rest.representation.builder.RestRepresentationObjectMother.anMoRepresentationLike;
import static com.cumulocity.rest.representation.builder.SampleAlarmRepresentation.ALARM_REPRESENTATION;
import static com.cumulocity.rest.representation.builder.SampleManagedObjectRepresentation.MO_REPRESENTATION;
import static com.cumulocity.sdk.client.common.SdkExceptionMatcher.sdkException;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.api.HamcrestCondition.matching;

public class AlarmIT extends JavaSdkITBase {

    private static final int UNPROCESSABLE = 422;

    private AlarmApi alarmApi;

    private int t = 0;

    private ManagedObjectRepresentation mo1;
    private ManagedObjectRepresentation mo2;
    private ManagedObjectRepresentation mo3;

    @BeforeEach
    public void setup() throws Exception {
        alarmApi = platform.getAlarmApi();

        mo1 = platform.getInventoryApi().create(aSampleMo().withName("MO" + 1).build());
        mo2 = platform.getInventoryApi().create(aSampleMo().withName("MO" + 2).build());
        mo3 = platform.getInventoryApi().create(aSampleMo().withName("MO" + 3).build());
    }

    private static ManagedObjectRepresentationBuilder aSampleMo() {
        return anMoRepresentationLike(MO_REPRESENTATION);
    }

    @Test
    public void shouldHaveIdAfterCreateAlarm() {
        // Given
        AlarmRepresentation rep = aSampleAlarm(mo1);

        // When
        AlarmRepresentation created = alarmApi.create(rep);

        // Then
        assertThat(created.getId()).isNotNull();
    }

    @Test
    public void createAlarmWithoutTime() {
        // Given
        AlarmRepresentation alarm = anAlarmRepresentationLike(ALARM_REPRESENTATION)
                .withSource(mo1).withDateTime(null).build();

        // Then
        Throwable thrown = catchThrowable(() -> alarmApi.create(alarm));

        // When
        assertThat(thrown).is(matching(sdkException(UNPROCESSABLE)));
    }

    @Test
    public void createAlarmWithoutText() {
        // Given
        AlarmRepresentation alarm = anAlarmRepresentationLike(ALARM_REPRESENTATION)
                .withSource(mo1).withText(null).build();

        // Then
        Throwable thrown = catchThrowable(() -> alarmApi.create(alarm));

        // When
        assertThat(thrown).is(matching(sdkException(UNPROCESSABLE)));
    }

    @Test
    public void createAlarmsWithoutSeverity() {
        // Given
        AlarmRepresentation alarm = anAlarmRepresentationLike(ALARM_REPRESENTATION)
                .withSource(mo1).withSeverity(null).build();

        // Then
        Throwable thrown = catchThrowable(() -> alarmApi.create(alarm));

        // When
        assertThat(thrown).is(matching(sdkException(UNPROCESSABLE)));
    }

    @Test
    public void shouldReturnAllCreatedAlarms() {
        // Given
        ManagedObjectRepresentation source = mo1;

        for (int i = 0; i<10 ; i++) {
            alarmApi.create(aSampleAlarm(source));
        }

        int resultNumber = 0;
        Iterable<AlarmRepresentation> pager = alarmApi.getAlarmsByFilter(new AlarmFilter().bySource(source.getId())).get().allPages();
        for (AlarmRepresentation alarm : pager) {
            resultNumber++;
        }

        assertThat(resultNumber).isEqualTo(10);
    }

    @Test
    public void shouldReturnAllCreatedAsyncAlarms() {
        // Given
        ManagedObjectRepresentation source = mo1;

        for (int i = 0; i<10 ; i++) {
            alarmApi.createAsync(aSampleAlarm(source)).get();
        }

        int resultNumber = 0;
        Iterable<AlarmRepresentation> pager = alarmApi.getAlarmsByFilter(new AlarmFilter().bySource(source.getId())).get().allPages();
        for (AlarmRepresentation alarm : pager) {
            resultNumber++;
        }

        assertThat(resultNumber).isEqualTo(10);
    }

    @Test
    public void shouldReturnNoAlarmWithUnmatchedFilter() {
        // Given
        alarmApi.create(aSampleAlarm(mo1));

        // When
        AlarmFilter filter = new AlarmFilter().bySource(mo3);
        AlarmCollectionRepresentation bySource = alarmApi.getAlarmsByFilter(filter).get();

        // Then
        List<AlarmRepresentation> alarms = bySource.getAlarms();
        assertThat(alarms).hasSize(0);
    }

    @Test
    public void shouldReturnMultipleAlarmsWithMatchedFilter() {
        // Given
        alarmApi.create(aSampleAlarm(mo1));
        alarmApi.create(aSampleAlarm(mo1));

        // When
        AlarmFilter filter = new AlarmFilter().bySource(mo1);
        AlarmCollectionRepresentation bySource = alarmApi.getAlarmsByFilter(filter).get();

        // Then
        List<AlarmRepresentation> alarms = bySource.getAlarms();
        assertThat(alarms).hasSize(2);
    }

    @Test
    public void shouldReturnFilterBySource() {
        // Given
        alarmApi.create(aSampleAlarm(mo1));
        alarmApi.create(aSampleAlarm(mo2));

        // When
        AlarmFilter filter = new AlarmFilter().bySource(mo1);
        AlarmCollectionRepresentation bySource = alarmApi.getAlarmsByFilter(filter).get();

        // Then
        List<AlarmRepresentation> alarms = bySource.getAlarms();
        assertThat(alarms).hasSize(1);
        assertThat(alarms.get(0).getSource().getId()).isEqualTo(mo1.getId());
    }

    @Test
    public void getAlarmCollectionByStatus() {
        // Given
        alarmApi.create(anAlarmRepresentationLike(ALARM_REPRESENTATION)
                .withType("com_nsn_bts_TrxFaulty" + t++)
                .withStatus("ACTIVE")
                .withSource(mo1).build());

        alarmApi.create(anAlarmRepresentationLike(ALARM_REPRESENTATION)
                .withType("com_nsn_bts_TrxFaulty" + t++)
                .withStatus("ACKNOWLEDGED")
                .withSource(mo1).build());

        // When
        AlarmFilter acknowledgedFilter = new AlarmFilter().byStatus(CumulocityAlarmStatuses.valueOf("ACKNOWLEDGED"));

        // Then
        for (AlarmRepresentation result : alarmApi.getAlarmsByFilter(acknowledgedFilter).get().allPages()) {
            assertThat(result.getStatus()).isEqualTo("ACKNOWLEDGED");
        }
    }

    @Test
    public void getAlarmCollectionByStatusAndSource() {
        // Given
        alarmApi.create(anAlarmRepresentationLike(ALARM_REPRESENTATION)
                .withType("com_nsn_bts_TrxFaulty" + t++)
                .withStatus("CLEARED")
                .withSource(mo1).build());

        alarmApi.create(anAlarmRepresentationLike(ALARM_REPRESENTATION)
                .withType("com_nsn_bts_TrxFaulty" + t++)
                .withStatus("ACKNOWLEDGED")
                .withSource(mo1).build());

        alarmApi.create(anAlarmRepresentationLike(ALARM_REPRESENTATION)
                .withType("com_nsn_bts_TrxFaulty" + t++)
                .withStatus("CUSTOM")
                .withSource(mo2).build());

        // When
        AlarmFilter acknowledgedFilter =
                new AlarmFilter().byStatus(CumulocityAlarmStatuses.valueOf("ACKNOWLEDGED")).bySource(mo1);
        AlarmCollectionRepresentation acknowledgedAlarms = alarmApi.getAlarmsByFilter(acknowledgedFilter).get();

        // Then
        List<AlarmRepresentation> alarms = acknowledgedAlarms.getAlarms();
        assertThat(alarms).hasSize(1);
        assertThat(alarms.get(0).getStatus()).isEqualTo("ACKNOWLEDGED");
        assertThat(alarms.get(0).getSource().getId()).isEqualTo(mo1.getId());
    }

    @Test
    public void shouldGetAlarmById() {
        // Given
        AlarmRepresentation created = alarmApi.create(anAlarmRepresentationLike(ALARM_REPRESENTATION)
                .withStatus("ACTIVE")
                .withSource(mo1).build());

        AlarmRepresentation returned = alarmApi.getAlarm(created.getId());

        assertThat(returned.getStatus()).isEqualTo("ACTIVE");
        assertThat(returned.getSource().getId()).isEqualTo(mo1.getId());
    }

    @Test
    public void shouldReturnTheUpdatedAlarm() {
        // Given
        AlarmRepresentation created = alarmApi.create(anAlarmRepresentationLike(ALARM_REPRESENTATION)
                .withStatus("ACTIVE")
                .withSource(mo1).build());

        // When
        AlarmRepresentation alarm = new AlarmRepresentation();
        alarm.setStatus("ACKNOWLEDGED");
        alarm.setId(created.getId());
        AlarmRepresentation updated = alarmApi.updateAlarm(alarm);

        // Then
        assertThat(updated.getStatus()).isEqualTo("ACKNOWLEDGED");
        assertThat(updated.getSource().getId()).isEqualTo(mo1.getId());
    }

    @Test
    public void shouldUpdateAlarm() {
        // Given
        AlarmRepresentation created = alarmApi.create(anAlarmRepresentationLike(ALARM_REPRESENTATION)
                .withStatus("ACTIVE")
                .withSource(mo1).build());

        // When
        AlarmRepresentation alarm = new AlarmRepresentation();
        alarm.setStatus("ACKNOWLEDGED");
        alarm.setId(created.getId());
        alarmApi.updateAlarm(alarm);

        // Then
        AlarmRepresentation returned = alarmApi.getAlarm(created.getId());

        assertThat(returned.getStatus()).isEqualTo("ACKNOWLEDGED");
        assertThat(returned.getSource().getId()).isEqualTo(mo1.getId());
    }

    @Test
    public void shouldDeleteAlarmCollectionByEmptyFilter() throws Exception {
        // Given
        for (int i = 0; i<5 ; i++) {
            alarmApi.create(aSampleAlarm(mo1));
            alarmApi.create(aSampleAlarm(mo2));
            alarmApi.create(aSampleAlarm(mo3));
        }

        AlarmFilter requiredFilter = new AlarmFilter().byFromDate(string2Date("2000-01-01T00:00:00.000+00:00"));

        //When
        alarmApi.deleteAlarmsByFilter(requiredFilter);

        //wait as bulk delete is asynchronous
        Thread.sleep(5000);

        //Then
        int resultNumber = 0;
        Iterable<AlarmRepresentation> pager = alarmApi.getAlarms().get().allPages();
        for (AlarmRepresentation alarm : pager) {
            resultNumber++;
        }

        assertThat(resultNumber).isEqualTo(0);
    }

    @Test
    public void shouldDeleteByFilterStatus() throws Exception {
        // Given
        alarmApi.create(anAlarmRepresentationLike(ALARM_REPRESENTATION)
                .withType("com_nsn_bts_TrxFaulty" + t++)
                .withStatus("ACTIVE")
                .withSource(mo1).build());

        alarmApi.create(anAlarmRepresentationLike(ALARM_REPRESENTATION)
                .withType("com_nsn_bts_TrxFaulty" + t++)
                .withStatus("ACKNOWLEDGED")
                .withSource(mo1).build());

        alarmApi.create(anAlarmRepresentationLike(ALARM_REPRESENTATION)
                .withType("com_nsn_bts_TrxFaulty" + t++)
                .withStatus("ACKNOWLEDGED")
                .withSource(mo2).build());

        alarmApi.create(anAlarmRepresentationLike(ALARM_REPRESENTATION)
                .withType("com_nsn_bts_TrxFaulty" + t++)
                .withStatus("CLEARED")
                .withSource(mo2).build());

        AlarmFilter alarmFilter = new AlarmFilter()
                .byFromDate(string2Date("2000-01-01T00:00:00.000+00:00"))
                .byStatus(CumulocityAlarmStatuses.valueOf("ACKNOWLEDGED"));

        // When
        alarmApi.deleteAlarmsByFilter(alarmFilter);

        //wait as bulk delete is asynchronous
        Thread.sleep(5000);


        // Then
        List<AlarmRepresentation> allAlarms = alarmApi.getAlarmsByFilter(new AlarmFilter().bySource(mo1)).get().getAlarms();
        allAlarms.addAll(alarmApi.getAlarmsByFilter(new AlarmFilter().bySource(mo2)).get().getAlarms());

        assertThat(allAlarms).hasSize(2);

        for (AlarmRepresentation alarm : allAlarms) {
            assertThat(alarm.getStatus()).isIn("ACTIVE", "CLEARED");
        }
    }

    private AlarmRepresentation aSampleAlarm(ManagedObjectRepresentation source) {
        return anAlarmRepresentationLike(ALARM_REPRESENTATION)
                .withType("com_nsn_bts_TrxFaulty" + t++)
                .withStatus("ACTIVE")
                .withSeverity("major")
                .withSource(source)
                .withText("Alarm for mo")
                .withDateTime(nowDateTimeLocal()).build();
    }
}
