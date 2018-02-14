package com.cumulocity.microservice.subscription.repository;

import com.cumulocity.microservice.subscription.model.MicroserviceMetadataRepresentation;
import com.cumulocity.model.authentication.CumulocityCredentials;
import com.cumulocity.rest.representation.application.ApplicationRepresentation;
import com.cumulocity.rest.representation.application.ApplicationUserCollectionRepresentation;
import com.cumulocity.rest.representation.application.ApplicationUserRepresentation;
import com.cumulocity.sdk.client.RestOperations;
import com.cumulocity.sdk.client.SDKException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Supplier;
import lombok.Builder;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;

import java.util.ArrayList;
import java.util.List;

import static com.cumulocity.model.authentication.CumulocityCredentials.Builder.cumulocityCredentials;
import static com.cumulocity.rest.representation.application.ApplicationMediaType.APPLICATION_USER_COLLECTION_MEDIA_TYPE;
import static com.cumulocity.rest.representation.application.ApplicationRepresentation.MICROSERVICE;
import static lombok.AccessLevel.PRIVATE;
import static org.apache.commons.httpclient.HttpStatus.*;

@Slf4j
@Setter(value = PRIVATE)
public class MicroserviceRepository implements EnvironmentAware {

    private Environment environment;

    interface SelfRegistration {
        ApplicationRepresentation register(String application, MicroserviceMetadataRepresentation representation);
    }

    private final CredentialsSwitchingPlatform platform;
    private final ObjectMapper objectMapper;
    private final MicroserviceApiRepresentation api;
    private boolean register;

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    public MicroserviceRepository(CredentialsSwitchingPlatform platform, ObjectMapper objectMapper, MicroserviceApiRepresentation api, boolean register) {
        this.platform = platform;
        this.objectMapper = objectMapper;
        this.api = api;
        this.register = register;
    }

    @Builder(builderMethodName = "microserviceApi")
    public static MicroserviceRepository create(
            final Supplier<String> baseUrl,
            final String tenant,
            final String username,
            final String password,
            ObjectMapper objectMapper,
            CredentialsSwitchingPlatform connector,
            boolean register) {
        return new MicroserviceRepository(connector != null ? connector :
                new DefaultCredentialsSwitchingPlatform(baseUrl)
                        .switchTo(
                                cumulocityCredentials(username, password)
                                        .withTenantId(tenant)
                                        .build()
                        ), objectMapper == null ? new ObjectMapper() : objectMapper,
                MicroserviceApiRepresentation.of(baseUrl), register);
    }

    public ApplicationRepresentation register(final String applicationName, final MicroserviceMetadataRepresentation metadata) {
        log.debug("registering {} with {}", applicationName, metadata);
        SelfRegistration registration = isAutoRegistrationEnabled() ? new AutoSelfRegistration() : new NoSelfRegistration();
        return registration.register(applicationName, metadata);

    }

    private boolean isAutoRegistrationEnabled() {
        return environment != null ? environment.getProperty("C8Y.bootstrap.register", Boolean.class, register) : register;
    }


    private class AutoSelfRegistration implements MicroserviceRepository.SelfRegistration {
        @Override
        public ApplicationRepresentation register(String applicationName, MicroserviceMetadataRepresentation metadata) {
            ApplicationRepresentation application = getApplication();
            if (application == null) {
                return createOrGet(applicationName, metadata);
            } else {
                if (!MICROSERVICE.equals(application.getType())) {
                    throw new SDKException("Cannot register application. There is another application with name \"" + application.getName() + "\"");
                }
                return updateMetadata(metadata);
            }

        }

        private ApplicationRepresentation createOrGet(String applicationName, MicroserviceMetadataRepresentation metadata) {
            ApplicationRepresentation application;
            log.info("Self registration procedure activated");
            try {
                log.info("Attempt to self register with name {}", applicationName);
                application = applicationApi().getByName(applicationName);
                if (application == null) {
                    application = new ApplicationRepresentation();
                    application.setName(applicationName);
                    application.setKey(applicationName + "-application-key");
                    application.setType(MICROSERVICE);
                    log.info("Application not registered creating {}", application);
                    application = applicationApi().create(application);
                } else {
                    log.info("Application already registered");
                }
                platform.switchTo(getBoostrapUser(application));
                return updateMetadata(metadata);
            } catch (Exception ex) {
                log.error("Self registration process failed ", ex);
                throw ex;
            }
        }


