package com.cumulocity.microservice.security.filter.provider;

import com.cumulocity.microservice.context.credentials.Credentials;

public interface PostAuthorizationContextProvider<T> {
    boolean supports(T credentialSource);
    Credentials get(T input);
}
