package com.cumulocity.microservice.agent.server.api.service;

import com.cumulocity.microservice.agent.server.api.model.MicroserviceApiRepresentation;
import com.cumulocity.rest.representation.application.ApplicationCollectionRepresentation;
import com.cumulocity.rest.representation.application.ApplicationRepresentation;
import com.cumulocity.rest.representation.application.ApplicationUserRepresentation;
import com.cumulocity.sdk.client.RestOperations;
import com.cumulocity.sdk.client.SDKException;
import lombok.extern.slf4j.Slf4j;

import static com.cumulocity.rest.representation.application.ApplicationMediaType.APPLICATION;
import static com.cumulocity.rest.representation.application.ApplicationMediaType.APPLICATION_COLLECTION;
import static com.cumulocity.rest.representation.user.UserMediaType.USER;
import static org.apache.commons.httpclient.HttpStatus.SC_FORBIDDEN;
import static org.apache.commons.httpclient.HttpStatus.SC_NOT_FOUND;
import static org.apache.commons.httpclient.HttpStatus.SC_UNAUTHORIZED;

@Slf4j
final class ApplicationApi {
    private MicroserviceApiRepresentation microserviceApi;
    private final RestOperations rest;

    public ApplicationApi(RestOperations rest, MicroserviceApiRepresentation microserviceApi) {
        this.microserviceApi = microserviceApi;
        this.rest = rest;
    }

    public ApplicationCollectionRepresentation list() {
        return rest.get(microserviceApi.getCollectionUrl(), APPLICATION_COLLECTION, ApplicationCollectionRepresentation.class);
    }


    public ApplicationRepresentation getByName(String name) {
        final String url = microserviceApi.getFindByNameUrl(name);
        try {
            final ApplicationCollectionRepresentation response = rest.get(url, APPLICATION_COLLECTION, ApplicationCollectionRepresentation.class);
            if (response.getApplications().size() == 1) {
                return response.getApplications().get(0);
            }
            return null;
        } catch (final Exception ex) {
            return (ApplicationRepresentation) handleException("GET", url, ex);
        }
    }

    public ApplicationRepresentation create(ApplicationRepresentation application) {
        final String url = microserviceApi.getCollectionUrl();
        try {

            return rest.post(url, APPLICATION, application);
        } catch (final Exception ex) {
            return (ApplicationRepresentation) handleException("POST", url, ex);
        }
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

    public ApplicationUserRepresentation getBootstrapUser(String applicationId) {
        return rest.get(microserviceApi.getBootstrapUserUrl(applicationId), USER, ApplicationUserRepresentation.class);
    }

}
