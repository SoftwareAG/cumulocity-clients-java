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

import static com.cumulocity.rest.representation.builder.RestRepresentationObjectMother.anAlarmRepresentationLike;
import static com.cumulocity.rest.representation.builder.RestRepresentationObjectMother.anMoRepresentationLike;
import static com.cumulocity.rest.representation.builder.SampleAlarmRepresentation.ALARM_REPRESENTATION;
import static com.cumulocity.rest.representation.builder.SampleManagedObjectRepresentation.MO_REPRESENTATION;
import static com.cumulocity.sdk.client.common.SdkExceptionMatcher.sdkException;
import static com.cumulocity.sdk.client.inventory.InventoryParam.withoutChildrenNames;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

import java.util.Date;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.cumulocity.model.event.CumulocityAlarmStatuses;
import com.cumulocity.rest.representation.alarm.AlarmCollectionRepresentation;
import com.cumulocity.rest.representation.alarm.AlarmRepresentation;
import com.cumulocity.rest.representation.builder.ManagedObjectRepresentationBuilder;
import com.cumulocity.rest.representation.inventory.ManagedObjectRepresentation;
import com.cumulocity.sdk.client.SDKException;
import com.cumulocity.sdk.client.common.JavaSdkITBase;

public class AlarmIT extends JavaSdkITBase {

    private static final int UNPROCESSABLE = 422;

    private AlarmApi alarmApi;
    
    private int t = 0;

    @Rule
    public ExpectedException exception = ExpectedException.none();
    private ManagedObjectRepresentation mo1;
    private ManagedObjectRepresentation mo2;
    private ManagedObjectRepresentation mo3;

    @Before
    public void setup() throws Exception {
        alarmApi = platform.getAlarmApi();

        mo1 = platform.getInventoryApi().create(aSampleMo().withName("MO" + 1).build());
        mo2 = platform.getInventoryApi().create(aSampleMo().withName("MO" + 2).build());
        mo3 = platform.getInventoryApi().create(aSampleMo().withName("MO" + 3).build());
    }

    @After
    public void deleteManagedObjects() throws Exception {
        List<ManagedObjectRepresentation> mosOn1stPage = getMOsFrom1stPage();
        while (!mosOn1stPage.isEmpty()) {
            deleteMOs(mosOn1stPage);
            mosOn1stPage = getMOsFrom1stPage();
        }
    }
    
    private void deleteMOs(List<ManagedObjectRepresentation> mosOn1stPage) throws SDKException {
        for (ManagedObjectRepresentation mo : mosOn1stPage) {
            platform.getInventoryApi().getManagedObject(mo.getId()).delete();
        }
    }

    private List<ManagedObjectRepresentation> getMOsFrom1stPage() throws SDKException {
        return platform.getInventoryApi().getManagedObjects().get().getManagedObjects();
    }

    private static ManagedObjectRepresentationBuilder aSampleMo() {
        return anMoRepresentationLike(MO_REPRESENTATION);
    }

    @Test
    public void shouldHaveIdAfterCreateAlarm() throws Exception {
        // Given
        AlarmRepresentation rep = aSampleAlarm(mo1);

        // When
        AlarmRepresentation created = alarmApi.create(rep);

        // Then
        assertThat(created.getId(), is(notNullValue()));
    }

    @Test
    public void createAlarmWithoutTime() throws Exception {
        // Given
        AlarmRepresentation alarm = anAlarmRepresentationLike(ALARM_REPRESENTATION)
                .withSource(mo1).withTime(null).build();

        // Then
        exception.expect(sdkException(UNPROCESSABLE));

        // When
        alarmApi.create(alarm);
    }

    @Test
    public void createAlarmWithoutText() throws Exception {
        // Given
        AlarmRepresentation alarm = anAlarmRepresentationLike(ALARM_REPRESENTATION)
                .withSource(mo1).withText(null).build();

        // Then
        exception.expect(sdkException(UNPROCESSABLE));

        // When
        alarmApi.create(alarm);
    }

    @Test
    public void createAlarmWithoutStatus() throws Exception {
        // Given
        AlarmRepresentation alarm = anAlarmRepresentationLike(ALARM_REPRESENTATION)
                .withSource(mo1).withStatus(null).build();

        // Then
        exception.expect(sdkException(UNPROCESSABLE));

        // When
        alarmApi.create(alarm);
    }

    @Test
    public void createAlarmsWithoutSeverity() throws Exception {
        // Given
        AlarmRepresentation alarm = anAlarmRepresentationLike(ALARM_REPRESENTATION)
                .withSource(mo1).withSeverity(null).build();

        // Then
        exception.expect(sdkException(UNPROCESSABLE));

        // When
        alarmApi.create(alarm);
    }

    @Test
    public void shouldReturnAllAlarms() throws Exception {
        // Given
        ManagedObjectRepresentation source = mo1;
        alarmApi.create(aSampleAlarm(source));
        alarmApi.create(aSampleAlarm(source));

        // When
        AlarmCollectionRepresentation alarms = alarmApi.getAlarms().get();

        // Then
        assertThat(alarms.getAlarms().size(), is(2));
    }
    
    @Test
    public void shouldReturnAllAlarms2() throws Exception {
        // Given
        ManagedObjectRepresentation source = mo1;
        
        for (int i = 0; i<20 ; i++) {
            alarmApi.create(aSampleAlarm(source));
        }

        int resultNumber = 0;
        Iterable<AlarmRepresentation> pager = alarmApi.getAlarmsByFilter(new AlarmFilter().bySource(source.getId())).get().allPages();
        for (AlarmRepresentation alarm : pager) {
            System.out.println(resultNumber);
            resultNumber++;
        }

        assertThat(resultNumber, is(20));
    }
    
