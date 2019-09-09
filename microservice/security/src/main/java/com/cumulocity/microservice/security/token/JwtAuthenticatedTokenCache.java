package com.cumulocity.microservice.security.token;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import org.springframework.security.core.Authentication;

/**During implementation please take care of race condition when reading and writing to cache.*/
public interface JwtAuthenticatedTokenCache {
    Authentication get(JwtCredentials key, Callable<JwtTokenAuthentication> valueLoader) throws ExecutionException;
}

