package com.cumulocity.me.agent.fieldbus.impl;

public class AlarmMapKey {
    private final String deviceId;
    private final String alarmType;

    public AlarmMapKey(String deviceId, String alarmType) {
        this.deviceId = deviceId;
        this.alarmType = alarmType;
    }

    public String getAlarmType() {
        return alarmType;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AlarmMapKey that = (AlarmMapKey) o;

        if (deviceId != null ? !deviceId.equals(that.deviceId) : that.deviceId != null) return false;
        return alarmType != null ? alarmType.equals(that.alarmType) : that.alarmType == null;

    }

    public int hashCode() {
        int result = deviceId != null ? deviceId.hashCode() : 0;
        result = 31 * result + (alarmType != null ? alarmType.hashCode() : 0);
        return result;
    }
}
