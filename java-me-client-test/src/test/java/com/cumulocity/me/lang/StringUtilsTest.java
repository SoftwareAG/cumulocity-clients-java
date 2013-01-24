package com.cumulocity.me.lang;

import static org.fest.assertions.Assertions.assertThat;

import org.junit.Test;

import com.cumulocity.me.util.StringUtils;

public class StringUtilsTest {
    
    private static String SOURCE = "Test:Test:Test:Test:Test:";;

    @Test
    public void shouldReplaceAll() throws Exception {
        String expected = "Tests:Tests:Tests:Tests:Tests:";
        
        String output = StringUtils.replaceAll(SOURCE, "st", "sts");

        assertThat(output).isEqualTo(expected);
    }
    
    @Test
    public void shouldSplit() throws Exception {
        String[] output = StringUtils.split(SOURCE, ":");
        String[] expected = SOURCE.split(":");
        
        assertThat(output.length).isEqualTo(expected.length);
        for (int i = 0; i < output.length; i++) {
            assertThat(output[i]).isEqualTo(expected[i]);
        }
    }
    
}
