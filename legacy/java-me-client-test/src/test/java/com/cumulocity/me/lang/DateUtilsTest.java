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
package com.cumulocity.me.lang;

import static java.util.Calendar.DAY_OF_MONTH;
import static java.util.Calendar.HOUR_OF_DAY;
import static java.util.Calendar.MILLISECOND;
import static java.util.Calendar.MINUTE;
import static java.util.Calendar.MONTH;
import static java.util.Calendar.SECOND;
import static java.util.Calendar.YEAR;
import static org.fest.assertions.Assertions.assertThat;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import org.junit.Test;

import com.cumulocity.me.util.DateUtils;

public class DateUtilsTest {

    private static final String GMT = "GMT";
    private static final int DATE_MILLISECONDS = 932;
    private static final int DATE_SECONDS = 19;
    private static final int DATE_MINUTES = 3;
    private static final int DATE_HOUR_OF_DAY = 2;
    private static final int DATE_DAY_OF_MONTH = 22;
    private static final int DATE_MONTH = 3;
    private static final int DATE_YEAR = 2012;
    
    
    @Test
    public void shouldParsePositiveTimezoneDate() throws Exception {
        Date date = DateUtils.parse("2012-04-21T19:03:19.932+07:00");
        
        Calendar cal = getCalendarForDate(date, GMT);
        assertThat(cal.get(YEAR)).isEqualTo(DATE_YEAR);
        assertThat(cal.get(MONTH)).isEqualTo(DATE_MONTH);
        assertThat(cal.get(DAY_OF_MONTH)).isEqualTo(DATE_DAY_OF_MONTH);
        assertThat(cal.get(HOUR_OF_DAY)).isEqualTo(DATE_HOUR_OF_DAY);
        assertThat(cal.get(MINUTE)).isEqualTo(DATE_MINUTES);
        assertThat(cal.get(SECOND)).isEqualTo(DATE_SECONDS);
        assertThat(cal.get(MILLISECOND)).isEqualTo(DATE_MILLISECONDS);
    }
    
    @Test
    public void shouldParseNegativeTimezoneDate() throws Exception {
        Date date = DateUtils.parse("2012-04-21T02:03:19.932-07:00");
        
        Calendar cal = getCalendarForDate(date, GMT);
        assertThat(cal.get(YEAR)).isEqualTo(DATE_YEAR);
        assertThat(cal.get(MONTH)).isEqualTo(DATE_MONTH);
        assertThat(cal.get(DAY_OF_MONTH)).isEqualTo(20);
        assertThat(cal.get(HOUR_OF_DAY)).isEqualTo(19);
        assertThat(cal.get(MINUTE)).isEqualTo(DATE_MINUTES);
        assertThat(cal.get(SECOND)).isEqualTo(DATE_SECONDS);
        assertThat(cal.get(MILLISECOND)).isEqualTo(DATE_MILLISECONDS);
    }
    
    @Test
    public void shouldParseDateWithoutTimezone() throws Exception {
        Date date = DateUtils.parse("2012-04-21T18:03:19.930Z");
        
        Calendar cal = getCalendarForDate(date, GMT);
        assertThat(cal.get(HOUR_OF_DAY)).isEqualTo(18);
    }

    @Test
    public void shouldFormatPositiveTimezoneDate() throws Exception {
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
        cal.set(YEAR, DATE_YEAR);
        cal.set(MONTH, DATE_MONTH);
        cal.set(DAY_OF_MONTH, 21);
        cal.set(HOUR_OF_DAY, 19);
        cal.set(MINUTE, DATE_MINUTES);
        cal.set(SECOND, DATE_SECONDS);
        cal.set(MILLISECOND, DATE_MILLISECONDS);

        String dateString = DateUtils.format(cal.getTime());

        assertThat(dateString).isEqualTo("2012-04-22T02:03:19.932Z");
    }

    private Calendar getCalendarForDate(Date date, String timeZoneId) {
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone(timeZoneId));
        cal.setTime(date);
        return cal;
    }
}
