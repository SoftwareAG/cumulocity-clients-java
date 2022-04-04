package com.cumulocity.microservice.security.token;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;

@Slf4j
public class JwtTokenAuthenticationGuavaCache implements JwtAuthenticatedTokenCache {

    private final Cache<JwtCredentials, Authentication> userTokenCache;

    public JwtTokenAuthenticationGuavaCache(int maximumSize, int expireAfterAccessInMinutes, int jwtCacheExpireAfterWrite) {
        CacheBuilder<Object, Object> builder = CacheBuilder.newBuilder()
                .maximumSize(maximumSize)
                .expireAfterAccess(expireAfterAccessInMinutes, TimeUnit.MINUTES);

        if(jwtCacheExpireAfterWrite > 0){
            builder.expireAfterWrite(jwtCacheExpireAfterWrite, TimeUnit.SECONDS);
        }

        this.userTokenCache = builder.build();
    }

    @Override
    public Authentication get(JwtCredentials key, JwtTokenAuthenticationLoader jwtTokenAuthenticationLoader) throws ExecutionException {
        return userTokenCache.get(key, jwtTokenAuthenticationLoader);
    }
}


