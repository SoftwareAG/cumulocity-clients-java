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
