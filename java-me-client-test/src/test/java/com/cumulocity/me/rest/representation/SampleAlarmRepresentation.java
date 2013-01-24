package com.cumulocity.me.rest.representation;

import java.util.Date;

import com.cumulocity.me.rest.representation.alarm.AlarmRepresentation;
import com.cumulocity.me.rest.representation.alarm.AlarmRepresentationBuilder;

public enum SampleAlarmRepresentation {
    
    ALARM_REPRESENTATION {
        @Override
        public AlarmRepresentationBuilder builder() {
            //@formatter:off
            return RestRepresentationObjectBuilder.anAlarmRepresentation()
                    .withSeverity(ALARM_SEVERITY)
                    .withStatus(ALARM_STATUS)
                    .withType(ALARM_TYPE)
                    .withText(ALARM_TEXT)
                    .withTime(ALARM_TIME);
            //@formatter:on
        }
    };

    public static final String ALARM_SEVERITY = "Alarm severity #";

    public static final String ALARM_STATUS = "Alarm status #";

    public static final String ALARM_TYPE = "Alarm Type #";

    public static final String ALARM_TEXT = "Alarm Text #";

    public static final Date ALARM_TIME = new Date();

    public abstract AlarmRepresentationBuilder builder();

    public AlarmRepresentation build() {
        return builder().build();
    }

}
