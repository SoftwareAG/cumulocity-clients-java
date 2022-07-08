package com.cumulocity.rest.representation.alarm;

import com.cumulocity.rest.representation.CumulocityMediaType;

public class AlarmMediaType extends CumulocityMediaType {
    
    public static final AlarmMediaType ALARM = new AlarmMediaType("alarm");

    public static final String ALARM_TYPE = APPLICATION_VND_COM_NSN_CUMULOCITY + "alarm+json;" + VND_COM_NSN_CUMULOCITY_PARAMS;

    public static final AlarmMediaType ALARM_COLLECTION = new AlarmMediaType("alarmCollection");

    public static final String ALARM_COLLECTION_TYPE = APPLICATION_VND_COM_NSN_CUMULOCITY + "alarmCollection+json;" + VND_COM_NSN_CUMULOCITY_PARAMS;

    public static final AlarmMediaType ALARM_API = new AlarmMediaType("alarmApi");

    public static final String ALARM_API_TYPE = APPLICATION_VND_COM_NSN_CUMULOCITY + "alarmApi+json;" + VND_COM_NSN_CUMULOCITY_PARAMS;

    public static final String AVAILABILITY_STAT_TYPE = APPLICATION_VND_COM_NSN_CUMULOCITY + "availabilityStat+json;" + VND_COM_NSN_CUMULOCITY_PARAMS;

    public static final String AVAILABILITY_STAT_COLLECTION_TYPE = APPLICATION_VND_COM_NSN_CUMULOCITY + "availabilityStatCollection+json;" + VND_COM_NSN_CUMULOCITY_PARAMS;

    public AlarmMediaType(String string) {
        super(string);
    }
}
