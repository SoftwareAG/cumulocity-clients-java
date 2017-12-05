package com.cumulocity.microservice.subscription.repository;

import com.cumulocity.rest.representation.application.ApplicationRepresentation;
import com.cumulocity.rest.representation.application.ApplicationUserCollectionRepresentation;
import com.cumulocity.rest.representation.application.ApplicationUserRepresentation;
import com.cumulocity.rest.representation.microservice.MicroserviceMetadataRepresentation;
import com.cumulocity.sdk.client.RestOperations;
import com.cumulocity.sdk.client.SDKException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.List;

import static com.cumulocity.rest.representation.application.ApplicationMediaType.APPLICATION;
import static com.cumulocity.rest.representation.application.ApplicationMediaType.APPLICATION_USER_COLLECTION_MEDIA_TYPE;
import static com.cumulocity.rest.representation.application.ApplicationRepresentation.MICROSERVICE;
import static org.apache.commons.httpclient.HttpStatus.*;

public class MicroserviceRepository {

    private static final Logger log = org.slf4j.LoggerFactory.getLogger(MicroserviceRepository.class);
    private final Supplier<String> baseUrl;
    private final Supplier<RestOperations> platform;
    private final ObjectMapper objectMapper;
    private final boolean register;
    private final MicroserviceApiRepresentation microserviceApi;

    @java.beans.ConstructorProperties({"baseUrl", "platform", "objectMapper", "register", "microserviceApi"})
    public MicroserviceRepository(Supplier<String> baseUrl, Supplier<RestOperations> platform, ObjectMapper objectMapper, boolean register, MicroserviceApiRepresentation microserviceApi) {
        this.baseUrl = baseUrl;
        this.platform = platform;
        this.objectMapper = objectMapper;
        this.register = register;
        this.microserviceApi = microserviceApi;
    }

    public static MicroserviceRepository create(
            final Supplier<String> baseUrl,
            ObjectMapper objectMapper,
            Supplier<RestOperations> connector,
            final boolean register) {
        return new MicroserviceRepository(baseUrl, Suppliers.memoize(connector), objectMapper == null ? new ObjectMapper() : objectMapper, register, MicroserviceApiRepresentation.microserviceApiRepresentation()
                .updateUrl("/application/currentApplication")
                .subscriptionsUrl("/application/currentApplication/subscriptions")
                .getUrl("/application/currentApplication")
                .build());
    }

    public static MicroserviceRepositoryBuilder microserviceApi() {
        return new MicroserviceRepositoryBuilder();
    }

    public ApplicationRepresentation register(String applicationName, MicroserviceMetadataRepresentation representation) {
        log.debug("register {}", representation);

        final ApplicationRepresentation application = getApplication();
        if (application == null) {
            // new micro-service SDK approach requires application configured
            throw new SDKException("No microservice with name " + applicationName + " registered. Microservice must be configured before running the SDK, please contact administrator");
        } else if (shouldUpdateApplication()) {
            if (!MICROSERVICE.equals(application.getType())) {
                throw new SDKException("Cannot register application. There is another application with name \"" + applicationName + "\"");
            }
            return update(application, representation);
        } else {
            return application;
        }
    }

    private boolean shouldUpdateApplication() {
        return register;
    }

    public ApplicationRepresentation getApplication() {
        final String url = microserviceApi.getAppUrl(baseUrl.get());
        try {
            return rest().get(url, APPLICATION, ApplicationRepresentation.class);
        } catch (final Exception ex) {
            return (ApplicationRepresentation) handleException("GET", url, ex);
        }
    }

    public Iterable<ApplicationUserRepresentation> getSubscriptions(String applicationId) {
        final String url = microserviceApi.getSubscriptionsUrl(baseUrl.get());
        try {
            return retrieveUsers(rest().get(url, APPLICATION_USER_COLLECTION_MEDIA_TYPE, ApplicationUserCollectionRepresentation.class));
        } catch (final Exception ex) {
            return (ApplicationUserCollectionRepresentation) handleException("GET", url, ex);
        }
    }

    private ApplicationRepresentation update(ApplicationRepresentation source, MicroserviceMetadataRepresentation representation) {
        final String name = source.getName();
        final String id = source.getId();
        final String url = microserviceApi.getUpdateUrl(baseUrl.get(), name, id);
        try {
            final ApplicationRepresentation application = new ApplicationRepresentation();
            application.setRequiredRoles(representation.getRequiredRoles());
            application.setRoles(representation.getRoles());
            application.setUrl(representation.getUrl());
            return rest().put(url, APPLICATION, application);
        } catch (final Exception ex) {
            return (ApplicationRepresentation) handleException("PUT", url, ex);
        }
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
                return null;
            } else if (sdkException.getHttpStatus() == SC_NOT_FOUND) {
                return null;
            }
        }
        throw new SDKException("Error invoking " + method + " " + url, ex);
    }

    public static class MicroserviceRepositoryBuilder {
        private Supplier<String> baseUrl;
        private ObjectMapper objectMapper;
        private Supplier<RestOperations> connector;
        private boolean register;

        MicroserviceRepositoryBuilder() {
        }

        public MicroserviceRepository.MicroserviceRepositoryBuilder baseUrl(Supplier<String> baseUrl) {
            this.baseUrl = baseUrl;
            return this;
        }

        public MicroserviceRepository.MicroserviceRepositoryBuilder objectMapper(ObjectMapper objectMapper) {
            this.objectMapper = objectMapper;
            return this;
        }

        public MicroserviceRepository.MicroserviceRepositoryBuilder connector(Supplier<RestOperations> connector) {
            this.connector = connector;
            return this;
        }

        public MicroserviceRepository.MicroserviceRepositoryBuilder register(boolean register) {
            this.register = register;
            return this;
        }

        public MicroserviceRepository build() {
            return MicroserviceRepository.create(baseUrl, objectMapper, connector, register);
        }

        public String toString() {
            return "MicroserviceRepository.MicroserviceRepositoryBuilder(baseUrl=" + this.baseUrl + ", objectMapper=" + this.objectMapper + ", connector=" + this.connector + ", register=" + this.register + ")";
        }
    }
}
