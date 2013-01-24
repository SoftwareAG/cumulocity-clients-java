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

import static com.cumulocity.me.rest.representation.RestRepresentationObjectMother.anAlarmRepresentationLike;
import static com.cumulocity.me.rest.representation.RestRepresentationObjectMother.anMoRepresentationLike;
import static com.cumulocity.me.rest.representation.SampleAlarmRepresentation.ALARM_REPRESENTATION;
import static com.cumulocity.me.rest.representation.SampleManagedObjectRepresentation.MO_REPRESENTATION;
import static com.cumulocity.sdk.client.common.SDKExceptionMatcher.sdkException;
import static java.lang.System.currentTimeMillis;
import static org.fest.assertions.Assertions.assertThat;

import java.util.Date;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.cumulocity.me.lang.Iterator;
import com.cumulocity.me.lang.List;
import com.cumulocity.me.model.event.CumulocityAlarmStatuses;
import com.cumulocity.me.rest.representation.alarm.AlarmCollectionRepresentation;
import com.cumulocity.me.rest.representation.alarm.AlarmRepresentation;
import com.cumulocity.me.rest.representation.inventory.ManagedObjectCollectionRepresentation;
import com.cumulocity.me.rest.representation.inventory.ManagedObjectRepresentation;
import com.cumulocity.me.rest.representation.inventory.ManagedObjectRepresentationBuilder;
import com.cumulocity.me.sdk.SDKException;
import com.cumulocity.me.sdk.client.alarm.AlarmApi;
import com.cumulocity.me.sdk.client.alarm.AlarmFilter;
import com.cumulocity.me.sdk.client.inventory.InventoryApi;
import com.cumulocity.sdk.client.common.JavaSdkITBase;

public class AlarmIT extends JavaSdkITBase {

    private static final int UNPROCESSABLE = 422;
    
    @Rule
    public ExpectedException exception = ExpectedException.none();
    
    private ManagedObjectRepresentation source1;
    private ManagedObjectRepresentation source2;

    private InventoryApi inventoryApi;
    private AlarmApi alarmApi;

    @Before
    public void setUp() throws Exception {
        inventoryApi = platform.getInventoryApi();
        alarmApi = platform.getAlarmApi();
        
        source1 = inventoryApi.create(aSampleMo().withName("MO1-" + currentTimeMillis()).build());
    }
    
    @After
    public void tearDown() throws Exception {
        List mosOn1stPage = getMOsFrom1stPage();
        while (!mosOn1stPage.isEmpty()) {
            deleteMOs(mosOn1stPage);
            mosOn1stPage = getMOsFrom1stPage();
        }
    }

    @Test
    public void shouldHaveIdAfterCreateAlarm() throws Exception {
        // Given
        AlarmRepresentation rep = aSampleAlarm(source1);

        // When
        AlarmRepresentation created = alarmApi.create(rep);

        // Then
        assertThat(created.getId()).isNotNull();
    }

    @Test
    public void createAlarmWithoutTime() throws Exception {
        // Given
        AlarmRepresentation alarm = anAlarmRepresentationLike(ALARM_REPRESENTATION).withSource(source1).withTime(null).build();

        // Then
        exception.expect(sdkException(UNPROCESSABLE));

        // When
        alarmApi.create(alarm);
    }

    @Test
    public void createAlarmWithoutText() throws Exception {
        // Given
        AlarmRepresentation alarm = anAlarmRepresentationLike(ALARM_REPRESENTATION).withSource(source1).withText(null).build();

        // Then
        exception.expect(sdkException(UNPROCESSABLE));

        // When
        alarmApi.create(alarm);
    }

    @Test
    public void createAlarmWithoutStatus() throws Exception {
        // Given
        AlarmRepresentation alarm = anAlarmRepresentationLike(ALARM_REPRESENTATION).withSource(source1).withStatus(null).build();

        // Then
        exception.expect(sdkException(UNPROCESSABLE));

        // When
        alarmApi.create(alarm);
    }

    @Test
    public void createAlarmsWithoutSeverity() throws Exception {
        // Given
        AlarmRepresentation alarm = anAlarmRepresentationLike(ALARM_REPRESENTATION).withSource(source1).withSeverity(null).build();

        // Then
        exception.expect(sdkException(UNPROCESSABLE));

        // When
        alarmApi.create(alarm);
    }

    @Test
    public void shouldReturnAllAlarms() throws Exception {
        // Given
        alarmApi.create(aSampleAlarm(source1));
        alarmApi.create(aSampleAlarm(source1));

        // When
        AlarmCollectionRepresentation alarms = (AlarmCollectionRepresentation) alarmApi.getAlarms().get();

        // Then
        assertThat(alarms.getAlarms().size()).isGreaterThanOrEqualTo(2);
    }

    @Test
    public void shouldReturnNoAlarmWithUnmatchedFilter() throws Exception {
        // Given
        source2 = platform.getInventoryApi().create(aSampleMo().withName("MO2-" + currentTimeMillis()).build());
        alarmApi.create(aSampleAlarm(source1));

        // When
        AlarmFilter filter = new AlarmFilter().bySource(source2);
        AlarmCollectionRepresentation bySource = (AlarmCollectionRepresentation) alarmApi.getAlarmsByFilter(filter).get();

        // Then
        List alarms = bySource.getAlarms();
        assertThat(alarms.size()).isEqualTo(0);
    }

