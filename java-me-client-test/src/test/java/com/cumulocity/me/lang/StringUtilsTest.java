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
