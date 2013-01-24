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
package com.cumulocity.me.sdk.client;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import com.cumulocity.me.lang.ArrayList;
import com.cumulocity.me.lang.HashMap;
import com.cumulocity.me.lang.List;
import com.cumulocity.me.lang.Map;

public class QueryURLBuilderTest {

    @Test
    public void shouldBuildUrlForOneParameter() throws Exception {
        // given
        Map filters = new HashMap();
        filters.put("a", "b");

        List uriTemplates = new ArrayList();
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
        Map filters = new HashMap();
        filters.put("a", "b");
        filters.put("c", "d");

        List uriTemplates = new ArrayList();
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
        Map filters = new HashMap();
        filters.put("s", "b");
        filters.put("f", "d");

        List uriTemplates = new ArrayList();
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
        Map filters = new HashMap();
        filters.put("a", "b");
        filters.put("c", "d");

        List uriTemplates = new ArrayList();
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
        Map filters = new HashMap();
        filters.put("a", "b");

        List uriTemplates = new ArrayList();
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
        Map filters = new HashMap();
        filters.put("a", "b");
        filters.put("e", "f");

        List uriTemplates = new ArrayList();
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
        Map filters = new HashMap();
        filters.put("a", "b");
        filters.put("e", "f");

        List uriTemplates = new ArrayList();
        uriTemplates.add("http://something?c={c}&a={a}&e={e}");

        String[] optionalParameters = new String[] { "c" };

        QueryURLBuilder urlBuilder = new QueryURLBuilder(new TemplateUrlParser(), filters, uriTemplates, optionalParameters);

        // when
        String url = urlBuilder.build();

        // then
        assertThat(url, is(equalTo("http://something?a=b&e=f")));
    }

}
