package com.cumulocity.rest.representation.tenant.builder.test;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import com.cumulocity.rest.representation.tenant.TenantRepresentation;
import com.cumulocity.rest.representation.tenant.builder.TenantRepresentationBuilder;

public class TenantRepresentationBuilderTest {

    private TenantRepresentationBuilder tenantBuilder = new TenantRepresentationBuilder();

    @Test
    public void shouldBuildTenantRepresentationGivenTenantPropertyValues() {
        // When
        tenantBuilder.withId("tenantId");
        TenantRepresentation representation = tenantBuilder.build();
        // Then
        assertThat(representation.getId()).isEqualTo("tenantId");
    }
}
