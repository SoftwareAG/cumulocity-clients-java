package com.cumulocity.microservice.subscription.repository.application;

import com.google.common.base.Suppliers;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ApplicationApiRepresentationTest {

    private final ApplicationApiRepresentation representation = ApplicationApiRepresentation.of(Suppliers.ofInstance("/"));

    @Test
    public void shouldReturnUrlWithoutDoubledSlashes() {
        String url = representation.getBootstrapUserUrl("service");

        assertThat(url).isEqualTo("/application/applications/service/bootstrapUser");
    }
}
