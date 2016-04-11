package com.cumulocity.sdk.client;

import static org.fest.assertions.Assertions.assertThat;

import java.util.Map;

import org.junit.Test;

import com.cumulocity.model.DateTimeConverter;
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
        EventFilter eventFilter = new EventFilter().byFromDate(DateTimeConverter.string2Date("2011-11-03T11:01:00.000+05:30"));
        
        Map<String, String> queryParams = eventFilter.getQueryParams();

        assertThat(queryParams.get("dateFrom")).doesNotContain(":");
    }
    
    @Test
    public void shouldEncodeParamValueForMuliplyStatues() throws Exception {
    	AlarmFilter alarmFilter = new AlarmFilter();
    	
    	Map<String, String> queryParams = alarmFilter.getQueryParams();
    	
    	assertThat(queryParams.get("status")).isNull();
    	
    	
    	alarmFilter = new AlarmFilter().byStatus(CumulocityAlarmStatuses.ACTIVE);
    	
    	queryParams = alarmFilter.getQueryParams();
    	
    	assertThat(queryParams.get("status")).isEqualTo("ACTIVE");
    	
    	
    	alarmFilter = new AlarmFilter().byStatus(CumulocityAlarmStatuses.ACTIVE, CumulocityAlarmStatuses.ACKNOWLEDGED);
    	
    	queryParams = alarmFilter.getQueryParams();
    	
    	assertThat(queryParams.get("status")).isEqualTo("ACTIVE%2CACKNOWLEDGED");
    }
}
