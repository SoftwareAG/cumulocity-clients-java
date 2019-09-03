package com.cumulocity.microservice.security.token;

import com.cumulocity.microservice.context.credentials.UserCredentials;
import org.springframework.security.jwt.Jwt;

import java.util.Objects;

public class JwtAndXsrfTokenCredentials implements JwtCredentials{
    private final Jwt jwt;
    private final String xsrfToken;

    public JwtAndXsrfTokenCredentials(Jwt jwt, String xsrfToken) {
        this.jwt = jwt;
        this.xsrfToken = xsrfToken;
    }

    @Override
    public Jwt getJwt() {
        return jwt;
    }

    @Override
    public UserCredentials buildUserCredentials(String tenantName, JwtTokenAuthentication jwtTokenAuthentication) {
        JwtAndXsrfTokenCredentials credentials = ((JwtAndXsrfTokenCredentials) jwtTokenAuthentication.getCredentials());
        return UserCredentials.builder()
                .tenant(tenantName)
                .username(jwtTokenAuthentication.getCurrentUserRepresentation().getUserName())
                .oAuthAccessToken(credentials.getJwt().getEncoded())
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
        return Objects.equals(jwt.getEncoded(), that.jwt.getEncoded()) &&
                Objects.equals(xsrfToken, that.xsrfToken);
    }

    @Override
    public int hashCode() {
        return Objects.hash(jwt.getEncoded(), xsrfToken);
    }
}
