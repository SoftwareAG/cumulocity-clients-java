package com.cumulocity.lpwan.devicetype.model;

import com.cumulocity.lpwan.payload.uplink.model.AlarmMapping;
import com.cumulocity.lpwan.payload.uplink.model.EventMapping;
import com.cumulocity.lpwan.payload.uplink.model.ManagedObjectMapping;
import com.cumulocity.lpwan.payload.uplink.model.MeasurementMapping;
import lombok.Data;

@Data
public class UplinkConfigurationMapping {

    private AlarmMapping alarmMapping;
    private MeasurementMapping measurementMapping;
    private EventMapping eventMapping;
    private ManagedObjectMapping managedObjectMapping;

    public boolean containsAlarmMapping() {
        if (alarmMapping == null) {
            return false;
        }
        return true;
    }

    public boolean containsMeasurementMapping() {
        if (measurementMapping == null) {
            return false;
        }
        return true;
    }

    public boolean containsEventMapping() {
        if (eventMapping == null) {
            return false;
        }
        return true;
    }

    public boolean containsManagedObjectMapping() {
        if (managedObjectMapping == null) {
            return false;
        }
        return true;
    }
}
