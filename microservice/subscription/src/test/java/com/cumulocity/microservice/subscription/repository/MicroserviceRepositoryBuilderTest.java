package com.cumulocity.microservice.subscription.repository;

import com.cumulocity.microservice.subscription.model.core.PlatformProperties.IsolationLevel;
import com.cumulocity.microservice.subscription.repository.impl.CurrentMicroserviceRepository;
import com.cumulocity.microservice.subscription.repository.impl.LegacyMicroserviceRepository;
import org.junit.Before;
import org.junit.Test;
import org.springframework.core.env.Environment;
import org.springframework.mock.env.MockEnvironment;

import static com.cumulocity.microservice.subscription.model.core.PlatformProperties.IsolationLevel.MULTI_TENANT;
import static com.cumulocity.microservice.subscription.repository.MicroserviceRepositoryBuilder.MICROSERVICE_ISOLATION_ENV_NAME;
import static org.assertj.core.api.Assertions.assertThat;

public class MicroserviceRepositoryBuilderTest {

    private MicroserviceRepositoryBuilder builder;

    @Before
    public void setup() {
        builder = MicroserviceRepositoryBuilder.microserviceRepositoryBuilder()
        .username("test")
        .password("test");
    }

    @Test
    public void shouldCreateLegacyRepositoryWhenNoIsolationDefined() {
        builder.environment(environmentWithIsolation(null));

        MicroserviceRepository repository = builder.build();

        assertThat(repository).isInstanceOf(LegacyMicroserviceRepository.class);
    }

    @Test
    public void shouldCreateCurrentRepositoryWhenIsolationDefined() {
        builder.environment(environmentWithIsolation(MULTI_TENANT));

        MicroserviceRepository repository = builder.build();

        assertThat(repository).isInstanceOf(CurrentMicroserviceRepository.class);
    }

    private Environment environmentWithIsolation(IsolationLevel isolation) {
        MockEnvironment environment = new MockEnvironment();
        if (isolation != null) {
            environment.setProperty(MICROSERVICE_ISOLATION_ENV_NAME, isolation.toString());
        }
        return environment;
    }


}
