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
import static org.apache.commons.httpclient.HttpStatus.*;

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
        final ApplicationCollectionRepresentation response = rest.get(microserviceApi.getFindByNameUrl(name), APPLICATION_COLLECTION, ApplicationCollectionRepresentation.class);
        if (response.getApplications().size() == 1) {
            return response.getApplications().get(0);
        }
        return null;
    }

    public ApplicationRepresentation create(ApplicationRepresentation application) {
        return rest.post(microserviceApi.getCollectionUrl(), APPLICATION, application);
    }


    public ApplicationUserRepresentation getBootstrapUser(String applicationId) {
        return rest.get(microserviceApi.getBootstrapUserUrl(applicationId), USER, ApplicationUserRepresentation.class);
    }

    public ApplicationRepresentation update(ApplicationRepresentation update) {
        return rest.put(microserviceApi.getByIdUrl(update.getId()), APPLICATION, update);
    }

    public CurrentApplicationApi currentApplication() {
        return new CurrentApplicationApi(rest, microserviceApi);
    }
}
