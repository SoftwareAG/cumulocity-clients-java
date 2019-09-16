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

    private CumulocityOAuthUserDetails() {
    }

    public static JwtTokenAuthentication updateTokenWithTenantAndUserDetailsUsingRequestsToCore(String baseUrl, JwtTokenAuthentication jwtTokenAuthentication) {
        ApacheHttpClient client = createClient(jwtTokenAuthentication);
        CurrentUserRepresentation currUserRepresentation = getCurrentUser(client, baseUrl);
        String tenantName = getTenantName(client, baseUrl);
        jwtTokenAuthentication.setCurrentUserRepresentation(currUserRepresentation);
        JwtTokenAuthentication updatedToken = updateUserCredentials(tenantName, jwtTokenAuthentication);
        client.destroy();
        return updatedToken;
    }

    private static ApacheHttpClient createClient(JwtTokenAuthentication jwtTokenAuthentication) {
        ApacheHttpClient client = new ApacheHttpClient();
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
        return client;
    }

    private static CurrentUserRepresentation getCurrentUser(ApacheHttpClient client, String baseUrl) {
        return client.resource(baseUrl + "/user/currentUser")
                .accept(UserMediaType.CURRENT_USER)
                .get(CurrentUserRepresentation.class);
    }

    private static String getTenantName(ApacheHttpClient client, String baseUrl) {
        SimplifiedCurrentTenantRepresentation currentTenantRepresentation = client.resource(baseUrl + "/tenant/currentTenant")
                .accept(UserMediaType.CURRENT_TENANT)
                .get(SimplifiedCurrentTenantRepresentation.class);
        return currentTenantRepresentation.name;
    }

    protected static JwtTokenAuthentication updateUserCredentials(String tenantName, JwtTokenAuthentication
            jwtTokenAuthentication) {
        UserCredentials userCredentials = buildUserCredentials(tenantName, jwtTokenAuthentication);
        jwtTokenAuthentication.setUserCredentials(userCredentials);
        return jwtTokenAuthentication;
    }

    private static UserCredentials buildUserCredentials(String tenantName, JwtTokenAuthentication jwtTokenAuthentication) {
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
