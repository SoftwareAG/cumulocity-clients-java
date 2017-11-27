package com.cumulocity.microservice.security.filter;

import com.cumulocity.microservice.context.credentials.UserCredentials;
import com.cumulocity.microservice.security.filter.util.HttpRequestUtils;
import com.cumulocity.microservice.security.filter.util.HttpRequestUtils.AuthorizationHeader;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

import static com.cumulocity.sdk.client.RestConnector.X_CUMULOCITY_APPLICATION_KEY;
import static javax.ws.rs.core.HttpHeaders.AUTHORIZATION;

@Component
public class UserCredentialsProvider implements CredentailsProvider<HttpServletRequest> {
    @Override
    public UserCredentials get(HttpServletRequest request) {
        final String authorization = request.getHeader(AUTHORIZATION);
        final String applicationKey = request.getHeader(X_CUMULOCITY_APPLICATION_KEY);
        final AuthorizationHeader from = HttpRequestUtils.from(authorization);

        return UserCredentials.builder()
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
