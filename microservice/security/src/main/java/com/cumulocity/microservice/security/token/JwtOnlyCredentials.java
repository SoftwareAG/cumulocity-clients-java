package com.cumulocity.microservice.security.token;

import com.cumulocity.microservice.context.credentials.UserCredentials;
import com.nimbusds.jwt.JWT;

import java.util.Objects;

public class JwtOnlyCredentials implements JwtCredentials{
    private final JWT jwt;

    public JwtOnlyCredentials(JWT jwt) {
        this.jwt = jwt;
    }

    @Override
    public JWT getJwt() {
        return jwt;
    }

    @Override
    public UserCredentials toUserCredentials(String tenantName, JwtTokenAuthentication jwtTokenAuthentication) {
        return UserCredentials.builder()
                .tenant(tenantName)
                .username(jwtTokenAuthentication.getCurrentUserRepresentation().getUserName())
                .oAuthAccessToken((jwtTokenAuthentication.getCredentials()).getJwt().serialize())
                .build();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        JwtOnlyCredentials that = (JwtOnlyCredentials) o;
        return Objects.equals(jwt.serialize(), that.jwt.serialize());
    }

    @Override
    public int hashCode() {
        return Objects.hash(jwt.serialize());
    }
}
