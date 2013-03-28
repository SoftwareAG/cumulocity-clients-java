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
package com.cumulocity.sdk.client;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

public class TemplateUrlParserTest {

    @Test
    public void shouldReplacePlaceholdersInTemplate() throws Exception {
        // Given
        String templateUrl = "myTemplateUrl?myParam={p1}&myParam2={p2}";
        Map<String, String> params = new HashMap<String, String>();
        params.put("p1", "myValue1");
        params.put("p2", "myValue2");

        // When
        String finalUrl = new TemplateUrlParser().replacePlaceholdersWithParams(templateUrl, params);

        // Then
        assertThat(finalUrl, is("myTemplateUrl?myParam=myValue1&myParam2=myValue2"));
    }

    @Test
    public void shouldReplacePlaceholdersInTemplateWithUrlEncodedParams() throws Exception {
        // Given
        String templateUrl = "myTemplateUrl?source={source}&type={type}";
        Map<String, String> params = new HashMap<String, String>();
        params.put("source", "myStrangeSource{type}");
        params.put("type", "myType");

        // When
        String finalUrl = new TemplateUrlParser().replacePlaceholdersWithParams(templateUrl, params);

        // Then
        assertThat(finalUrl, is("myTemplateUrl?source=myStrangeSource%7Btype%7D&type=myType"));
    }

    @Test
    public void shouldReplacePlaceholdersInTemplateWithUrlEncodedParamsContainingSpaces() throws Exception {
        // Given
        String templateUrl = "myTemplateUrlsource={source}&type={type}";
        Map<String, String> params = new HashMap<String, String>();
        params.put("source", "myStrangeSource {type}");
        params.put("type", "myType");

        // When
        String finalUrl = new TemplateUrlParser().replacePlaceholdersWithParams(templateUrl, params);

        // Then
        assertThat(finalUrl, is("myTemplateUrlsource=myStrangeSource%20%7Btype%7D&type=myType"));
    }


    @Test
    public void shouldReplacePlaceholdersInTemplateWithUrlProperlyEncodedValuesContainingSpaces() throws Exception {
        // Given
        String templateUrl = "myTemplateUrlsource={source}&type={type}?source={source}&type={type}";
        Map<String, String> params = new HashMap<String, String>();
        params.put("source", "myStrangeSource {type}");
        params.put("type", "myType");

        // When
        String finalUrl = new TemplateUrlParser().replacePlaceholdersWithParams(templateUrl, params);

        // Then
        assertThat(finalUrl, is("myTemplateUrlsource=myStrangeSource%20%7Btype%7D&type=myType?source=myStrangeSource+%7Btype%7D&type=myType"));
    }
}
