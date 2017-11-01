package com.cumulocity.microservice.agent.server.api.service;

import com.cumulocity.microservice.agent.server.api.model.MicroserviceApiRepresentation;
import com.cumulocity.model.authentication.CumulocityCredentials;
import com.cumulocity.rest.representation.application.ApplicationCollectionRepresentation;
import com.cumulocity.rest.representation.application.ApplicationRepresentation;
import com.cumulocity.rest.representation.application.ApplicationUserCollectionRepresentation;
import com.cumulocity.rest.representation.application.ApplicationUserRepresentation;
import com.cumulocity.rest.representation.microservice.MicroserviceMetadataRepresentation;
import com.cumulocity.sdk.client.PlatformImpl;
import com.cumulocity.sdk.client.RestConnector;
import com.cumulocity.sdk.client.SDKException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Supplier;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

import static com.cumulocity.microservice.agent.server.api.model.MicroserviceApiRepresentation.APPLICATION_ID;
import static com.cumulocity.microservice.agent.server.api.model.MicroserviceApiRepresentation.APPLICATION_NAME;
import static com.cumulocity.rest.representation.application.ApplicationMediaType.*;
import static com.cumulocity.rest.representation.application.ApplicationRepresentation.MICROSERVICE;
import static lombok.AccessLevel.PRIVATE;
import static org.apache.commons.httpclient.HttpStatus.*;

@Slf4j
@Setter(value = PRIVATE)
@RequiredArgsConstructor
public class MicroserviceRepository {

    private final Supplier<String> baseUrl;
    private final PlatformImpl platform;
    private final ObjectMapper objectMapper;
    private final MicroserviceApiRepresentation microserviceApi;

    @Builder(builderMethodName = "microserviceApi")
    public static MicroserviceRepository create(
            ObjectMapper objectMapper,
            Supplier<String> baseUrlSupplier,
            final String baseUrl,
            final String tenant,
            final String user,
            final String password) {
        if (baseUrl != null) {
            baseUrlSupplier = new Supplier<String>() {
                @Override
                public String get() {
                    return baseUrl;
                }
            };
        }

        if (objectMapper == null) {
            objectMapper = new ObjectMapper();
        }

        final CumulocityCredentials credentials = initCredentials(user, password, tenant);
        final PlatformImpl platform = new PlatformImpl(baseUrlSupplier.get(), credentials);
        return new MicroserviceRepository(baseUrlSupplier, platform, objectMapper, MicroserviceApiRepresentation.microserviceApiRepresentation()
                .createUrl("/application/applications")
                .updateUrl("/application/applications/" + APPLICATION_ID)
                .subscriptionsUrl("/application/applications/" + APPLICATION_ID + "/subscriptions")
                .findByNameUrl("/application/applicationsByName/" + APPLICATION_NAME)
                .build());
    }

    public ApplicationRepresentation register(String applicationName, MicroserviceMetadataRepresentation representation) {
        log.debug("register {}", representation);

        final ApplicationRepresentation application = getByName(applicationName);
        if (application == null) {
            return create(applicationName, representation);
        } else {
            if (!MICROSERVICE.equals(application.getType())) {
                throw new SDKException("Cannot register application. There is another application with name \""  + applicationName + "\"");
            }
            return update(application, representation);
        }
    }

    public ApplicationRepresentation getByName(String applicationName) {
        final String url = microserviceApi.getFindByNameUrl(baseUrl.get(), applicationName);
        try {
            final ApplicationCollectionRepresentation response = rest().get(url, APPLICATION_COLLECTION, ApplicationCollectionRepresentation.class);
            if (response.getApplications().size() == 1) {
                return response.getApplications().get(0);
            }
            return null;
        } catch (final Exception ex) {
            return (ApplicationRepresentation) handleException("GET", url, ex);
        }
    }

    public Iterable<ApplicationUserRepresentation> getSubscriptions(String applicationId) {
        final String url = microserviceApi.getSubscriptionsUrl(baseUrl.get(), applicationId);
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

    public ApplicationRepresentation create(String applicationName, MicroserviceMetadataRepresentation representation) {
        final String url = microserviceApi.getCreateUrl(baseUrl.get());
        try {
            final ApplicationRepresentation application = new ApplicationRepresentation();
            application.setName(applicationName);
            application.setKey(applicationName + "-application-key");
            application.setType(MICROSERVICE);
            application.setRequiredRoles(representation.getRequiredRoles());
            application.setRoles(representation.getRoles());
            application.setUrl(representation.getUrl());
            return rest().post(url, APPLICATION, application);
        } catch (final Exception ex) {
            return (ApplicationRepresentation) handleException("POST", url, ex);
        }
    }

    private RestConnector rest() {
        return platform.createRestConnector();
    }

    private ApplicationUserCollectionRepresentation retrieveUsers(ApplicationUserCollectionRepresentation result) {
        final List<ApplicationUserRepresentation> users =  new ArrayList<>();
        for (final Object userMap : result.getUsers()) {
            users.add(objectMapper.convertValue(userMap, ApplicationUserRepresentation.class));
        }
        result.setUsers(users);
        return result;
    }

    @Nullable
    private Object handleException(String method, String url, Exception ex) {
        if (ex instanceof SDKException) {
            final SDKException sdkException = (SDKException) ex;
            if (sdkException.getHttpStatus() == SC_FORBIDDEN ||  sdkException.getHttpStatus() == SC_UNAUTHORIZED) {
                log.warn("User has no permission to api " + method + " " + url);
                return null;
            } else if (sdkException.getHttpStatus() == SC_NOT_FOUND) {
                return null;
            }
        }
        throw new SDKException("Error invoking " + method + " " + url, ex);
    }

    @Nonnull
    private static CumulocityCredentials initCredentials(final String username, String password, String tenant) {
        return new CumulocityCredentials.Builder(username, password).withTenantId(tenant).build();
    }
}
