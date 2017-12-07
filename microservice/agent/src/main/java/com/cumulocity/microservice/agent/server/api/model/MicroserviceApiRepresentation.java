package com.cumulocity.microservice.agent.server.api.model;

import com.cumulocity.rest.representation.AbstractExtensibleRepresentation;
import lombok.*;

import static java.lang.String.valueOf;

@Data
@Builder(builderMethodName = "microserviceApiRepresentation")
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class MicroserviceApiRepresentation extends AbstractExtensibleRepresentation {
    public final static String APPLICATION_ID = "{{applicationId}}";
    public final static String APPLICATION_NAME = "{{applicationName}}";

    private String updateUrl;
    private String subscriptionsUrl;
    private String getUrl;

    public String getAppUrl(String baseUrl) {
        return url(baseUrl, getGetUrl(), null, null);
    }

    public String getSubscriptionsUrl(String baseUrl) {
        return url(baseUrl, getSubscriptionsUrl(), null, null);
    }

    public String getUpdateUrl(String baseUrl, String name, String id) {
        return url(baseUrl, getUpdateUrl(), name, id);
    }

    private static String url(@NonNull String baseUrl, @NonNull String url, String applicationName, String applicationId) {
        return baseUrl + url.replace(APPLICATION_NAME, valueOf(applicationName)).replace(APPLICATION_ID, valueOf(applicationId));
    }
}
