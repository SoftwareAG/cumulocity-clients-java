package com.cumulocity.microservice.security.token;

import com.cumulocity.microservice.context.credentials.UserCredentials;
import org.springframework.security.jwt.Jwt;

public interface JwtCredentials {
    Jwt getJwt();
    UserCredentials toUserCredentials(String tenantName, JwtTokenAuthentication jwtTokenAuthentication);
}
