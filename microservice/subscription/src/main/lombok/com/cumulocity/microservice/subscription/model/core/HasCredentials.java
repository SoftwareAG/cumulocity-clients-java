package com.cumulocity.microservice.subscription.model.core;

import com.cumulocity.microservice.context.credentials.Credentials;

public interface HasCredentials {
    Credentials getCredentials();
}
