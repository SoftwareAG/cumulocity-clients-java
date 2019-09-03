package com.cumulocity.microservice.security.token;

import com.google.common.base.Optional;
import org.springframework.security.core.Authentication;

public interface JwtAuthenticatedTokenCache {
    Optional<Authentication> get(JwtCredentials key);
    void put(JwtCredentials key,JwtTokenAuthentication value);
}

