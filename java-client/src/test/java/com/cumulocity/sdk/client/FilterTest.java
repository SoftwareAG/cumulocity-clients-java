package com.cumulocity.sdk.client;

import static org.fest.assertions.Assertions.assertThat;

import java.util.Map;

import org.junit.Test;

import com.cumulocity.model.event.CumulocityAlarmStatuses;
import com.cumulocity.model.idtype.GId;
import com.cumulocity.sdk.client.alarm.AlarmFilter;

public class FilterTest {

    @Test
    public void shouldCreateParamMap() throws Exception {
        AlarmFilter alarmFilter = new AlarmFilter().bySource(new GId("1")).byStatus(CumulocityAlarmStatuses.ACTIVE);
        
        Map<String, String> queryParams = alarmFilter.getQueryParams();
        
        assertThat(queryParams.keySet()).containsOnly("source","status");
    }
}
