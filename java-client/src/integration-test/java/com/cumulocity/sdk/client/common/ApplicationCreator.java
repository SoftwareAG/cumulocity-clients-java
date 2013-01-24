package com.cumulocity.sdk.client.common;

import java.util.List;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.springframework.beans.factory.annotation.Autowired;

import com.cumulocity.model.JSONBase;
import com.cumulocity.rest.representation.application.ApplicationCollectionRepresentation;
import com.cumulocity.rest.representation.application.ApplicationRepresentation;
import com.cumulocity.sdk.client.PlatformImpl;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

public class ApplicationCreator {
    private static final String APPLICATIONS_URI = "application/applications";
    private static final String APPLICATION_MIME_TYPE = "application/vnd.com.nsn.cumulocity.application+json;charset=UTF-8;ver=0.9";
    public static final String APPLICATIONS_BY_NAME_URI = "application/applicationsByName/";

    private String applicationId;
    private final PlatformImpl platform;
    private final String appName;
    private final String host;

    @Autowired
    public ApplicationCreator(PlatformImpl platform) {
        this.platform = platform;
        appName = platform.getTenantId() + "_application";
        host = platform.getHost();
    }

    public String getApplicationId() {
        return applicationId;
    }

    public void createApplication() {
        Client httpClient = new HttpClientFactory().createClient();
        try {
            createApplication(httpClient);
        } finally {
            httpClient.destroy();
        }
    }

    private void createApplication(Client httpClient) {
        ClientResponse applicationResponse = postNewApplication(httpClient);
        MatcherAssert.assertThat(applicationResponse.getStatus(), Matchers.is(201));
        applicationId = parseApplicationId(applicationResponse);
    }

    public void removeApplication() {
        Client httpClient = new HttpClientFactory().createClient();
        try {
            removeApplication(httpClient);
        } finally {
            httpClient.destroy();
        }
    }

    private void removeApplication(Client httpClient) {
        String appId = getApplicationId(httpClient, appName);
        removeApplication(httpClient, appId);
    }

    private void removeApplication(Client httpClient, String appId) {
        WebResource applicationResource = httpClient.resource(host + APPLICATIONS_URI + "/" + appId);
        ClientResponse applicationResponse = applicationResource.delete(ClientResponse.class);
        MatcherAssert.assertThat(applicationResponse.getStatus(), Matchers.is(204));
    }

    private ClientResponse postNewApplication(Client httpClient) {
        String host = platform.getHost();
        WebResource applicationApi = httpClient.resource(host + APPLICATIONS_URI);
        String applicationJson = "{\"name\" : \"" + appName + "\", " +
                "\"key\" : \"" + platform.getApplicationKey() + "\"}";
        return applicationApi.type(APPLICATION_MIME_TYPE).accept(APPLICATION_MIME_TYPE).post(ClientResponse.class, applicationJson);
    }

    private String getApplicationId(Client httpClient, String appName) {
        List<ApplicationRepresentation> applications = getApplicationsByName(httpClient, appName);
        return applications.get(0).getId();
    }

    private List<ApplicationRepresentation> getApplicationsByName(Client httpClient, String appName) {
        String host = platform.getHost();
        ClientResponse apps = httpClient.resource(host + APPLICATIONS_BY_NAME_URI + appName).get(ClientResponse.class);
        String body = apps.getEntity(String.class);
        ApplicationCollectionRepresentation appsRep = JSONBase.fromJSON(body, ApplicationCollectionRepresentation.class);
        return appsRep.getApplications();
    }

    private String parseApplicationId(ClientResponse applicationResponse) {
        String body = applicationResponse.getEntity(String.class);
        ApplicationRepresentation representation = JSONBase.fromJSON(body, ApplicationRepresentation.class);
        return representation.getId();
    }

}