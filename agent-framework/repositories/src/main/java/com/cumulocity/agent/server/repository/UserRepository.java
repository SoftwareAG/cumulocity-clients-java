package com.cumulocity.agent.server.repository;

import org.apache.commons.httpclient.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.cumulocity.rest.representation.user.UserMediaType;
import com.cumulocity.rest.representation.user.UserRepresentation;
import com.cumulocity.sdk.client.RestConnector;
import com.cumulocity.sdk.client.SDKException;

@Component
public class UserRepository {

    private static final String USER_URL = "/user/{tenant}/users";

    private final RestConnector restConnector;

    private final String baseUrl;

    @Autowired
    public UserRepository(RestConnector restConnector, @Value("${C8Y.baseURL}") String host) {
        this.restConnector = restConnector;
        if (host.charAt(host.length() - 1) != '/') {
            host = host + "/";
        }
        this.baseUrl = host;
    }

    public UserRepresentation findByUsername(String username) {
        try {
            return restConnector.get(baseUrl + USER_URL, UserMediaType.USER, UserRepresentation.class);
        } catch (SDKException ex) {
            if (ex.getHttpStatus() == HttpStatus.SC_NOT_FOUND) {
                return null;
            } else {
                throw ex;
            }
        }
    }
}
