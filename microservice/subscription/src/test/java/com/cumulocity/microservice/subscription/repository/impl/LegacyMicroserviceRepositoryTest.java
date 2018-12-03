package com.cumulocity.microservice.subscription.repository.impl;

import com.cumulocity.microservice.subscription.repository.MicroserviceRepositoryBuilder;
import com.cumulocity.microservice.subscription.repository.application.ApplicationApiRepresentation;
import com.cumulocity.rest.representation.application.ApplicationCollectionRepresentation;
import com.cumulocity.rest.representation.application.ApplicationRepresentation;
import com.google.common.base.Predicate;
import com.google.common.base.Suppliers;
import com.google.common.collect.ImmutableList;
import org.assertj.core.api.Condition;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpMethod;
import org.springframework.mock.env.MockEnvironment;

import java.util.Arrays;
import java.util.Collection;

import static com.cumulocity.microservice.subscription.model.MicroserviceMetadataRepresentation.microserviceMetadataRepresentation;
import static com.cumulocity.microservice.subscription.repository.MicroserviceRepositoryBuilder.microserviceRepositoryBuilder;
import static com.cumulocity.rest.representation.application.ApplicationRepresentation.MICROSERVICE;
import static com.cumulocity.rest.representation.application.ApplicationRepresentation.applicationRepresentation;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.HttpMethod.PUT;

public class LegacyMicroserviceRepositoryTest {

    private static final String BASE_URL = "http://c8y.com";

    private FakeCredentialsSwitchingPlatform platform = new FakeCredentialsSwitchingPlatform();
    private ApplicationApiRepresentation api = ApplicationApiRepresentation.of(Suppliers.ofInstance(BASE_URL));

    private LegacyMicroserviceRepository repository;

    @Before
    public void setup() {
        MockEnvironment environment = new MockEnvironment();

        MicroserviceRepositoryBuilder builder = microserviceRepositoryBuilder()
                .baseUrl(Suppliers.ofInstance(BASE_URL))
                .connector(platform)
                .environment(environment)
                .username("test")
                .password("test");
        repository = (LegacyMicroserviceRepository) builder.build();
    }

    @Test
    public void shouldUpdateApplicationWhenAlreadyExistsAndRegistrationIsEnabled() {

        ApplicationRepresentation existing = applicationRepresentation()
                .id("3")
                .type(MICROSERVICE)
                .name("cep")
                .build();
        givenApplications(existing);

        ApplicationRepresentation cep = repository.register("cep", microserviceMetadataRepresentation()
                .role("TEST").requiredRole("TEST").build());

        assertThat(cep).isEqualTo(existing);

        assertThat(platform.take(byMethod(PUT)))
                .hasSize(1)
                .extracting("body")
                .have(new Condition<Object>() {
                    @Override
                    public boolean matches(Object value) {
                        if (value instanceof ApplicationRepresentation) {
                            assertThat(((ApplicationRepresentation) value).getRequiredRoles()).contains("TEST");
                            assertThat(((ApplicationRepresentation) value).getRoles()).contains("TEST");
                            return true;
                        }
                        return false;
                    }
                });
        assertThat(platform.take(byMethod(POST))).isEmpty();
    }

    @Test
    public void shouldRegisterApplicationWhenThereIsNoExisting() {
        givenApplications();
        repository.register("notExistingApp", microserviceMetadataRepresentation().build());

        Collection<FakeCredentialsSwitchingPlatform.Request> posts = platform.take(byMethod(POST));
        assertThat(posts).isNotEmpty();
        assertThat(posts)
                .hasSize(1)
                .extracting("body")
                .containsExactly(applicationRepresentation()
                        .id("0")
                        .name("notExistingApp")
                        .type("MICROSERVICE")
                        .key("notExistingApp-application-key")
                        .requiredRoles(ImmutableList.<String>of())
                        .roles(ImmutableList.<String>of())
                        .build());
    }

    @Test
    public void shouldNotRegisterApplicationWhenThereIsExisting() {
        givenApplications(applicationRepresentation().id("5").name("existingApp").type("MICROSERVICE").key("existingApp-application-key").build());

        repository.register("existingApp", microserviceMetadataRepresentation().build());

        assertThat(platform.take(byMethod(POST))).isEmpty();
    }

    @Test
    public void shouldNotFailOnMultipleCalls() {
        ApplicationRepresentation existing = applicationRepresentation()
                .type(MICROSERVICE)
                .name("cep")
                .build();

        assertThat(repository.register("existingApp", microserviceMetadataRepresentation().build())).isNotNull();
        assertThat(platform.take(byMethod(POST))).isNotEmpty();

        for (int i = 0; i < 10; ++i) {
            assertThat(repository.register("existingApp", microserviceMetadataRepresentation().build())).isNotNull();
            assertThat(platform.take(byMethod(POST))).isEmpty();
        }
    }

    private Predicate<FakeCredentialsSwitchingPlatform.Request> byMethod(final HttpMethod method) {
        return new Predicate<FakeCredentialsSwitchingPlatform.Request>() {
            @Override
            public boolean apply(FakeCredentialsSwitchingPlatform.Request request) {
                return request.getMethod().equals(method);
            }
        };
    }

    private void givenApplications(ApplicationRepresentation... applications) {

        ApplicationCollectionRepresentation collection = new ApplicationCollectionRepresentation();
        collection.setApplications(Arrays.asList(applications));
        for (ApplicationRepresentation app : applications) {
            platform.addApplication(app);
        }
    }

}
