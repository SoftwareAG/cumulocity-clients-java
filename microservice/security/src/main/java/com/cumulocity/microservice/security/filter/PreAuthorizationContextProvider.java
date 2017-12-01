package com.cumulocity.microservice.security.filter;

import com.cumulocity.microservice.context.credentials.Credentials;

public interface PreAuthorizationContextProvider<T> {
    boolean supports(T credentialSource);
    Credentials get(T input);
}
