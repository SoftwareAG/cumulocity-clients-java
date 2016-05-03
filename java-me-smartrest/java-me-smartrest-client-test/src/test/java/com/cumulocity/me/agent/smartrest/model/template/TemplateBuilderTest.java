package com.cumulocity.me.agent.smartrest.model.template;

import org.junit.Test;

import static com.cumulocity.me.agent.smartrest.model.template.Json.json;
import static com.cumulocity.me.agent.smartrest.model.template.Path.path;
import static com.cumulocity.me.agent.smartrest.model.template.Method.*;
import static com.cumulocity.me.agent.smartrest.model.template.TemplateBuilder.*;
import static com.cumulocity.me.agent.smartrest.model.template.PlaceholderType.STRING;
import static com.cumulocity.me.agent.smartrest.model.template.PlaceholderType.UNSIGNED;
import static org.fest.assertions.Assertions.assertThat;

public class TemplateBuilderTest {

    @Test
    public void shouldBuldEmptyRequestTemplate() {
        final String result = requestTemplate()
                .messageId(100)
                .path(path("/"))
                .method(GET)
                .build();

        assertThat(result).isEqualTo("10,100,GET,/,,,&&,,\r\n");
    }

    @Test
    public void shouldBuldEmptyResponseTemplate() {
        final String result = responseTemplate()
                .messageId(1000)
                .jsonPath("$.c8y_IsDevice")
                .jsonPath("$.id")
                .build();

        assertThat(result).isEqualTo("11,1000,,$.c8y_IsDevice,$.id\r\n");
    }

    @Test
    public void shouldBuildRequestTemplate() {
        final String result = requestTemplate()
                .messageId(100)
                .method(POST)
                .path(path("/inventory/managedObjects"))
                .content("application/vnd.com.nsn.cumulocity.managedObject+json")
                .accept("application/vnd.com.nsn.cumulocity.managedObject+json")
                .placeholder(PLACEHOLDER)
                .json(json()
                        .addString("name", PLACEHOLDER)
                        .addJson("c8y_IsDevice", json())
                        .addJson("com_cumulocity_model_Agent", json())
                        .addJson("c8y_RequiredAvailability", json().addValue("responseInterval", PLACEHOLDER))
                        .addString("type", "J2ME-Agent"))
                .build();

        assertThat(result).isEqualTo("10,100,POST,/inventory/managedObjects,application/vnd.com.nsn.cumulocity.managedObject+json,application/vnd.com.nsn.cumulocity.managedObject+json,&&,,\"{\"\"name\"\":\"\"&&\"\",\"\"c8y_IsDevice\"\":{},\"\"com_cumulocity_model_Agent\"\":{},\"\"c8y_RequiredAvailability\"\":{\"\"responseInterval\"\":&&},\"\"type\"\":\"\"J2ME-Agent\"\"}\"\r\n");
    }

    @Test
    public void shouldBuildRequestTemplateWithPlaceholdersTypes() {
        final String result = requestTemplate()
                .messageId(120)
                .method(PUT)
                .path(path("/inventory/managedObjects").add(PLACEHOLDER))
                .content("application/vnd.com.nsn.cumulocity.managedObject+json")
                .placeholderType(UNSIGNED, STRING, STRING, STRING)
                .json(json()
                        .addJson("c8y_Hardware", json()
                                .addString("model", PLACEHOLDER)
                                .addString("revision", PLACEHOLDER)
                                .addString("serialNumber", PLACEHOLDER)))
                .build();

        assertThat(result).isEqualTo("10,120,PUT,/inventory/managedObjects/&&,application/vnd.com.nsn.cumulocity.managedObject+json,,&&,UNSIGNED STRING STRING STRING,\"{\"\"c8y_Hardware\"\":{\"\"model\"\":\"\"&&\"\",\"\"revision\"\":\"\"&&\"\",\"\"serialNumber\"\":\"\"&&\"\"}}\"\r\n");
    }

    @Test
    public void shouldBuildRequestTemplateWithPlaceholderInPath() {
        final String result = requestTemplate()
                .messageId(101)
                .method(GET)
                .path(path("/identity/externalIds").add(PLACEHOLDER).add(PLACEHOLDER))
                .build();

        assertThat(result).isEqualTo("10,101,GET,/identity/externalIds/&&/&&,,,&&,,\r\n");
    }
}
