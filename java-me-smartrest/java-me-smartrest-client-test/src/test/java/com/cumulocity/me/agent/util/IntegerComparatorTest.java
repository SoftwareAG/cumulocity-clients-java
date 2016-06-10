package com.cumulocity.me.agent.util;

import org.fest.assertions.Assertions;
import org.junit.Test;

import static org.fest.assertions.Assertions.assertThat;
import static org.junit.Assert.*;

public class IntegerComparatorTest {

    private IntegerComparator comparator = new IntegerComparator();

    @Test
    public void shouldSupportInteger() throws Exception {
        boolean result = comparator.isSupported(new Integer(0));
        assertThat(result).isTrue();
    }

    @Test
    public void shouldNotSupportObject() throws Exception {
        boolean result = comparator.isSupported(new Object());
        assertThat(result).isFalse();
    }

    @Test
    public void shouldNotSupportNull() throws Exception {
        boolean result = comparator.isSupported(null);
        assertThat(result).isFalse();
    }

    @Test
    public void shouldCompareLessCorrectly() throws Exception {
        int result = comparator.compare(new Integer(1), new Integer(2));
        assertThat(result).isEqualTo(-1);
    }

    @Test
    public void shouldCompareEqualCorrectly() throws Exception {
        int result = comparator.compare(new Integer(1), new Integer(1));
        assertThat(result).isEqualTo(0);
    }

    @Test
    public void shouldCompareMoreCorrectly() throws Exception {
        int result = comparator.compare(new Integer(3), new Integer(2));
        assertThat(result).isEqualTo(1);
    }

}