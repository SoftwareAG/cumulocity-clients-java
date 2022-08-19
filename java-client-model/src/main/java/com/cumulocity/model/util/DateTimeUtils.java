package com.cumulocity.model.util;

import com.cumulocity.model.DateTimeConverter;
import org.joda.time.*;
import org.joda.time.chrono.ISOChronology;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

    /**
     * Don't use {@link DateTimeUtils#nowLocal()} as it adds {@link DateTimeUtils#convertNameToOffset(DateTime)} which breaks
     * the daylight saving time transition when operating on dates, for example when using minusMonth(1)
     *
     * @deprecated use {@link DateTimeUtils#nowDateTimeLocal}
     */
    @Deprecated
    public static DateTime nowLocal() {
        return convertNameToOffset(new DateTime(org.joda.time.DateTimeUtils.currentTimeMillis(), chronologyLocal()));
    }

    /**
     * Don't use {@link DateTimeUtils#nowLocal()} as it adds {@link DateTimeUtils#convertNameToOffset(DateTime)} which breaks
     * the daylight saving time transition when operating on dates, for example when using minusMonth(1)
     */
    public static DateTime nowDateTimeLocal() {
        return new DateTime(org.joda.time.DateTimeUtils.currentTimeMillis(), chronologyLocal());
    }

    public static DateTime convertNameToOffset(DateTime dateTime) {
        return DateTimeConverter.string2Date(DateTimeConverter.date2String(dateTime));
    }

    public static void checkChronologyUTC(DateTime dateTime) {
        if (dateTime != null) {
            if (!chronologyUTC().equals(dateTime.getChronology())) {
                throw new IllegalStateException();
            }
        }
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

    @Deprecated
    public static DateTime nowLocalMidnight() {
        return toMidnight(nowLocal());
    }


    public static DateTime beginningOfDay(DateTime time) {
        if (time == null) {
            return null;
        }
        return time.withChronology(chronologyLocal()).withTimeAtStartOfDay().withChronology(time.getChronology());
    }

    public static Interval today() {
        final DateTime beginningOfDay = beginningOfDay(nowLocal());
        return toChronologyUTC(new Interval(beginningOfDay, beginningOfDay.plusDays(1)));
    }

    public static DateTime endOfDay(DateTime time) {
        return toMidnight(time).plusDays(1);
    }

    public static DateTime beginningOfTodayLocal() {
        return beginningOfDay(nowLocal());
    }

    public static DateTime endOfTodayLocal() {
        return endOfDay(nowLocal());
    }

    /**
     * @param dateTime dateTime
     * @return midnight data time
     * @deprecated use {@link DateTimeUtils#beginningOfDay(DateTime)} instead
     */
    @Deprecated
    public static DateTime toMidnight(DateTime dateTime) {
        return new DateMidnight(dateTime).toDateTime();
    }

    /**
     * @return midnight data time
     * @see DateTimeUtils#beginningOfTodayLocal()
     */
    public static DateTime nowUTCMidnight() {
        return beginningOfTodayLocal().withChronology(chronologyUTC());
    }

    public static DateTime newLocal(Date date, int offsetInMillis) {
        return convertNameToOffset(new DateTime(date, ISOChronology.getInstance(DateTimeZone.forOffsetMillis(offsetInMillis))));
    }

    public static DateTime toChronologyUTC(DateTime dateTime) {
        return dateTime.withChronology(chronologyUTC());
    }

    public static Interval toChronologyUTC(Interval interval) {
        return interval.withChronology(chronologyUTC());
    }

    public static DateTime toUTC(DateTime dateTime) {
        if (dateTime == null) {
            return null;
        } else {
            return dateTime.toDateTime(chronologyUTC());
        }
    }

    public static DateTime beginningOfMonth(final DateTime dateTime) {
        return beginningOfDay(dateTime.withDayOfMonth(1));
    }

    public static DateTime endOfMonth(final DateTime dateTime) {
        return beginningOfDay(dateTime.withDayOfMonth(1).plusMonths(1));
    }
    public static List<Interval> splitInterval(Interval interval, Duration chunk) {
        DateTime subStart = interval.getStart();
        List<Interval> list = new ArrayList<>();
        do {
            DateTime subEnd = subStart.plus(chunk);
            if (subEnd.isAfter(interval.getEnd())) {
                subEnd = interval.getEnd();
            }
            list.add(new Interval(subStart, subEnd));
            subStart = subEnd;
        } while (subStart.isBefore(interval.getEnd()));
        return list;
    }

    public static DateTime theBeginningOfEpoch(){
        return new DateTime(0);
    }
}
