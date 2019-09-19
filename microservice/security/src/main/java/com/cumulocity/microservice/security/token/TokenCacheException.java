package com.cumulocity.microservice.security.token;

import org.springframework.security.core.AuthenticationException;

public class TokenCacheException extends AuthenticationException {
    public TokenCacheException(String msg, Throwable t) {
        super(msg, t);
    }
}
