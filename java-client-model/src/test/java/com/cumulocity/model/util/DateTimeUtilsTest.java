package com.cumulocity.model.util;

import org.joda.time.*;
import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.cumulocity.model.util.DateTimeUtils.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.joda.time.Minutes.minutes;

public class DateTimeUtilsTest {

    @Test
    public void shouldReturnIntervalOfCurrentDay() {
        DateTime now = nowLocal();
        DateTime beginOfDay = now.withTimeAtStartOfDay().withChronology(chronologyUTC());
        DateTime endOfDay = beginOfDay.plusDays(1);
        //
        final Interval today = today();

        assertThat(today).isEqualTo(new Interval(beginOfDay, endOfDay));
    }

    @Test
    public void shouldReturnIntervalOfLocalCurrentDay() {
        // given
        DateTime now = nowLocal();
        DateTime beginOfDay = now.withTimeAtStartOfDay().withChronology(chronologyUTC());
        DateTime endOfDay = beginOfDay.plusDays(1);

        // when
        final Interval today = new Interval(beginningOfTodayLocal(), endOfTodayLocal()).withChronology(chronologyUTC());

        // Then
        assertThat(today).isEqualTo(new Interval(beginOfDay, endOfDay));
    }

    @Test
    public void shouldReturnBeginningOfDayLastMonth() {
        // given
        DateTime testDateFromJanuary = new DateTime("2013-02-25");
        DateTime firstDayOfMonth = new DateTime("2013-02-01");

        // when
        DateTime beginningOfMonth = beginningOfMonth(testDateFromJanuary);

        // Then
        assertThat(beginningOfMonth.toString()).isEqualTo(firstDayOfMonth.toString());
    }

    @Test
    public void shouldReturnMidnightOfLastDayPreviousMonth() {
        // given
        DateTime testDateFromJanuary = new DateTime("2013-02-22");
        DateTime lastDayOfMonth = new DateTime("2013-03-01");

        // when
        DateTime endOfMonth = endOfMonth(testDateFromJanuary);

        // Then
        assertThat(endOfMonth.toString()).isEqualTo(lastDayOfMonth.toString());
    }

    @Test
    public void shouldSplitInterval() {
        // given
        DateTime now = nowLocal();
        Interval interval = new Interval(now, now.plus(minutes(55)));
        Duration chunk = minutes(20).toStandardDuration();

        // when
        List<Interval> subIntervals = DateTimeUtils.splitInterval(interval, chunk);

        // then
        assertThat(subIntervals.size()).isEqualTo(3);
        assertThat(subIntervals.get(0)).isEqualTo(new Interval(now, now.plus(minutes(20))));
        assertThat(subIntervals.get(1)).isEqualTo(new Interval(now.plus(minutes(20)), now.plus(minutes(40))));
        assertThat(subIntervals.get(2)).isEqualTo(new Interval(now.plus(minutes(40)), interval.getEnd()));
    }

    @Test
    public void shouldHandleChunkLargerThanSplittingInterval() {
        // given
        DateTime now = nowLocal();
        Interval interval = new Interval(now, now.plus(minutes(55)));
        Duration chunk = minutes(60).toStandardDuration();

        // when
        List<Interval> subIntervals = DateTimeUtils.splitInterval(interval, chunk);

        // then
        assertThat(subIntervals.size()).isEqualTo(1);
        assertThat(subIntervals.get(0)).isEqualTo(new Interval(now, now.plus(minutes(55))));
    }
}
