package com.cumulocity.microservice.security.token;

import org.springframework.security.jwt.Jwt;

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

    public String getXsrfToken() {
        return xsrfToken;
    }
}
