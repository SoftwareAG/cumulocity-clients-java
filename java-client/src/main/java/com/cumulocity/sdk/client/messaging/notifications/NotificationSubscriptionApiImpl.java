package com.cumulocity.sdk.client.messaging.notifications;

import com.cumulocity.rest.representation.CumulocityMediaType;
import com.cumulocity.rest.representation.event.EventRepresentation;
import com.cumulocity.rest.representation.reliable.notification.NotificationSubscriptionRepresentation;

import com.cumulocity.sdk.client.PlatformParameters;
import com.cumulocity.sdk.client.RestConnector;
import com.cumulocity.sdk.client.SDKException;
import com.cumulocity.sdk.client.UrlProcessor;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import static java.util.Objects.requireNonNull;

@Slf4j
@RequiredArgsConstructor
public class NotificationSubscriptionApiImpl implements NotificationSubscriptionApi {
    
    public static final CumulocityMediaType MEDIA_TYPE = new CumulocityMediaType("application", "json");

    public static final String REQUEST_URI = "reliablenotification/subscriptions";

    private final PlatformParameters platformParameters;
    private final RestConnector restConnector;

    private final int pageSize;

    private UrlProcessor urlProcessor;

    public NotificationSubscriptionApiImpl(
            PlatformParameters platformParameters,
            RestConnector restConnector,
            UrlProcessor urlProcessor,
            int pageSize) {
        this.restConnector = restConnector;
        this.urlProcessor = urlProcessor;
        this.pageSize = pageSize;
        this.platformParameters = platformParameters;
    }
    
    @Override
    public NotificationSubscriptionRepresentation subscribe(NotificationSubscriptionRepresentation description) throws SDKException{
        requireNonNull(description, "description");
        NotificationSubscriptionRepresentation result = restConnector.post(
                getSelfUri(),
                MEDIA_TYPE,
                description
                );
        return result;
    }

    @Override
    public NotificationSubscriptionCollection getSubscriptions() throws SDKException {
        String url = getSelfUri();
        return new NotificationSubscriptionCollectionImpl(restConnector, url, pageSize);
    }

    private String getSelfUri() throws SDKException {
        return platformParameters.getHost() + REQUEST_URI;
    }

    @Override
    public NotificationSubscriptionCollection getSubscriptionsByFilter(NotificationSubscriptionFilter filter) throws SDKException {
        if (filter == null) {
            return getSubscriptions();
        }
        Map<String, String> params = filter.getQueryParams();
        return new NotificationSubscriptionCollectionImpl(restConnector, urlProcessor.replaceOrAddQueryParam(getSelfUri(), params), pageSize);
    }

    @Override
    public void delete(NotificationSubscriptionRepresentation subscription) throws SDKException {
        String url = getSelfUri() + "/" + subscription.getId().getValue();
        restConnector.delete(url);
    }
}
