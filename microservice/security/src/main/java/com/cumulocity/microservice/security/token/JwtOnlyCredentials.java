package com.cumulocity.microservice.security.token;

import org.springframework.security.jwt.Jwt;

public class JwtOnlyCredentials implements JwtCredentials{
    private final Jwt jwt;

    public JwtOnlyCredentials(Jwt jwt) {
        this.jwt = jwt;
    }

    @Override
    public Jwt getJwt() {
        return jwt;
    }
}
