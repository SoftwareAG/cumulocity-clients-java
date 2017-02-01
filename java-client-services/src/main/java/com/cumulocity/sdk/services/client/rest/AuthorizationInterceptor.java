package com.cumulocity.sdk.services.client.rest;

import static org.springframework.http.MediaType.APPLICATION_JSON;

import java.io.IOException;

import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.security.crypto.codec.Base64;
import com.cumulocity.model.authentication.CumulocityCredentials;
import org.springframework.http.HttpHeaders;

public class AuthorizationInterceptor implements ClientHttpRequestInterceptor {

    private final CumulocityCredentials credentials;
    public AuthorizationInterceptor(CumulocityCredentials credentials) {
        this.credentials = credentials;
    }
    
    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        final HttpHeaders headers = request.getHeaders();
        headers.set("Authorization", getBasicAuthorization(credentials));
        return execution.execute(request, body);
    }
    
    private String getBasicAuthorization(CumulocityCredentials credentials) {
        String basicAuth = "Basic " + base64Credentials(credentials.getTenantId(), credentials.getUsername(), credentials.getPassword());
        return basicAuth;
    }

    private String base64Credentials(String tenantId, String username, String password) {
        String auth = tenantId + "/" + username + ":" + password;
        String base64EncodedAuth = new String(Base64.encode(auth.getBytes()));
        return base64EncodedAuth;
    }

}
