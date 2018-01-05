package com.cumulocity.microservice.agent.server.api.service;

import com.cumulocity.model.authentication.CumulocityCredentials;
import com.cumulocity.sdk.client.RestOperations;
import com.google.common.base.Supplier;

public interface CredentialsSwitchingPlatform extends Supplier<RestOperations> {
    @Override
    RestOperations get();

    CredentialsSwitchingPlatform switchTo(CumulocityCredentials credentials);
}
