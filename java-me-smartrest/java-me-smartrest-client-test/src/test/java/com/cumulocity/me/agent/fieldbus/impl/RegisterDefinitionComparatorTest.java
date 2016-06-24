package com.cumulocity.me.agent.fieldbus.impl;

import com.cumulocity.me.agent.fieldbus.model.RegisterDefinition;
import org.fest.assertions.Assertions;
import org.junit.Test;

import static org.fest.assertions.Assertions.assertThat;
import static org.junit.Assert.*;

public class RegisterDefinitionComparatorTest {
    private RegisterDefinitionComparator comparator = new RegisterDefinitionComparator();

    @Test
    public void shouldCompareEqualsCorrectly() throws Exception {
        RegisterDefinition first = new RegisterDefinitionBuilder().withNumber(1).withStartBit(3).build();
        RegisterDefinition second = new RegisterDefinitionBuilder().withNumber(1).withStartBit(3).build();
        assertThat(comparator.compare(first, second)).isEqualTo(0);
    }

    @Test
    public void shouldCompareLessCorrectly() throws Exception {
        RegisterDefinition first = new RegisterDefinitionBuilder().withNumber(1).build();
        RegisterDefinition second = new RegisterDefinitionBuilder().withNumber(4).build();
        assertThat(comparator.compare(first, second)).isEqualTo(-1);
    }

    @Test
    public void shouldCompareLessWithEqualNumberCorrectly() throws Exception {
        RegisterDefinition first = new RegisterDefinitionBuilder().withNumber(1).withStartBit(3).build();
        RegisterDefinition second = new RegisterDefinitionBuilder().withNumber(1).withStartBit(7).build();
        assertThat(comparator.compare(first, second)).isEqualTo(-1);
    }

    @Test
    public void shouldCompareMoreCorrectly() throws Exception {
        RegisterDefinition first = new RegisterDefinitionBuilder().withNumber(8).build();
        RegisterDefinition second = new RegisterDefinitionBuilder().withNumber(2).build();
        assertThat(comparator.compare(first, second)).isEqualTo(1);
    }

    @Test
    public void shouldCompareMoreWithEqualNumberCorrectly() throws Exception {
        RegisterDefinition first = new RegisterDefinitionBuilder().withNumber(1).withStartBit(8).build();
        RegisterDefinition second = new RegisterDefinitionBuilder().withNumber(1).withStartBit(2).build();
        assertThat(comparator.compare(first, second)).isEqualTo(1);
    }

}