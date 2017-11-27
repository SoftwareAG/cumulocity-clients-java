package com.cumulocity.microservice.security.filter;

import com.cumulocity.microservice.context.credentials.Credentials;

public interface CredentailsProvider<T> {
    boolean supports(Object credentialSource);
    Credentials get(T input);
}