    @Test
    public void shouldReturnNoAlarmWithUnmatchedFilter() throws Exception {
        // Given
        alarmApi.create(aSampleAlarm(mo1));

        // When
        AlarmFilter filter = new AlarmFilter().bySource(mo3);
        AlarmCollectionRepresentation bySource = alarmApi.getAlarmsByFilter(filter).get();

        // Then
        List<AlarmRepresentation> alarms = bySource.getAlarms();
        assertThat(alarms.size(), is(equalTo(0)));
    }

    @Test
    public void shouldReturnMultipleAlarmsWithMatchedFilter() throws Exception {
        // Given
        alarmApi.create(aSampleAlarm(mo1));
        alarmApi.create(aSampleAlarm(mo1));

        // When
        AlarmFilter filter = new AlarmFilter().bySource(mo1);
        AlarmCollectionRepresentation bySource = alarmApi.getAlarmsByFilter(filter).get();

        // Then
        List<AlarmRepresentation> alarms = bySource.getAlarms();
        assertThat(alarms.size(), is(equalTo(2)));
    }

    @Test
    public void shouldReturnFilterBySource() throws Exception {
        // Given
        alarmApi.create(aSampleAlarm(mo1));
        alarmApi.create(aSampleAlarm(mo2));

        // When
        AlarmFilter filter = new AlarmFilter().bySource(mo1);
        AlarmCollectionRepresentation bySource = alarmApi.getAlarmsByFilter(filter).get();

        // Then
        List<AlarmRepresentation> alarms = bySource.getAlarms();
        assertThat(alarms.size(), is(equalTo(2)));
        assertThat(alarms.get(0).getSource().getId(), is(mo1.getId()));
    }

    @Test
    public void getAlarmCollectionByStatus() throws Exception {
        // Given
        alarmApi.create(anAlarmRepresentationLike(ALARM_REPRESENTATION)
                .withStatus("ACTIVE")
                .withSource(mo1).build());

        alarmApi.create(anAlarmRepresentationLike(ALARM_REPRESENTATION)
                .withStatus("ACKNOWLEDGED")
                .withSource(mo1).build());

        // When
        AlarmFilter acknowledgedFilter = new AlarmFilter().byStatus(CumulocityAlarmStatuses.valueOf("ACKNOWLEDGED"));
        AlarmCollectionRepresentation acknowledgedAlarms = alarmApi.getAlarmsByFilter(acknowledgedFilter).get();

        // Then
        List<AlarmRepresentation> alarms = acknowledgedAlarms.getAlarms();
        assertThat(alarms.size(), is(equalTo(1)));
        assertThat(alarms.get(0).getStatus(), is("ACKNOWLEDGED"));
    }

    @Test
    public void getAlarmCollectionByStatusAndSource() throws Exception {
        // Given
        alarmApi.create(anAlarmRepresentationLike(ALARM_REPRESENTATION)
                .withStatus("ACTIVE")
                .withSource(mo1).build());

        alarmApi.create(anAlarmRepresentationLike(ALARM_REPRESENTATION)
                .withStatus("ACKNOWLEDGED")
                .withSource(mo1).build());

        alarmApi.create(anAlarmRepresentationLike(ALARM_REPRESENTATION)
                .withStatus("ACKNOWLEDGED")
                .withSource(mo2).build());

        // When
        AlarmFilter acknowledgedFilter = new AlarmFilter().byStatus(CumulocityAlarmStatuses.valueOf("ACKNOWLEDGED")).bySource(mo1);
        AlarmCollectionRepresentation acknowledgedAlarms = alarmApi.getAlarmsByFilter(acknowledgedFilter).get();

        // Then
        List<AlarmRepresentation> alarms = acknowledgedAlarms.getAlarms();
        assertThat(alarms.size(), is(equalTo(1)));
        assertThat(alarms.get(0).getStatus(), is("ACKNOWLEDGED"));
        assertThat(alarms.get(0).getSource().getId(), is(mo1.getId()));
    }

    @Test
    public void shouldGetAlarmById() throws Exception {
        // Given
        AlarmRepresentation created = alarmApi.create(anAlarmRepresentationLike(ALARM_REPRESENTATION)
                .withStatus("ACTIVE")
                .withSource(mo1).build());

        AlarmRepresentation returned = alarmApi.getAlarm(created.getId());

        assertThat(returned.getStatus(), is("ACTIVE"));
        assertThat(returned.getSource().getId(), is(mo1.getId()));
    }

    @Test
    public void shouldReturnTheUpdatedAlarm() throws Exception {
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
        assertThat(updated.getStatus(), is("ACKNOWLEDGED"));
        assertThat(updated.getSource().getId(), is(mo1.getId()));
    }

    @Test
    public void shouldUpdateAlarm() throws Exception {
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

        assertThat(returned.getStatus(), is("ACKNOWLEDGED"));
        assertThat(returned.getSource().getId(), is(mo1.getId()));
    }

    private AlarmRepresentation aSampleAlarm(ManagedObjectRepresentation source) {
        return anAlarmRepresentationLike(ALARM_REPRESENTATION)
                .withType("com_nsn_bts_TrxFaulty" + t++)
                .withStatus("ACTIVE")
                .withSeverity("major")
                .withSource(source)
                .withText("Alarm for mo")
                .withTime(new Date()).build();
    }
}
