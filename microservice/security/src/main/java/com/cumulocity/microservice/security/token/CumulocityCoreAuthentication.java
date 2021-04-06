package com.cumulocity.microservice.security.token;

import com.cumulocity.microservice.context.credentials.UserCredentials;
import com.cumulocity.model.authentication.AuthenticationMethod;
import com.cumulocity.model.authentication.CumulocityOAuthCredentials;
import com.cumulocity.rest.representation.user.CurrentUserRepresentation;
import com.cumulocity.rest.representation.user.UserMediaType;
import com.cumulocity.sdk.client.CumulocityAuthenticationFilter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;


class CumulocityCoreAuthentication {

    private CumulocityCoreAuthentication() {
    }

    static JwtTokenAuthentication authenticateUserAndUpdateToken(String baseUrl, JwtTokenAuthentication jwtTokenAuthentication) {
        Client client = createClientWithAuthenticationFilter(jwtTokenAuthentication);
        CurrentUserRepresentation currUserRepresentation = getCurrentUser(client, baseUrl);
        String tenantName = getTenantName(client, baseUrl);
        jwtTokenAuthentication.setCurrentUserRepresentation(currUserRepresentation);
        JwtTokenAuthentication updatedToken = updateUserCredentials(tenantName, jwtTokenAuthentication);
        client.close();
        return updatedToken;
    }

    /**
     * Remember to release resources when client is not needed
     */
    static Client createClientWithAuthenticationFilter(JwtTokenAuthentication jwtTokenAuthentication) {
        Client client = ClientBuilder.newClient();
        if (jwtTokenAuthentication != null) {
            JwtCredentials jwtCredentials = jwtTokenAuthentication.getCredentials();
            if (jwtCredentials instanceof JwtAndXsrfTokenCredentials) {
                JwtAndXsrfTokenCredentials jwtAndXsrfCred = (JwtAndXsrfTokenCredentials) jwtCredentials;
                client.register(new CumulocityAuthenticationFilter(
                        CumulocityOAuthCredentials.builder()
                                .authenticationMethod(AuthenticationMethod.COOKIE)
                                .oAuthAccessToken(jwtAndXsrfCred.getJwt().getEncoded())
                                .xsrfToken(jwtAndXsrfCred.getXsrfToken())
                                .build()
                ));
            } else {
                client.register(new CumulocityAuthenticationFilter(
                        CumulocityOAuthCredentials.builder()
                                .authenticationMethod(AuthenticationMethod.HEADER)
                                .oAuthAccessToken(jwtCredentials.getJwt().getEncoded())
                                .build()
                ));
            }
        }
        return client;
    }

    private static CurrentUserRepresentation getCurrentUser(Client client, String baseUrl) {
        return client.target(baseUrl + "/user/currentUser")
                .request(UserMediaType.CURRENT_USER)
                .get(CurrentUserRepresentation.class);
    }

    private static String getTenantName(Client client, String baseUrl) {
        SimplifiedCurrentTenantRepresentation currentTenantRepresentation = client.target(baseUrl + "/tenant/currentTenant")
                .request(UserMediaType.CURRENT_TENANT)
                .get(SimplifiedCurrentTenantRepresentation.class);
        return currentTenantRepresentation.name;
    }

    static JwtTokenAuthentication updateUserCredentials(String tenantName, JwtTokenAuthentication jwtTokenAuthentication) {
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
