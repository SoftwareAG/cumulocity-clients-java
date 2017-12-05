package com.cumulocity.microservice.context.credentials;

public interface Credentials {
    String getUsername();
    String getTenant();
    String getPassword();
    String getTfaToken();
    String getAppKey();
}

