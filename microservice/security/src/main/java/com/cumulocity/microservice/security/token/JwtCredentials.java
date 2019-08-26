package com.cumulocity.microservice.security.token;

import org.springframework.security.jwt.Jwt;

public interface JwtCredentials {
    Jwt getJwt();
}
