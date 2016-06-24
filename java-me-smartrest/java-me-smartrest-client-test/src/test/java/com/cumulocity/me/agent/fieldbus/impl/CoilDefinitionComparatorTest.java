package com.cumulocity.me.agent.fieldbus.impl;

import com.cumulocity.me.agent.fieldbus.model.CoilDefinition;
import org.fest.assertions.Assertions;
import org.junit.Test;

import static org.fest.assertions.Assertions.assertThat;
import static org.junit.Assert.*;

public class CoilDefinitionComparatorTest {
    CoilDefinitionComparator comparator = new CoilDefinitionComparator();

    @Test
    public void shouldCompareEqualsCorrecty() throws Exception {
        CoilDefinition first = new CoilDefinitionBuilder().withNumber(1).build();
        CoilDefinition second = new CoilDefinitionBuilder().withNumber(1).build();
        assertThat(comparator.compare(first, second)).isEqualTo(0);
    }

    @Test
    public void shouldCompareLessCorrecty() throws Exception {
        CoilDefinition first = new CoilDefinitionBuilder().withNumber(1).build();
        CoilDefinition second = new CoilDefinitionBuilder().withNumber(5).build();
        assertThat(comparator.compare(first, second)).isEqualTo(-1);
    }

    @Test
    public void shouldCompareMoreCorrecty() throws Exception {
        CoilDefinition first = new CoilDefinitionBuilder().withNumber(7).build();
        CoilDefinition second = new CoilDefinitionBuilder().withNumber(3).build();
        assertThat(comparator.compare(first, second)).isEqualTo(1);
    }

}