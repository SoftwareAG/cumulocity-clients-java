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

}
