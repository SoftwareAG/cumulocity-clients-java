package com.cumulocity.me.agent.util;

import org.fest.assertions.Assertions;
import org.junit.Assert;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

public class ArraysTest {

    @Test
    public void shouldBubbleSortCorrectly() throws Exception {
        Integer[] integers = aUnsortedArray();
        Arrays.bubbleSort(integers, new IntegerComparator());
        Integer[] control = aUnsortedArray();
        java.util.Arrays.sort(control);
        Assertions.assertThat(integers).isEqualTo(control);
    }

    private Integer[] aUnsortedArray() {
        return new Integer[]{
                new Integer(4),
                new Integer(8),
                new Integer(0),
                new Integer(-5),
                new Integer(3),
                new Integer(0)
        };
    }

}