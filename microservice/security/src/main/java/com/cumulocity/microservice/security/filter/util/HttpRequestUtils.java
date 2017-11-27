package com.cumulocity.microservice.security.filter.util;

import lombok.Builder;
import lombok.Data;

import static com.cumulocity.agent.server.context.DeviceCredentials.decode;
import static com.cumulocity.agent.server.context.DeviceCredentials.splitUsername;

public class HttpRequestUtils {

    @Data
    @Builder
    public static class AuthorizationHeader {
        private String tenant;
        private String username;
        private String password;
    }

    public static AuthorizationHeader from(String authorization) {
        String tenant = null;
        String username = null;
        String password = null;

        if (authorization != null) {
            String[] loginAndPass = decode(authorization);

            tenant = null;
            username = loginAndPass.length > 0 ? loginAndPass[0] : "";
            password = loginAndPass.length > 1 ? loginAndPass[1] : "";
            final String[] tenantAndUsername = splitUsername(username);
            if (tenantAndUsername.length > 1) {
                tenant = tenantAndUsername[0];
                username = tenantAndUsername[1];
            }
        }

        return AuthorizationHeader.builder()
                .tenant(tenant)
                .username(username)
                .password(password)
                .build();
    }
}
