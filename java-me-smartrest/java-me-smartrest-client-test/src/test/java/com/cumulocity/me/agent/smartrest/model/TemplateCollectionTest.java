package com.cumulocity.me.agent.smartrest.model;

import org.junit.Test;

import static com.cumulocity.me.agent.smartrest.model.template.TemplateBuilder.responseTemplate;
import static org.fest.assertions.Assertions.assertThat;

public class TemplateCollectionTest {

    @Test
    public void builtTemplateCollectionShouldNotEndWithBlankCharacters() {
        final TemplateCollection templateCollection = TemplateCollection.templateCollection()
                .xid("456")
                .template(responseTemplate().messageId(456))
                .build();

        assertThat(templateCollection.getTemplates()).isEqualTo(templateCollection.getTemplates().trim());
    }
}
