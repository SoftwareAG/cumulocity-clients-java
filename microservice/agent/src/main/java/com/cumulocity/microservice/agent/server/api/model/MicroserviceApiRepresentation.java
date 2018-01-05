package com.cumulocity.microservice.agent.server.api.model;

import com.cumulocity.rest.representation.AbstractExtensibleRepresentation;
import com.google.common.base.Supplier;
import lombok.*;

import static java.lang.String.valueOf;


@Builder(builderMethodName = "microserviceApiRepresentation")
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class MicroserviceApiRepresentation extends AbstractExtensibleRepresentation {
    public final static String APPLICATION_ID = "{{applicationId}}";
    public final static String APPLICATION_NAME = "{{applicationName}}";

    public static MicroserviceApiRepresentation of(Supplier<String> baseUrl) {
       return  microserviceApiRepresentation()
                .baseUrl(baseUrl)
                .collectionUrl("/application/applications")
                .currentApplicationSubscriptions("/application/currentApplication/subscriptions")
                .currentApplication("/application/currentApplication")
                .findByNameUrl("/application/applicationsByName/" + APPLICATION_NAME)
                .getById("/application/applications/" + APPLICATION_ID)
                .bootstrapUserUrl("/application/applications/" + APPLICATION_ID + "/bootstrapUser")
                .build();

    }

    private final Supplier<String> baseUrl;
    private final String updateUrl;
    private final String currentApplicationSubscriptions;
    private final String currentApplication;
    private final String collectionUrl;
    private final String getById;
    private final String findByNameUrl;
    private final String bootstrapUserUrl;

    public String getCollectionUrl() {
        return url(collectionUrl, null, null);
    }

    public String getFindByNameUrl(String applicationName) {
        return url(findByNameUrl, applicationName, null);
    }

    public String getCurrentApplication() {
        return url(currentApplication, null, null);
    }

    public String getCurrentApplicationSubscriptions() {
        return url(currentApplicationSubscriptions, null, null);
    }


    public String getByIdUrl(String id) {
        return url(getById, null, id);
    }

    public String getBootstrapUserUrl(String id) {
        return url(bootstrapUserUrl, null, id);
    }

    private String url(@NonNull String url, String applicationName, String applicationId) {
        return baseUrl.get() + url.replace(APPLICATION_NAME, valueOf(applicationName)).replace(APPLICATION_ID, valueOf(applicationId));
    }
}
