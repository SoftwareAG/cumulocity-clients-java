package com.cumulocity.microservice.security.token;

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
