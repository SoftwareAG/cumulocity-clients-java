package com.cumulocity.microservice.subscription.repository.impl;

import com.cumulocity.microservice.subscription.repository.MicroserviceRepository;
import com.cumulocity.microservice.subscription.repository.MicroserviceRepositoryBuilder;
import com.cumulocity.rest.representation.application.ApplicationCollectionRepresentation;
import com.cumulocity.rest.representation.application.ApplicationRepresentation;
import com.google.common.base.Predicate;
import com.google.common.base.Suppliers;
import com.google.common.collect.ImmutableList;
import org.assertj.core.api.Condition;
import org.assertj.core.api.ThrowableAssert;
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
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.HttpMethod.PUT;

public class LegacyMicroserviceRepositoryTest {

    private static final String BASE_URL = "http://c8y.com";
    private static final String CURRENT_APPLICATION_NAME = "current-application-name";

    private FakeCredentialsSwitchingPlatform platform = new FakeCredentialsSwitchingPlatform();

    private LegacyMicroserviceRepository repository;

    @Before
    public void setup() {
        MockEnvironment environment = new MockEnvironment();

        MicroserviceRepositoryBuilder builder = microserviceRepositoryBuilder()
                .baseUrl(Suppliers.ofInstance(BASE_URL))
                .connector(platform)
                .environment(environment)
                .username("test")
                .password("test")
                .applicationName(CURRENT_APPLICATION_NAME);
        repository = (LegacyMicroserviceRepository) builder.build();
    }

    @Test
    public void shouldNotRequireCurrentApplicationName(){
        //when
        MicroserviceRepository repository = repositoryWithoutCurrentApplicationName();

        //then
        assertThat(repository).isNotNull();
    }

    @Test
    public void shouldNotRegisterApplicationWhenApplicationNameWasNotProvidedToConstructor(){
        //given
        final MicroserviceRepository repository = repositoryWithoutCurrentApplicationName();

        //when
        Throwable throwable = catchThrowable(new ThrowableAssert.ThrowingCallable() {
            @Override
            public void call() {
                repository.register(microserviceMetadataRepresentation().build());
            }
        });

        //then
        assertThat(throwable)
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Application name must be provided at construction time");
    }

    @Test
    public void shouldNotGetCurrentApplicationWhenApplicationNameWasNotProvidedToConstructor(){
        //given
        final MicroserviceRepository repository = repositoryWithoutCurrentApplicationName();

        //when
        Throwable throwable = catchThrowable(new ThrowableAssert.ThrowingCallable() {
            @Override
            public void call() {
                repository.getCurrentApplication();
            }
        });

        //then
        assertThat(throwable)
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("You need to provide current application name at construction");
    }

    @Test
    public void shouldGetCurrentApplication(){
        //given
        ApplicationRepresentation existing = applicationRepresentation()
                .id("3")
                .type(MICROSERVICE)
                .name(CURRENT_APPLICATION_NAME)
                .build();
        givenApplications(existing);

        //when
        ApplicationRepresentation currentApplication = repository.getCurrentApplication();

        //then
        assertThat(currentApplication)
                .isNotNull()
                .isSameAs(existing);
    }

    @Test
    public void shouldUpdateApplicationWhenAlreadyExistsAndRegistrationIsEnabledWhenDeprecatedRegisterMethodIsUsed() {

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
    public void shouldUpdateApplicationWhenAlreadyExistsAndRegistrationIsEnabledWhenOneArgumentRegisterMethodIsUsed() {

        ApplicationRepresentation existing = applicationRepresentation()
                .id("3")
                .type(MICROSERVICE)
                .name(CURRENT_APPLICATION_NAME)
                .build();
        givenApplications(existing);

        ApplicationRepresentation cep = repository.register(microserviceMetadataRepresentation()
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
    public void shouldRegisterApplicationWhenThereIsNoExistingWhenOneArgumentRegisterMethodIsUsed() {
        givenApplications();
        repository.register(microserviceMetadataRepresentation().build());

        Collection<FakeCredentialsSwitchingPlatform.Request> posts = platform.take(byMethod(POST));
        assertThat(posts).isNotEmpty();
        assertThat(posts)
                .hasSize(1)
                .extracting("body")
                .containsExactly(applicationRepresentation()
                        .id("0")
                        .name(CURRENT_APPLICATION_NAME)
                        .type("MICROSERVICE")
                        .key(String.format("%s-application-key", CURRENT_APPLICATION_NAME))
                        .requiredRoles(ImmutableList.<String>of())
                        .roles(ImmutableList.<String>of())
                        .build());
    }

    @Test
    public void shouldRegisterApplicationWhenThereIsNoExistingWhenDeprecatedRegisterMethodIsUsed() {
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
    public void shouldRegisterApplicationWithDeprecatedMethodWhenApplicationNameWasNotPassedToConstructor() {
        //given
        givenApplications();
        MicroserviceRepository repository = repositoryWithoutCurrentApplicationName();

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
    public void shouldNotRegisterApplicationWhenThereIsExistingWhenDeprecatedRegisterMethodIsUsed() {
        givenApplications(applicationRepresentation().id("5").name("existingApp").type("MICROSERVICE").key("existingApp-application-key").build());

        repository.register("existingApp", microserviceMetadataRepresentation().build());

        assertThat(platform.take(byMethod(POST))).isEmpty();
    }

    @Test
    public void shouldNotRegisterApplicationWhenThereIsExistingWhenOneArgumentRegisterMethodIsUsed() {
        givenApplications(applicationRepresentation()
                .id("5")
                .name(CURRENT_APPLICATION_NAME)
                .type("MICROSERVICE")
                .key(String.format("%s-application-key", CURRENT_APPLICATION_NAME))
                .build());

        repository.register(microserviceMetadataRepresentation().build());

        assertThat(platform.take(byMethod(POST))).isEmpty();
    }

    @Test
    public void shouldNotFailOnMultipleCallsWhenOneArgumentRegisterMethodIsUsed() {
        assertThat(repository.register(microserviceMetadataRepresentation().build())).isNotNull();
        assertThat(platform.take(byMethod(POST))).isNotEmpty();

        for (int i = 0; i < 10; ++i) {
            assertThat(repository.register(microserviceMetadataRepresentation().build())).isNotNull();
            assertThat(platform.take(byMethod(POST))).isEmpty();
        }
    }

    @Test
    public void shouldNotFailOnMultipleCallsWhenDeprecatedRegisterMethodIsUsed() {
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

    private MicroserviceRepository repositoryWithoutCurrentApplicationName() {
        MockEnvironment environment = new MockEnvironment();
        return microserviceRepositoryBuilder()
                .baseUrl(Suppliers.ofInstance(BASE_URL))
                .connector(platform)
                .environment(environment)
                .username("test")
                .password("test")
                .build();
    }

}
