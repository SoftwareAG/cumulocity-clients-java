package com.cumulocity.microservice.security.token;

import com.cumulocity.microservice.context.credentials.UserCredentials;
import com.nimbusds.jwt.JWT;

import java.util.Objects;

public class JwtAndXsrfTokenCredentials implements JwtCredentials{
    private final JWT jwt;
    private final String xsrfToken;

    public JwtAndXsrfTokenCredentials(JWT jwt, String xsrfToken) {
        this.jwt = jwt;
        this.xsrfToken = xsrfToken;
    }

    @Override
    public JWT getJwt() {
        return jwt;
    }

    @Override
    public UserCredentials toUserCredentials(String tenantName, JwtTokenAuthentication jwtTokenAuthentication) {
        JwtAndXsrfTokenCredentials credentials = ((JwtAndXsrfTokenCredentials) jwtTokenAuthentication.getCredentials());
        return UserCredentials.builder()
                .tenant(tenantName)
                .username(jwtTokenAuthentication.getCurrentUserRepresentation().getUserName())
                .oAuthAccessToken(credentials.getJwt().serialize())
                .xsrfToken(credentials.getXsrfToken())
                .build();    }

    public String getXsrfToken() {
        return xsrfToken;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        JwtAndXsrfTokenCredentials that = (JwtAndXsrfTokenCredentials) o;
        return Objects.equals(jwt.serialize(), that.jwt.serialize()) &&
                Objects.equals(xsrfToken, that.xsrfToken);
    }

    @Override
    public int hashCode() {
        return Objects.hash(jwt.serialize(), xsrfToken);
    }
}
