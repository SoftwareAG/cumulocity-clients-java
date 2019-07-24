package com.cumulocity.microservice.subscription.repository.impl;

import com.cumulocity.microservice.subscription.model.MicroserviceMetadataRepresentation;
import com.cumulocity.microservice.subscription.repository.CredentialsSwitchingPlatform;
import com.cumulocity.microservice.subscription.repository.MicroserviceRepository;
import com.cumulocity.microservice.subscription.repository.application.ApplicationApi;
import com.cumulocity.microservice.subscription.repository.application.ApplicationApiRepresentation;
import com.cumulocity.model.authentication.CumulocityBasicCredentials;
import com.cumulocity.model.authentication.CumulocityCredentials;
import com.cumulocity.rest.representation.application.ApplicationRepresentation;
import com.cumulocity.rest.representation.application.ApplicationUserCollectionRepresentation;
import com.cumulocity.rest.representation.application.ApplicationUserRepresentation;
import com.cumulocity.sdk.client.RestOperations;
import com.cumulocity.sdk.client.SDKException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

import static com.cumulocity.rest.representation.application.ApplicationMediaType.APPLICATION_USER_COLLECTION_MEDIA_TYPE;
import static com.cumulocity.rest.representation.application.ApplicationRepresentation.MICROSERVICE;
import static com.google.common.base.Preconditions.checkState;
import static org.apache.commons.httpclient.HttpStatus.*;
import static org.apache.commons.lang.StringUtils.isBlank;
import static org.apache.commons.lang.StringUtils.isNotBlank;

/**
 * works OK with platform API older than 8.18
 */
@Slf4j
public class LegacyMicroserviceRepository implements MicroserviceRepository {

    private final CredentialsSwitchingPlatform platform;
    private final ObjectMapper objectMapper;
    private final ApplicationApiRepresentation api;
    private final String applicationName;

    public LegacyMicroserviceRepository(String applicationName, CredentialsSwitchingPlatform platform, ObjectMapper objectMapper, ApplicationApiRepresentation api) {
        if(isBlank(applicationName)){
            log.warn("Current application name was not provided to LegacyMicroserviceRepository. Please correct LegacyMicroserviceRepository usage in your code.");
        }
        this.applicationName = applicationName;
        this.platform = platform;
        this.objectMapper = objectMapper;
        this.api = api;
    }

    @Deprecated
    public LegacyMicroserviceRepository(CredentialsSwitchingPlatform platform, ObjectMapper objectMapper, ApplicationApiRepresentation api) {
        this(null, platform, objectMapper, api);
    }

    @Override
    public ApplicationRepresentation register(final MicroserviceMetadataRepresentation metadata) {
        checkState(isNotBlank(applicationName),
                "Application name must be provided at construction time to use one argument register method.");
        return register(applicationName, metadata);
    }

    @Override
    @Deprecated
    public ApplicationRepresentation register(final String applicationName, final MicroserviceMetadataRepresentation metadata) {
        log.debug("registering {} with {}", applicationName, metadata);
        final ApplicationRepresentation application = getByName(applicationName);
        if (application == null) {
            return create(applicationName, metadata);
        } else {
            if (!MICROSERVICE.equals(application.getType())) {
                throw new SDKException("Cannot register application. There is another application with name \"" + applicationName + "\"");
            }
            return update(application, metadata);
        }
    }

    @Override
    public ApplicationRepresentation getCurrentApplication() {
        Preconditions.checkState(isNotBlank(applicationName), "You need to provide current application name at construction time to use method getCurrentApplication.");
        return getByName(applicationName);
    }

    @Override
    public Iterable<ApplicationUserRepresentation> getSubscriptions(String applicationId) {
        String url = api.getApplicationSubscriptions(applicationId);
        try {
            return retrieveUsers(rest().get(url, APPLICATION_USER_COLLECTION_MEDIA_TYPE, ApplicationUserCollectionRepresentation.class));
        } catch (final Exception ex) {
            return (ApplicationUserCollectionRepresentation) handleException("GET", url, ex);
        }
    }

    private ApplicationRepresentation getByName(String applicationName) {
        try {
            Optional<ApplicationRepresentation> byName = applicationApi().getByName(applicationName);
            return byName.orNull();
        } catch (final Exception ex) {
            return (ApplicationRepresentation) handleException("GET", api.getFindByNameUrl(applicationName), ex);
        }
    }

    private ApplicationRepresentation create(String applicationName, MicroserviceMetadataRepresentation representation) {
        try {
            final ApplicationRepresentation application = new ApplicationRepresentation();
            application.setName(applicationName);
            application.setKey(applicationName + "-application-key");
            application.setType(MICROSERVICE);
            application.setRequiredRoles(representation.getRequiredRoles());
            application.setRoles(representation.getRoles());
            application.setUrl(representation.getUrl());
            return applicationApi().create(application);
        } catch (final Exception ex) {
            return (ApplicationRepresentation) handleException("POST", api.getCollectionUrl(), ex);
        }
    }

    private ApplicationRepresentation update(ApplicationRepresentation source, MicroserviceMetadataRepresentation metadata) {
        try {
            final ApplicationRepresentation application = new ApplicationRepresentation();
            application.setId(source.getId());
            application.setRequiredRoles(metadata.getRequiredRoles());
            application.setRoles(metadata.getRoles());
            application.setUrl(metadata.getUrl());
            return applicationApi().update(application);
        } catch (final Exception ex) {
            return (ApplicationRepresentation) handleException("PUT", api.getByIdUrl(source.getId()), ex);
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
                log.debug("Details :", ex);
                return null;
            } else if (sdkException.getHttpStatus() == SC_NOT_FOUND) {
                log.warn("Not found " + method + " " + url + "(" + ex.getMessage() + ")");
                log.debug("Details :", ex);
                return null;
            }
        }
        throw new SDKException("Error invoking " + method + " " + url, ex);
    }

    private CumulocityCredentials asCredentials(ApplicationUserRepresentation user) {
        return CumulocityBasicCredentials.builder()
                .username(user.getName())
                .password(user.getPassword())
                .tenantId(user.getTenant())
                .build();
    }

    private ApplicationApi applicationApi() {
        return new ApplicationApi(platform.get(), api);
    }
}
