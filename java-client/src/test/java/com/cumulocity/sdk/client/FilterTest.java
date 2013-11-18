package com.cumulocity.sdk.client;

import static org.fest.assertions.Assertions.assertThat;

import java.util.Map;

import org.junit.Test;

import com.cumulocity.model.DateConverter;
import com.cumulocity.model.event.CumulocityAlarmStatuses;
import com.cumulocity.model.idtype.GId;
import com.cumulocity.sdk.client.alarm.AlarmFilter;
import com.cumulocity.sdk.client.event.EventFilter;

public class FilterTest {

    @Test
    public void shouldCreateParamMap() throws Exception {
        AlarmFilter alarmFilter = new AlarmFilter().bySource(new GId("1")).byStatus(CumulocityAlarmStatuses.ACTIVE);
        
        Map<String, String> queryParams = alarmFilter.getQueryParams();
        
        assertThat(queryParams.keySet()).containsOnly("source","status");
    }
    
    @Test
    public void shouldEncodeParamValue() throws Exception {
        EventFilter eventFilter = new EventFilter().byFromDate(DateConverter.string2Date("2011-11-03T11:01:00.000+05:30"));
        
        Map<String, String> queryParams = eventFilter.getQueryParams();

        assertThat(queryParams.get("dateFrom")).isEqualTo("2011-11-03T06%3A31%3A00.000%2B01%3A00");
    }
}
