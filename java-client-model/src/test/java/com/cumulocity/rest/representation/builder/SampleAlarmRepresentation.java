package com.cumulocity.rest.representation.builder;

import com.cumulocity.rest.representation.alarm.AlarmRepresentation;
import org.joda.time.DateTime;

import static com.cumulocity.model.util.DateTimeUtils.nowUTC;

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
                    .withDateTime(ALARM_TIME);
            //@formatter:on
        }
    };

    public static final String ALARM_SEVERITY = "Alarm severity #";

    public static final String ALARM_STATUS = "Alarm status #";

    public static final String ALARM_TYPE = "Alarm Type #";

    public static final String ALARM_TEXT = "Alarm Text #";

    public static final DateTime ALARM_TIME = nowUTC();

    public abstract AlarmRepresentationBuilder builder();

    public AlarmRepresentation build() {
        return builder().build();
    }

}