        private ApplicationRepresentation updateMetadata(MicroserviceMetadataRepresentation metadata) {
            try {
                final ApplicationRepresentation update = new ApplicationRepresentation();
                update.setRequiredRoles(metadata.getRequiredRoles());
                update.setRoles(metadata.getRoles());
                update.setUrl(metadata.getUrl());
                return applicationApi().currentApplication().update(update);
            } catch (final Exception ex) {
                return (ApplicationRepresentation) handleException("PUT", api.getCurrentApplication(), ex);
            }
        }
    }


    private class NoSelfRegistration implements MicroserviceRepository.SelfRegistration {
        @Override
        public ApplicationRepresentation register(String appliation, MicroserviceMetadataRepresentation representation) {
            log.info("Self registration procedure not active");
            final ApplicationRepresentation application = getApplication();
            if (application == null) {
                throw new SDKException("No microservice with name " + appliation + " registered. Microservice must be configured before running the SDK, please contact administrator");
            } else {
                if (!MICROSERVICE.equals(application.getType())) {
                    throw new SDKException("Cannot register application. There is another application with name \"" + application.getName() + "\"");
                }
                return application;
            }


        }

    }

    private CumulocityCredentials asCredentials(ApplicationUserRepresentation user) {
        return new CumulocityCredentials.Builder(user.getName(), user.getPassword())
                .withTenantId(user.getTenant())
                .build();
    }


    public ApplicationRepresentation getApplication() {
        try {
            return applicationApi().currentApplication().get();
        } catch (final Exception ex) {
            return (ApplicationRepresentation) handleException("GET", api.getCurrentApplication(), ex);
        }
    }

    public Iterable<ApplicationUserRepresentation> getSubscriptions(String applicationId) {
        final String url = api.getCurrentApplicationSubscriptions();
        try {
            return retrieveUsers(rest().get(url, APPLICATION_USER_COLLECTION_MEDIA_TYPE, ApplicationUserCollectionRepresentation.class));
        } catch (final Exception ex) {
            return (ApplicationUserCollectionRepresentation) handleException("GET", url, ex);
        }
    }


    public CumulocityCredentials getBoostrapUser(ApplicationRepresentation application) {
        ApplicationApi applicationApi = applicationApi();
        return asCredentials(applicationApi.getBootstrapUser(application.getId()));
    }

    private RestOperations rest() {
        return platform.get();
    }

    private ApplicationUserCollectionRepresentation retrieveUsers(ApplicationUserCollectionRepresentation result) {
        final List<ApplicationUserRepresentation> users = new ArrayList<>();
        for (final Object userMap : result.getUsers()) {
            users.add(objectMapper.convertValue(userMap, ApplicationUserRepresentation.class));
        }
        result.setUsers(users);
        return result;
    }

    private Object handleException(String method, String url, Exception ex) {
        if (ex instanceof SDKException) {
            final SDKException sdkException = (SDKException) ex;
            if (sdkException.getHttpStatus() == SC_FORBIDDEN || sdkException.getHttpStatus() == SC_UNAUTHORIZED) {
                log.warn("User has no permission to api " + method + " " + url);
                log.debug("Details :" ,ex );
                return null;
            } else if (sdkException.getHttpStatus() == SC_NOT_FOUND) {
                log.warn("Not found " + method + " " + url + "(" + ex.getMessage() + ")");
                log.debug("Details :" ,ex );
                return null;
            }
        }
        throw new SDKException("Error invoking " + method + " " + url, ex);
    }


    private ApplicationApi applicationApi() {
        return new ApplicationApi(platform.get(), api);
    }
}
