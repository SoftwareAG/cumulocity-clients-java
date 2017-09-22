package com.cumulocity.agent.server.repository;

import com.cumulocity.agent.server.context.DeviceContextService;
import com.cumulocity.rest.representation.user.UserRepresentation;
import com.cumulocity.sdk.client.SDKException;
import com.cumulocity.sdk.client.user.UserApi;
import org.apache.commons.httpclient.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserRepository {

    private final DeviceContextService contextService;
    private final UserApi userApi;

    @Autowired
    public UserRepository(DeviceContextService contextService, UserApi userApi) {
        this.contextService = contextService;
        this.userApi = userApi;
    }

    public UserRepresentation findByUsername(String username) {
        try {
            return userApi.getUser(currentTenant(), username);
        } catch (SDKException ex) {
            if (ex.getHttpStatus() == HttpStatus.SC_NOT_FOUND) {
                return null;
            } else {
                throw ex;
            }
        }
    }

    public UserRepresentation create(UserRepresentation user) {
        return userApi.create(currentTenant(), user);
    }

    public UserRepresentation update(UserRepresentation user) {
        if (user.getUserName() == null) {
            throw new IllegalArgumentException("Username must not be null !");
        }
        return userApi.update(currentTenant(), user);
    }

    public void delete(String username) {
        userApi.delete(currentTenant(), username);
    }

    private String currentTenant() {
        return contextService.getContext().getLogin().getTenant();
    }
}
