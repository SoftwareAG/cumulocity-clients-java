package com.cumulocity.microservice.subscription.repository.impl;

import com.cumulocity.microservice.subscription.repository.MicroserviceRepositoryBuilder;
import com.cumulocity.microservice.subscription.repository.application.ApplicationApiRepresentation;
import com.cumulocity.model.authentication.CumulocityBasicCredentials;
import com.cumulocity.model.authentication.CumulocityCredentials;
import com.cumulocity.rest.representation.application.ApplicationRepresentation;
import com.cumulocity.rest.representation.application.ApplicationUserRepresentation;
import com.cumulocity.sdk.client.SDKException;
import com.google.common.base.Predicate;
import com.google.common.base.Suppliers;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.HttpMethod;
import org.springframework.mock.env.MockEnvironment;

import static com.cumulocity.microservice.subscription.model.MicroserviceMetadataRepresentation.microserviceMetadataRepresentation;
import static com.cumulocity.microservice.subscription.model.core.PlatformProperties.IsolationLevel.MULTI_TENANT;
import static com.cumulocity.microservice.subscription.repository.MicroserviceRepositoryBuilder.MICROSERVICE_ISOLATION_ENV_NAME;
import static com.cumulocity.microservice.subscription.repository.MicroserviceRepositoryBuilder.microserviceRepositoryBuilder;
import static com.cumulocity.rest.representation.application.ApplicationRepresentation.MICROSERVICE;
import static com.cumulocity.rest.representation.application.ApplicationRepresentation.applicationRepresentation;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.springframework.http.HttpMethod.POST;

@RunWith(MockitoJUnitRunner.class)
public class CurrentMicroserviceRepositoryTest {

    private static final String BASE_URL = "http://c8y.com";

    private FakeCredentialsSwitchingPlatform platform = new FakeCredentialsSwitchingPlatform();
    private ApplicationApiRepresentation api = ApplicationApiRepresentation.of(Suppliers.ofInstance(BASE_URL));

    private CurrentMicroserviceRepository repository;

    @Before
    public void setup() {
        MockEnvironment environment = new MockEnvironment();
        environment.setProperty(MICROSERVICE_ISOLATION_ENV_NAME, MULTI_TENANT.toString());

        MicroserviceRepositoryBuilder builder = microserviceRepositoryBuilder()
                .baseUrl(Suppliers.ofInstance(BASE_URL))
                .connector(platform)
                .environment(environment);
        repository = (CurrentMicroserviceRepository) builder.build();
    }

    @Test
    public void shouldFailWhenNoCurrentApplication() {
        ApplicationRepresentation notRegistered = applicationRepresentation()
                .type(MICROSERVICE)
                .name("cep")
                .build();
        platform.switchTo(asCredentials(platform.bootstrapUserFor(notRegistered)));

        Throwable exception = catchThrowable(new ThrowingCallable() {
            @Override
            public void call() throws Throwable {
                repository.register("cep", microserviceMetadataRepresentation().build());
            }
        });

        assertThat(exception)
                .isInstanceOf(SDKException.class)
                .hasMessageContaining("Failed to load current microservice.");
    }

    @Test
    public void shouldNotFailOnMultipleCalls() {
        ApplicationRepresentation existing = applicationRepresentation()
                .type(MICROSERVICE)
                .name("existingApp")
                .build();
        platform.addApplication(existing);
        platform.switchTo(asCredentials(platform.bootstrapUserFor(existing)));

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

    private CumulocityCredentials asCredentials(ApplicationUserRepresentation user) {
        return CumulocityBasicCredentials.builder()
                .username(user.getName())
                .password(user.getPassword())
                .tenantId(user.getTenant())
                .build();
    }

}
