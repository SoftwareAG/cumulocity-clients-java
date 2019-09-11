package com.cumulocity.microservice.security.token;

import com.cumulocity.microservice.context.credentials.UserCredentials;
import com.cumulocity.model.authentication.AuthenticationMethod;
import com.cumulocity.model.authentication.CumulocityOAuthCredentials;
import com.cumulocity.rest.representation.user.CurrentUserRepresentation;
import com.cumulocity.rest.representation.user.UserMediaType;
import com.cumulocity.sdk.client.CumulocityAuthenticationFilter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.sun.jersey.client.apache.ApacheHttpClient;

public class CumulocityOAuthUserDetails {
    private ApacheHttpClient client;
    private String baseUrl;
    private JwtTokenAuthentication jwtTokenAuthentication;

    public CumulocityOAuthUserDetails(String baseUrl, JwtTokenAuthentication jwtTokenAuthentication) {
        this.jwtTokenAuthentication = jwtTokenAuthentication;
        this.client = new ApacheHttpClient();
        if (jwtTokenAuthentication != null) {
            if (jwtTokenAuthentication.getCredentials() instanceof JwtAndXsrfTokenCredentials) {
                JwtAndXsrfTokenCredentials credentials = (JwtAndXsrfTokenCredentials) jwtTokenAuthentication.getCredentials();
                client.addFilter(new CumulocityAuthenticationFilter(
                        CumulocityOAuthCredentials.builder()
                                .authenticationMethod(AuthenticationMethod.COOKIE)
                                .oAuthAccessToken(credentials.getJwt().getEncoded())
                                .xsrfToken(credentials.getXsrfToken())
                                .build()
                ));
            } else {
                client.addFilter(new CumulocityAuthenticationFilter(
                        CumulocityOAuthCredentials.builder()
                                .authenticationMethod(AuthenticationMethod.HEADER)
                                .oAuthAccessToken((jwtTokenAuthentication.getCredentials()).getJwt().getEncoded())
                                .build()
                ));
            }
        }
        this.baseUrl = baseUrl;
    }

    public JwtTokenAuthentication updateTokenWithTenantAndUserDetailsUsingRequestsToCore() {
        CurrentUserRepresentation currUserRepresentation = getCurrentUser();
        String tenantName = getTenantName();

        jwtTokenAuthentication.setCurrentUserRepresentation(currUserRepresentation);
        return updateUserCredentials(tenantName, jwtTokenAuthentication);
    }

    private CurrentUserRepresentation getCurrentUser() {
        return client.resource(baseUrl + "/user/currentUser")
                .accept(UserMediaType.CURRENT_USER)
                .get(CurrentUserRepresentation.class);
    }

    private String getTenantName() {
        SimplifiedCurrentTenantRepresentation currentTenantRepresentation = client.resource(baseUrl + "/tenant/currentTenant")
                .accept(UserMediaType.CURRENT_TENANT)
                .get(SimplifiedCurrentTenantRepresentation.class);
        return currentTenantRepresentation.name;
    }

    protected JwtTokenAuthentication updateUserCredentials(String tenantName, JwtTokenAuthentication jwtTokenAuthentication) {
        UserCredentials userCredentials = buildUserCredentials(tenantName, jwtTokenAuthentication);
        jwtTokenAuthentication.setUserCredentials(userCredentials);
        return jwtTokenAuthentication;
    }

    private UserCredentials buildUserCredentials(String tenantName, JwtTokenAuthentication jwtTokenAuthentication) {
        JwtCredentials jwtCredentials = jwtTokenAuthentication.getCredentials();
        return jwtCredentials.toUserCredentials(tenantName, jwtTokenAuthentication);
    }


    @JsonIgnoreProperties(ignoreUnknown = true)
    protected static class SimplifiedCurrentTenantRepresentation {
        private String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
