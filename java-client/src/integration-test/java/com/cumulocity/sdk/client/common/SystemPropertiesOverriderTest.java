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
package com.cumulocity.sdk.client.common;

import java.util.HashMap;
import java.util.Map;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;

public class SystemPropertiesOverriderTest {

    private Map<Object, Object> properties = new HashMap<Object, Object>();

    @Test
    public void shouldReturnOriginalPropertyIfSystemIsNotDefined() throws Exception {
        properties.put("key", "value");

        SystemPropertiesOverrider overrider = new SystemPropertiesOverrider(properties);

        Assert.assertThat(overrider.get("key"), Matchers.is("value"));
    }

    @Test
    public void shouldReturnSystemPropertyIfSystemIsDefined() throws Exception {
        properties.put("system", "value");
        System.setProperty("system", "overridden");

        SystemPropertiesOverrider overrider = new SystemPropertiesOverrider(properties);

        Assert.assertThat(overrider.get("system"), Matchers.is("overridden"));
    }
}
