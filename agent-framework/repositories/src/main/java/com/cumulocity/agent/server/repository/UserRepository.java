package com.cumulocity.agent.server.repository;

import org.apache.commons.httpclient.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.cumulocity.agent.server.context.DeviceContextService;
import com.cumulocity.rest.representation.user.UserMediaType;
import com.cumulocity.rest.representation.user.UserRepresentation;
import com.cumulocity.sdk.client.RestConnector;
import com.cumulocity.sdk.client.SDKException;

@Component
public class UserRepository {

    private static final String USER_URL = "user/{tenant}/users/";

    private final RestConnector restConnector;
    
    private final DeviceContextService contextService;

    private final String baseUrl;

    @Autowired
    public UserRepository(RestConnector restConnector, @Value("${C8Y.baseURL}") String host, DeviceContextService contextService) {
        this.restConnector = restConnector;
        this.contextService = contextService;
        if (host.charAt(host.length() - 1) != '/') {
            host = host + "/";
        }
        this.baseUrl = host;
    }

    public UserRepresentation findByUsername(String username) {
        String userApiWithTenantURL = USER_URL.replace("{tenant}", contextService.getContext().getLogin().getTenant());
        try {
            return restConnector.get(baseUrl + userApiWithTenantURL + username, UserMediaType.USER, UserRepresentation.class);
        } catch (SDKException ex) {
            if (ex.getHttpStatus() == HttpStatus.SC_NOT_FOUND) {
                return null;
            } else {
                throw ex;
            }
        }
    }
    
    public UserRepresentation create(UserRepresentation user) {
        String userApiWithTenantURL = USER_URL.replace("{tenant}", contextService.getContext().getLogin().getTenant());
        return restConnector.post(baseUrl + userApiWithTenantURL, UserMediaType.USER, user);
    }

    public UserRepresentation update(UserRepresentation user) {
        String userApiWithTenantURL = USER_URL.replace("{tenant}", contextService.getContext().getLogin().getTenant());
        return restConnector.put(baseUrl + userApiWithTenantURL + user.getId(), UserMediaType.USER, user);
    }
    
    public void delete(String username) {
        String userApiWithTenantURL = USER_URL.replace("{tenant}", contextService.getContext().getLogin().getTenant());
        restConnector.delete(baseUrl + userApiWithTenantURL + username);
    }
}
