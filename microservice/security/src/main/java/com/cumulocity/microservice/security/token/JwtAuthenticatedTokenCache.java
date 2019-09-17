package com.cumulocity.microservice.security.token;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import org.springframework.security.core.Authentication;

/**The method must be prone to concurrent execution*/
public interface JwtAuthenticatedTokenCache {
    Authentication get(JwtCredentials key, Callable<JwtTokenAuthentication> valueLoader) throws ExecutionException;
}

