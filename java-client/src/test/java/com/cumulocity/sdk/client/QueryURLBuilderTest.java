package com.cumulocity.sdk.client;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

public class QueryURLBuilderTest {

    @Test
    public void shouldBuildUrlForOneParameter() throws Exception {
        // given
        Map<String, String> filters = new HashMap<String, String>();
        filters.put("a", "b");

        List<String> uriTemplates = new ArrayList<String>();
        uriTemplates.add("http://something?a={a}");

        String[] optionalParameters = new String[] {};

        QueryURLBuilder urlBuilder = new QueryURLBuilder(new TemplateUrlParser(), filters, uriTemplates, optionalParameters);

        // when
        String url = urlBuilder.build();

        // then
        assertThat(url, is(equalTo("http://something?a=b")));
    }

    @Test
    public void shouldBuildUrlForTwoParameters() throws Exception {
        // given
        Map<String, String> filters = new HashMap<String, String>();
        filters.put("a", "b");
        filters.put("c", "d");

        List<String> uriTemplates = new ArrayList<String>();
        uriTemplates.add("http://something?a={a}&c={c}");

        String[] optionalParameters = new String[] {};

        QueryURLBuilder urlBuilder = new QueryURLBuilder(new TemplateUrlParser(), filters, uriTemplates, optionalParameters);

        // when
        String url = urlBuilder.build();

        // then
        assertThat(url, is(equalTo("http://something?a=b&c=d")));
    }

    @Test
    public void shouldNotBuildUrlForUnknownParameters() throws Exception {
        // given
        Map<String, String> filters = new HashMap<String, String>();
        filters.put("s", "b");
        filters.put("f", "d");

        List<String> uriTemplates = new ArrayList<String>();
        uriTemplates.add("http://something?a={a}&c={c}");

        String[] optionalParameters = new String[] {};

        QueryURLBuilder urlBuilder = new QueryURLBuilder(new TemplateUrlParser(), filters, uriTemplates, optionalParameters);

        // when
        String url = urlBuilder.build();

        // then
        assertThat(url, is(nullValue()));
    }

    @Test
    public void shouldBuildUrlForOptionalParametersWithValue() throws Exception {
        // given
        Map<String, String> filters = new HashMap<String, String>();
        filters.put("a", "b");
        filters.put("c", "d");

        List<String> uriTemplates = new ArrayList<String>();
        uriTemplates.add("http://something?a={a}&c={c}");

        String[] optionalParameters = new String[] { "c" };

        QueryURLBuilder urlBuilder = new QueryURLBuilder(new TemplateUrlParser(), filters, uriTemplates, optionalParameters);

        // when
        String url = urlBuilder.build();

        // then
        assertThat(url, is(equalTo("http://something?a=b&c=d")));
    }

    @Test
    public void shouldBuildUrlForOptionalParametersWithoutValue() throws Exception {
        // given
        Map<String, String> filters = new HashMap<String, String>();
        filters.put("a", "b");

        List<String> uriTemplates = new ArrayList<String>();
        uriTemplates.add("http://something?a={a}&c={c}");

        String[] optionalParameters = new String[] { "c" };

        QueryURLBuilder urlBuilder = new QueryURLBuilder(new TemplateUrlParser(), filters, uriTemplates, optionalParameters);

        // when
        String url = urlBuilder.build();

        // then
        assertThat(url, is(equalTo("http://something?a=b")));
    }

    @Test
    public void shouldBuildUrlForOptionalParametersInBetweenMandatory() throws Exception {
        // given
        Map<String, String> filters = new HashMap<String, String>();
        filters.put("a", "b");
        filters.put("e", "f");

        List<String> uriTemplates = new ArrayList<String>();
        uriTemplates.add("http://something?a={a}&c={c}&e={e}");

        String[] optionalParameters = new String[] { "c" };

        QueryURLBuilder urlBuilder = new QueryURLBuilder(new TemplateUrlParser(), filters, uriTemplates, optionalParameters);

        // when
        String url = urlBuilder.build();

        // then
        assertThat(url, is(equalTo("http://something?a=b&e=f")));
    }

    @Test
    public void shouldBuildUrlForOptionalParametersAsFirstInUri() throws Exception {
        // given
        Map<String, String> filters = new HashMap<String, String>();
        filters.put("a", "b");
        filters.put("e", "f");

        List<String> uriTemplates = new ArrayList<String>();
        uriTemplates.add("http://something?c={c}&a={a}&e={e}");

        String[] optionalParameters = new String[] { "c" };

        QueryURLBuilder urlBuilder = new QueryURLBuilder(new TemplateUrlParser(), filters, uriTemplates, optionalParameters);

        // when
        String url = urlBuilder.build();

        // then
        assertThat(url, is(equalTo("http://something?a=b&e=f")));
    }

}