    @Test
    public void shouldReturnMultipleAlarmsWithMatchedFilter() throws Exception {
        // Given
        alarmApi.create(aSampleAlarm(source1));
        alarmApi.create(aSampleAlarm(source1));

        // When
        AlarmFilter filter = new AlarmFilter().bySource(source1);
        AlarmCollectionRepresentation bySource = (AlarmCollectionRepresentation) alarmApi.getAlarmsByFilter(filter).get();

        // Then
        List alarms = bySource.getAlarms();
        assertThat(alarms.size()).isEqualTo(2);
    }

    @Test
    public void shouldReturnFilterBySource() throws Exception {
        // Given
        source2 = platform.getInventoryApi().create(aSampleMo().withName("MO2-" + currentTimeMillis()).build());
        alarmApi.create(aSampleAlarm(source1));
        alarmApi.create(aSampleAlarm(source2));

        // When
        AlarmFilter filter = new AlarmFilter().bySource(source1);
        AlarmCollectionRepresentation bySource = (AlarmCollectionRepresentation) alarmApi.getAlarmsByFilter(filter).get();

        // Then
        List alarms = bySource.getAlarms();
        assertThat(alarms.size()).isEqualTo(1);
        assertThat(((AlarmRepresentation)alarms.get(0)).getSource().getId()).isEqualTo(source1.getId());
    }

    @Test
    public void getAlarmCollectionByStatus() throws Exception {
        // Given
        alarmApi.create(anAlarmRepresentationLike(ALARM_REPRESENTATION).withStatus("ACTIVE").withSource(source1).build());

        alarmApi.create(anAlarmRepresentationLike(ALARM_REPRESENTATION).withStatus("ACKNOWLEDGED").withSource(source1).build());

        // When
        AlarmFilter acknowledgedFilter = new AlarmFilter().byStatus(CumulocityAlarmStatuses.ACKNOWLEDGED);
        AlarmCollectionRepresentation acknowledgedAlarms = (AlarmCollectionRepresentation) alarmApi.getAlarmsByFilter(acknowledgedFilter).get();

        // Then
        List alarms = acknowledgedAlarms.getAlarms();
        assertThat(alarms.size()).isGreaterThanOrEqualTo(1);
        assertThat(((AlarmRepresentation)alarms.get(0)).getStatus()).isEqualTo("ACKNOWLEDGED");
    }

    @Test
    public void getAlarmCollectionByStatusAndSource() throws Exception {
        // Given
        source2 = platform.getInventoryApi().create(aSampleMo().withName("MO2-" + currentTimeMillis()).build());
        alarmApi.create(anAlarmRepresentationLike(ALARM_REPRESENTATION).withStatus("ACTIVE").withSource(source1).build());
        alarmApi.create(anAlarmRepresentationLike(ALARM_REPRESENTATION).withStatus("ACKNOWLEDGED").withSource(source1).build());
        alarmApi.create(anAlarmRepresentationLike(ALARM_REPRESENTATION).withStatus("ACKNOWLEDGED").withSource(source2).build());

        // When
        AlarmFilter acknowledgedFilter = new AlarmFilter().byStatus(CumulocityAlarmStatuses.ACKNOWLEDGED).bySource(source1);
        AlarmCollectionRepresentation acknowledgedAlarms = (AlarmCollectionRepresentation) alarmApi.getAlarmsByFilter(acknowledgedFilter).get();

        // Then
        List alarms = acknowledgedAlarms.getAlarms();
        assertThat(alarms.size()).isEqualTo(1);
        assertThat(((AlarmRepresentation)alarms.get(0)).getStatus()).isEqualTo("ACKNOWLEDGED");
        assertThat(((AlarmRepresentation)alarms.get(0)).getSource().getId()).isEqualTo(source1.getId());
    }

    @Test
    public void shouldGetAlarmById() throws Exception {
        // Given
        AlarmRepresentation created = alarmApi.create(anAlarmRepresentationLike(ALARM_REPRESENTATION).withStatus("ACTIVE").withSource(source1)
                .build());

        AlarmRepresentation returned = alarmApi.getAlarm(created.getId());

        assertThat(returned.getStatus()).isEqualTo("ACTIVE");
        assertThat(returned.getSource().getId()).isEqualTo(source1.getId());
    }

    @Test
    public void shouldReturnTheUpdatedAlarm() throws Exception {
        // Given
        AlarmRepresentation created = alarmApi.create(anAlarmRepresentationLike(ALARM_REPRESENTATION).withStatus("ACTIVE").withSource(source1)
                .build());

        // When
        AlarmRepresentation alarm = new AlarmRepresentation();
        alarm.setStatus("ACKNOWLEDGED");
        alarm.setId(created.getId());
        AlarmRepresentation updated = alarmApi.updateAlarm(alarm);

        // Then
        assertThat(updated.getStatus()).isEqualTo("ACKNOWLEDGED");
        assertThat(updated.getSource().getId()).isEqualTo(source1.getId());
    }

