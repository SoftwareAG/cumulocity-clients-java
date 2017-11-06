package com.cumulocity.microservice.subscription.model.core;

import com.fasterxml.jackson.annotation.JsonIgnore;

public interface Credentials {

    String getName();

    String getTenant();

    @JsonIgnore
    String getPassword();

    @JsonIgnore
    String getTfaToken();
}
