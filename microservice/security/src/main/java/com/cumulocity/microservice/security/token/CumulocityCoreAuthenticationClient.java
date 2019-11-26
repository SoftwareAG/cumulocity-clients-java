package com.cumulocity.microservice.security.token;

import com.cumulocity.microservice.context.credentials.UserCredentials;
import com.cumulocity.model.authentication.AuthenticationMethod;
import com.cumulocity.model.authentication.CumulocityOAuthCredentials;
import com.cumulocity.rest.mediatypes.ErrorMessageRepresentationReader;
import com.cumulocity.rest.providers.CumulocityJSONMessageBodyReader;
import com.cumulocity.rest.representation.AbstractExtensibleRepresentation;
import com.cumulocity.rest.representation.user.CurrentUserRepresentation;
import com.cumulocity.rest.representation.user.UserMediaType;
import com.cumulocity.sdk.client.CumulocityAuthenticationFilter;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.client.apache.ApacheHttpClient;
import com.sun.jersey.client.apache.ApacheHttpClientHandler;
import com.sun.jersey.client.apache.config.DefaultApacheHttpClientConfig;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;

class CumulocityCoreAuthenticationClient {
    private static final int CONNECTION_TIMEOUT = 30000;

    private CumulocityCoreAuthenticationClient() {
    }

    static JwtTokenAuthentication authenticateUserAndUpdateToken(String baseUrl, JwtTokenAuthentication jwtTokenAuthentication) {
        Client client = createClientWithAuthenticationFilter(jwtTokenAuthentication);
        try {
            CurrentUserRepresentation currUserRepresentation = getCurrentUser(client, baseUrl);
            String tenantName = getTenantName(client, baseUrl);
            jwtTokenAuthentication.setCurrentUserRepresentation(currUserRepresentation);
            return updateUserCredentials(tenantName, jwtTokenAuthentication);
        } finally {
            client.destroy();
        }
    }

    /**
     * Remember to release resources when client is not needed
     */
    static Client createClientWithAuthenticationFilter(JwtTokenAuthentication jwtTokenAuthentication) {
        final HttpClient httpClient = new HttpClient(new MultiThreadedHttpConnectionManager());
        DefaultApacheHttpClientConfig clientConfig = new DefaultApacheHttpClientConfig();
        clientConfig.getClasses().add(CumulocityJSONMessageBodyReader.class);
        clientConfig.getClasses().add(ErrorMessageRepresentationReader.class);
        ApacheHttpClientHandler apacheHttpClientHandler = new ApacheHttpClientHandler(httpClient, clientConfig);
        ApacheHttpClient client = new ApacheHttpClient(apacheHttpClientHandler);
        client.setConnectTimeout(CONNECTION_TIMEOUT);
        if (jwtTokenAuthentication != null) {
            JwtCredentials jwtCredentials = jwtTokenAuthentication.getCredentials();
            if (jwtCredentials instanceof JwtAndXsrfTokenCredentials) {
                JwtAndXsrfTokenCredentials jwtAndXsrfCred = (JwtAndXsrfTokenCredentials) jwtCredentials;
                client.addFilter(new CumulocityAuthenticationFilter(
                        CumulocityOAuthCredentials.builder()
                                .authenticationMethod(AuthenticationMethod.COOKIE)
                                .oAuthAccessToken(jwtAndXsrfCred.getJwt().getEncoded())
                                .xsrfToken(jwtAndXsrfCred.getXsrfToken())
                                .build()
                ));
            } else {
                client.addFilter(new CumulocityAuthenticationFilter(
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
        return client.resource(baseUrl + "/user/currentUser")
                .accept(UserMediaType.CURRENT_USER)
                .get(CurrentUserRepresentation.class);
    }

    private static String getTenantName(Client client, String baseUrl) {
        SimplifiedCurrentTenantRepresentation currentTenantRepresentation = client.resource(baseUrl + "/tenant/currentTenant")
                .accept(UserMediaType.CURRENT_TENANT)
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
