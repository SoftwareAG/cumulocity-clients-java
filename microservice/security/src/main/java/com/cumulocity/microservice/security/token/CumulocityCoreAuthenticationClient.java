package com.cumulocity.microservice.security.token;

import com.cumulocity.microservice.context.credentials.UserCredentials;
import com.cumulocity.model.authentication.AuthenticationMethod;
import com.cumulocity.model.authentication.CumulocityOAuthCredentials;
import com.cumulocity.rest.representation.AbstractExtensibleRepresentation;
import com.cumulocity.rest.representation.user.CurrentUserRepresentation;
import com.cumulocity.rest.representation.user.UserMediaType;
import com.cumulocity.sdk.client.CumulocityAuthenticationFilter;
import com.cumulocity.sdk.client.rest.mediatypes.ErrorMessageRepresentationReader;
import com.cumulocity.sdk.client.rest.providers.CumulocityJSONMessageBodyReader;

import lombok.Getter;
import lombok.Setter;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.glassfish.jersey.apache.connector.ApacheClientProperties;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.client.ClientProperties;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;

class CumulocityCoreAuthenticationClient {

    private static final int CONNECTION_TIMEOUT = 30000;

    private CumulocityCoreAuthenticationClient() { }

    static JwtTokenAuthentication authenticateUserAndUpdateToken(String baseUrl, JwtTokenAuthentication jwtTokenAuthentication) {
        Client client = createClientWithAuthenticationFilter(jwtTokenAuthentication);
        try {
            CurrentUserRepresentation currUserRepresentation = getCurrentUser(client, baseUrl);
            String tenantName = getTenantName(client, baseUrl);
            jwtTokenAuthentication.setCurrentUserRepresentation(currUserRepresentation);
            return updateUserCredentials(tenantName, jwtTokenAuthentication);
        } finally {
            client.close();
        }
    }

    /**
     * Remember to release resources when client is not needed
     */
    static Client createClientWithAuthenticationFilter(JwtTokenAuthentication jwtTokenAuthentication) {

        ClientConfig clientConfig = new ClientConfig();
        clientConfig.register(CumulocityJSONMessageBodyReader.class);
        clientConfig.register(ErrorMessageRepresentationReader.class);
        clientConfig.property(ApacheClientProperties.CONNECTION_MANAGER, new PoolingHttpClientConnectionManager());
        clientConfig.property(ClientProperties.CONNECT_TIMEOUT, CONNECTION_TIMEOUT);

        Client client = ClientBuilder.newClient(clientConfig);
        if (jwtTokenAuthentication != null) {
            JwtCredentials jwtCredentials = jwtTokenAuthentication.getCredentials();
            if (jwtCredentials instanceof JwtAndXsrfTokenCredentials) {
                JwtAndXsrfTokenCredentials jwtAndXsrfCred = (JwtAndXsrfTokenCredentials) jwtCredentials;
                client.register(new CumulocityAuthenticationFilter(
                        CumulocityOAuthCredentials.builder()
                                .authenticationMethod(AuthenticationMethod.COOKIE)
                                .oAuthAccessToken(jwtAndXsrfCred.getJwt().serialize())
                                .xsrfToken(jwtAndXsrfCred.getXsrfToken())
                                .build()
                ));
            } else {
                client.register(new CumulocityAuthenticationFilter(
                        CumulocityOAuthCredentials.builder()
                                .authenticationMethod(AuthenticationMethod.HEADER)
                                .oAuthAccessToken(jwtCredentials.getJwt().serialize())
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

    public static class SimplifiedCurrentTenantRepresentation extends AbstractExtensibleRepresentation {
        @Getter
        @Setter
        private String name;
    }
}
