package com.cumulocity.model.util;

import com.cumulocity.model.DateTimeConverter;
import org.joda.time.Chronology;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.chrono.ISOChronology;

import java.util.Date;

public class DateTimeUtils {

    public static Chronology chronologyUTC() {
        return ISOChronology.getInstanceUTC();
    }

    public static Chronology chronologyLocal() {
        return ISOChronology.getInstance();
    }

    public static DateTime nowUTC() {
        return new DateTime(org.joda.time.DateTimeUtils.currentTimeMillis(), chronologyUTC());
    }

    public static DateTime nowDateTimeLocal() {
        return new DateTime(org.joda.time.DateTimeUtils.currentTimeMillis(), chronologyLocal());
    }

    public static DateTime convertNameToOffset(DateTime dateTime) {
        return DateTimeConverter.string2Date(DateTimeConverter.date2String(dateTime));
    }

    public static DateTime newUTC(long millis) {
        return new DateTime(new Date(millis), chronologyUTC());
    }

    public static DateTime newUTC(Date date) {
        return new DateTime(date, chronologyUTC());
    }

    public static DateTime newLocal(long millis) {
        return convertNameToOffset(new DateTime(new Date(millis), chronologyLocal()));
    }

    public static DateTime newLocal(Date date) {
        return convertNameToOffset(new DateTime(date, chronologyLocal()));
    }

    public static DateTime newLocal(Date date, int offsetInMillis) {
        return convertNameToOffset(new DateTime(date, ISOChronology.getInstance(DateTimeZone.forOffsetMillis(offsetInMillis))));
    }

}
