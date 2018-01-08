package com.cumulocity.microservice.subscription.repository;

import com.cumulocity.model.authentication.CumulocityCredentials;
import com.cumulocity.sdk.client.Platform;
import com.cumulocity.sdk.client.PlatformBuilder;
import com.cumulocity.sdk.client.RestOperations;
import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class DefaultCredentialsSwitchingPlatform implements CredentialsSwitchingPlatform {
    private final Supplier<String> baseUrl;
    private volatile Supplier<RestOperations> delegate;

    @Override
    public RestOperations get() {
        return delegate.get();
    }

    @Override
    public DefaultCredentialsSwitchingPlatform switchTo(final CumulocityCredentials credentials) {
        delegate = Suppliers.memoize(new Supplier<RestOperations>() {
            @Override
            public RestOperations get() {
                try (Platform platform = PlatformBuilder.platform()
                        .withBaseUrl(baseUrl.get())
                        .withUsername(credentials.getUsername())
                        .withPassword(credentials.getPassword())
                        .withTenant(credentials.getTenantId())
                        .withForceInitialHost(true)
                        .build()) {
                    return platform.rest();
                }
            }
        });

        return this;
    }
}