    @Test
    public void shouldUpdateAlarm() throws Exception {
        // Given
        AlarmRepresentation created = alarmApi.create(anAlarmRepresentationLike(ALARM_REPRESENTATION).withStatus("ACTIVE").withSource(source1)
                .build());

        // When
        AlarmRepresentation alarm = new AlarmRepresentation();
        alarm.setStatus("ACKNOWLEDGED");
        alarm.setId(created.getId());
        alarmApi.updateAlarm(alarm);

        // Then
        AlarmRepresentation returned = alarmApi.getAlarm(created.getId());

        assertThat(returned.getStatus()).isEqualTo("ACKNOWLEDGED");
        assertThat(returned.getSource().getId()).isEqualTo(source1.getId());
    }

    @Test
    public void getAlarmCollectionByDefaultPageSettings() throws Exception {
        // Given
        for (int i = 0; i < 12; i++) {
            AlarmRepresentation rep = aSampleAlarm(source1);
            alarmApi.create(rep);
        }
        AlarmFilter filter = new AlarmFilter().bySource(source1);

        // When
        AlarmCollectionRepresentation alarms = (AlarmCollectionRepresentation) alarmApi.getAlarmsByFilter(filter).get();

        // Then
        assertThat(alarms.getAlarms().size()).isEqualTo(5);

        // When
        AlarmCollectionRepresentation page1st = (AlarmCollectionRepresentation) alarmApi.getAlarmsByFilter(filter).getPage(alarms, 1);

        // Then
        assertThat(page1st.getAlarms().size()).isEqualTo(5);

        // When
        AlarmCollectionRepresentation page2nd = (AlarmCollectionRepresentation) alarmApi.getAlarmsByFilter(filter).getPage(alarms, 2);

        // Then
        assertThat(page2nd.getAlarms().size()).isEqualTo(5);

        // When
        AlarmCollectionRepresentation page3rd = (AlarmCollectionRepresentation) alarmApi.getAlarmsByFilter(filter).getPage(alarms, 3);

        // Then
        assertThat(page3rd.getAlarms().size()).isEqualTo(2);

        // When
        AlarmCollectionRepresentation page4th = (AlarmCollectionRepresentation) alarmApi.getAlarmsByFilter(filter).getPage(alarms, 4);

        // Then
        assertThat(page4th.getAlarms().size()).isEqualTo(0);
    }

    //
    //    Scenario: Get alarm next and previous collection

    @Test
    public void getAlarmNextAndPreviousCollection() throws Exception {
        // Given
        for (int i = 0; i < 12; i++) {
            alarmApi.create(aSampleAlarm(source1));
        }
        AlarmFilter filter = new AlarmFilter().bySource(source1);

        // When
        AlarmCollectionRepresentation alarms = (AlarmCollectionRepresentation) alarmApi.getAlarmsByFilter(filter).get();

        // Then
        assertThat(alarms.getAlarms().size()).isEqualTo(5);

        // When
        AlarmCollectionRepresentation page2nd = (AlarmCollectionRepresentation) alarmApi.getAlarmsByFilter(filter).getPage(alarms, 2);

        // Then
        assertThat(page2nd.getAlarms().size()).isEqualTo(5);

        // When
        AlarmCollectionRepresentation page3rd = (AlarmCollectionRepresentation) alarmApi.getAlarmsByFilter(filter).getNextPage(page2nd);

        // Then
        assertThat(page3rd.getPageStatistics().getCurrentPage()).isEqualTo(3);
        assertThat(page3rd.getAlarms().size()).isEqualTo(2);

        // When
        AlarmCollectionRepresentation page1st = (AlarmCollectionRepresentation) alarmApi.getAlarmsByFilter(filter).getPreviousPage(page2nd);

        // Then
        assertThat(page1st.getPageStatistics().getCurrentPage()).isEqualTo(1);
        assertThat(page1st.getAlarms().size()).isEqualTo(5);

    }

    private AlarmRepresentation aSampleAlarm(ManagedObjectRepresentation source) {
        return anAlarmRepresentationLike(ALARM_REPRESENTATION)
                .withType("com_nsn_bts_TrxFaulty")
                .withStatus("ACTIVE")
                .withSeverity("major")
                .withSource(source)
                .withText("Alarm for mo")
                .withTime(new Date())
                .build();
    }
    
    private static ManagedObjectRepresentationBuilder aSampleMo() {
        return anMoRepresentationLike(MO_REPRESENTATION);
    }
    
    private List getMOsFrom1stPage() throws SDKException {
        return ((ManagedObjectCollectionRepresentation) inventoryApi.getManagedObjects().get()).getManagedObjects();
    }
    
    private void deleteMOs(List mosOn1stPage) throws SDKException {
        Iterator iterator = mosOn1stPage.iterator();
        while (iterator.hasNext()) {
        ManagedObjectRepresentation mo  = (ManagedObjectRepresentation) iterator.next();
            inventoryApi.getManagedObject(mo.getId()).delete();
        }
    }
}
