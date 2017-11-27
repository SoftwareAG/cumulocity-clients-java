package com.cumulocity.microservice.security.filter;

import com.cumulocity.microservice.context.credentials.MicroserviceCredentials;
import com.cumulocity.microservice.security.filter.util.HttpRequestUtils;
import com.cumulocity.microservice.security.filter.util.HttpRequestUtils.AuthorizationHeader;
import com.cumulocity.microservice.subscription.service.MicroserviceSubscriptionsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

import static com.cumulocity.sdk.client.RestConnector.X_CUMULOCITY_APPLICATION_KEY;
import static javax.ws.rs.core.HttpHeaders.AUTHORIZATION;

@Component
public class MicroserviceCredentialsProvider implements CredentailsProvider<HttpServletRequest> {

    @Autowired(required = false)
    private MicroserviceSubscriptionsService subscriptionsService;

    @Override
    public MicroserviceCredentials get(HttpServletRequest request) {
        final String authorization = request.getHeader(AUTHORIZATION);
        final String applicationKey = request.getHeader(X_CUMULOCITY_APPLICATION_KEY);
        final AuthorizationHeader from = HttpRequestUtils.from(authorization);

        return MicroserviceCredentials.builder()
                .tenant(from.getTenant())
                .username(from.getUsername())
                .password(from.getPassword())
                .appKey(applicationKey)
                .build();
    }

    @Override
    public boolean supports(Object credentialSource) {
        return credentialSource instanceof HttpServletRequest;
    }
}
