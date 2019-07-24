package com.cumulocity.microservice.subscription.repository;

import com.cumulocity.microservice.subscription.repository.application.ApplicationApiRepresentation;
import com.cumulocity.microservice.subscription.repository.impl.CurrentMicroserviceRepository;
import com.cumulocity.microservice.subscription.repository.impl.LegacyMicroserviceRepository;
import com.cumulocity.model.authentication.CumulocityBasicCredentials;
import com.cumulocity.model.authentication.CumulocityCredentials;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Supplier;
import org.springframework.core.env.Environment;
import org.springframework.core.env.StandardEnvironment;

public class MicroserviceRepositoryBuilder {

    public static final String MICROSERVICE_ISOLATION_ENV_NAME = "C8Y.microservice.isolation";

    private Supplier<String> baseUrl;
    private String tenant;
    private String username;
    private String password;
    private ObjectMapper objectMapper;
    private CredentialsSwitchingPlatform connector;
    private Environment environment;
    private String applicationName;

    /**
     * creates MicroserviceRepository implementation according to microservice isolation env variable:
     * - when C8Y.microservice.isolation is defined, then microservice runs on new SDK (8.18+) in kubernetes
     * - when C8Y.microservice.isolation not defined, then microservice should support old SDK (before 8.18)
     */
    public MicroserviceRepository build() {
        final ObjectMapper nonNullObjectMapper = objectMapper == null ? new ObjectMapper() : objectMapper;
        final CumulocityCredentials credentials = CumulocityBasicCredentials.builder()
                .username(username)
                .password(password)
                .tenantId(tenant)
                .build();
        final CredentialsSwitchingPlatform nonNullConnector = connector != null ? connector : new DefaultCredentialsSwitchingPlatform(baseUrl).switchTo(credentials);
        final ApplicationApiRepresentation api = ApplicationApiRepresentation.of(baseUrl);

        final Environment notNullEnvironment = environment == null ? new StandardEnvironment() : environment;

        if (notNullEnvironment.containsProperty(MICROSERVICE_ISOLATION_ENV_NAME)) {
            return new CurrentMicroserviceRepository(applicationName, nonNullConnector, nonNullObjectMapper, api);
        } else {
            return new LegacyMicroserviceRepository(applicationName, nonNullConnector, nonNullObjectMapper, api);
        }
    }

    private MicroserviceRepositoryBuilder() {
    }

    public static MicroserviceRepositoryBuilder microserviceRepositoryBuilder() {
        return new MicroserviceRepositoryBuilder();
    }

    public MicroserviceRepositoryBuilder baseUrl(Supplier<String> baseUrl) {
        this.baseUrl = baseUrl;
        return this;
    }

    public MicroserviceRepositoryBuilder tenant(String tenant) {
        this.tenant = tenant;
        return this;
    }

    public MicroserviceRepositoryBuilder username(String username) {
        this.username = username;
        return this;
    }

    public MicroserviceRepositoryBuilder applicationName(String applicationName){
        this.applicationName = applicationName;
        return this;
    }

    public MicroserviceRepositoryBuilder password(String password) {
        this.password = password;
        return this;
    }

    public MicroserviceRepositoryBuilder objectMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
        return this;
    }

    public MicroserviceRepositoryBuilder connector(CredentialsSwitchingPlatform connector) {
        this.connector = connector;
        return this;
    }

    public MicroserviceRepositoryBuilder environment(Environment environment) {
        this.environment = environment;
        return this;
    }

}
