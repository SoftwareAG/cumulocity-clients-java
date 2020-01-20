package com.cumulocity.microservice.security.token;

import com.cumulocity.microservice.context.credentials.UserCredentials;
import org.springframework.security.jwt.Jwt;

import java.util.Objects;

public class JwtOnlyCredentials implements JwtCredentials{
    private final Jwt jwt;

    public JwtOnlyCredentials(Jwt jwt) {
        this.jwt = jwt;
    }

    @Override
    public Jwt getJwt() {
        return jwt;
    }

    @Override
    public UserCredentials toUserCredentials(String tenantName, JwtTokenAuthentication jwtTokenAuthentication) {
        return UserCredentials.builder()
                .tenant(tenantName)
                .username(jwtTokenAuthentication.getCurrentUserRepresentation().getUserName())
                .oAuthAccessToken((jwtTokenAuthentication.getCredentials()).getJwt().getEncoded())
                .build();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        JwtOnlyCredentials that = (JwtOnlyCredentials) o;
        return Objects.equals(jwt.getEncoded(), that.jwt.getEncoded());
    }

    @Override
    public int hashCode() {
        return Objects.hash(jwt.getEncoded());
    }
}
