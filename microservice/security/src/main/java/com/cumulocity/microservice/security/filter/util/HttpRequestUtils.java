package com.cumulocity.microservice.security.filter.util;

import lombok.Builder;
import lombok.Data;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;

import static com.cumulocity.agent.server.context.DeviceCredentials.decode;
import static com.cumulocity.agent.server.context.DeviceCredentials.splitUsername;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

public class HttpRequestUtils {
    public static final String X_CUMULOCITY_APPLICATION_KEY = "X-Cumulocity-Application-Key";
    public static final String TFA_TOKEN_HEADER = "TFAToken";
    public static final String LOGIN_SEPARATOR = "/";

    @Data
    @Builder
    public static class AuthorizationHeader {
        private String tenant;
        private String username;
        private String password;
    }

    public static boolean hasAuthorizationHeader(Object credentialSource) {
        return credentialSource instanceof HttpServletRequest
                && !StringUtils.hasText(((HttpServletRequest) credentialSource).getHeader(AUTHORIZATION));
    }

    public static AuthorizationHeader authorizationHeader(String authorization) {
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

    public static String[] splitUsername(String username) {
        return username.split(LOGIN_SEPARATOR);
    }
}
