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
