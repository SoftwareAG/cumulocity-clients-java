package com.cumulocity.microservice.security.annotation;

import com.cumulocity.microservice.security.token.JwtAuthenticatedTokenCache;
import com.cumulocity.microservice.security.token.JwtTokenAuthenticationGuavaCache;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class TokenCacheConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public JwtAuthenticatedTokenCache jwtAuthenticatedTokenCache(@Value("${cache.guava.maxSize:10000}") int jwtGuavaCacheMaxSize, @Value("${cache.guava.expireAfterAccessInMinutes:10}") int jwtGuavaCacheExpireAfterAccessInMinutes) {
        log.info("Default Guava implementation for token cache is used.\nParameters:\n- max cache size: " + jwtGuavaCacheMaxSize + "\n- expire after access: " + jwtGuavaCacheExpireAfterAccessInMinutes + " minutes");
        return new JwtTokenAuthenticationGuavaCache(jwtGuavaCacheMaxSize, jwtGuavaCacheExpireAfterAccessInMinutes);
    }
}


