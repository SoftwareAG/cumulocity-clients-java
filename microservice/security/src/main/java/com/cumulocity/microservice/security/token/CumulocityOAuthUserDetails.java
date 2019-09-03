package com.cumulocity.microservice.security.token;

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

    public CumulocityOAuthUserDetails(String baseUrl, JwtTokenAuthentication jwtTokenAuthentication) {
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

    protected CurrentUserRepresentation getCurrentUser() {
        return client.resource(baseUrl + "/user/currentUser")
                .accept(UserMediaType.CURRENT_USER)
                .get(CurrentUserRepresentation.class);
    }

    protected String getTenantName() {
        SimplifiedCurrentTenantRepresentation currentTenantRepresentation = client.resource(baseUrl + "/tenant/currentTenant")
                .accept(UserMediaType.CURRENT_TENANT)
                .get(SimplifiedCurrentTenantRepresentation.class);
        return currentTenantRepresentation.name;
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
