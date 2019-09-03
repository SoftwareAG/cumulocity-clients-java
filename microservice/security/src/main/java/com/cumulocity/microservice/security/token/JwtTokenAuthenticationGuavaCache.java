
package com.cumulocity.microservice.security.token;

import com.google.common.base.Optional;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;

@Slf4j
public class JwtTokenAuthenticationGuavaCache implements JwtAuthenticatedTokenCache {

    private final Cache<JwtCredentials, Authentication> userTokenCache;

    public JwtTokenAuthenticationGuavaCache() {
        this.userTokenCache = CacheBuilder.newBuilder()
                .maximumSize(10000)
                .expireAfterAccess(10, TimeUnit.MINUTES)
                .build();
    }

    @Override
    public Optional<Authentication> get(JwtCredentials jwtTokenAuthentication) {
        Authentication tokenOrNull = userTokenCache.getIfPresent(jwtTokenAuthentication);
        return Optional.fromNullable(tokenOrNull);
    }

    @Override
    public void put(JwtCredentials key, JwtTokenAuthentication value) {
        userTokenCache.put(key, value);
    }
}


