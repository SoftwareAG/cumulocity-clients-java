package com.cumulocity.sdk.client;

import com.cumulocity.model.authentication.CumulocityCredentials;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.experimental.Wither;

@Wither
@NoArgsConstructor(staticName = "platform")
@AllArgsConstructor
public class PlatformBuilder {
    private String baseUrl;
    private String tenant;
    private String username;
    private String password;
    private String oAuthAccessToken;
    private String xsrfToken;
    private String proxyHost;
    private Integer proxyPort;
    private String tfaToken;
    private ResponseMapper responseMapper;
    private boolean forceInitialHost;

    public Platform build() {
        return configure(new PlatformImpl(baseUrl, buildCredentials()));
    }

    private PlatformImpl configure(PlatformImpl platform) {
        if (proxyHost != null && !proxyHost.isEmpty()) {
            platform.setProxyHost(proxyHost);
        }
        if (proxyPort != null && proxyPort > 0) {
            platform.setProxyPort(proxyPort);
        }
        if (responseMapper != null) {
            platform.setResponseMapper(responseMapper);
        }
        platform.setTfaToken(tfaToken);
        platform.setForceInitialHost(forceInitialHost);
        return platform;
    }

    private CumulocityCredentials buildCredentials() {
        CumulocityCredentials.Builder credentials = CumulocityCredentials.Builder.cumulocityCredentials(username, password);
        if (tenant != null && !tenant.isEmpty()) {
            credentials = credentials.withTenantId(tenant);
        }
        if (oAuthAccessToken != null && !oAuthAccessToken.isEmpty()) {
            credentials = credentials.withOAuthAccessToken(oAuthAccessToken).withXsrfToken(xsrfToken);

        }
        return credentials.build();
    }
}
