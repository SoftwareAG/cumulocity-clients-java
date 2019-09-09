
package com.cumulocity.microservice.security.token;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;

@Slf4j
public class JwtTokenAuthenticationGuavaCache implements JwtAuthenticatedTokenCache {

    private final Cache<JwtCredentials, Authentication> userTokenCache;

    public JwtTokenAuthenticationGuavaCache(int maximumSize, int expireAfterAccessInMinutes) {
        this.userTokenCache = CacheBuilder.newBuilder()
                .maximumSize(maximumSize)
                .expireAfterAccess(expireAfterAccessInMinutes, TimeUnit.MINUTES)
                .build();
    }

    @Override
    public Authentication get(JwtCredentials key, Callable<JwtTokenAuthentication> valueLoader) throws ExecutionException {
        return userTokenCache.get(key, valueLoader);
    }
}


