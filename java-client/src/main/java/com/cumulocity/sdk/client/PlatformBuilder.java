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
        if (tenant != null && !tenant.isEmpty()) {
            return CumulocityCredentials.builder()
                    .username(username)
                    .password(password)
                    .tenantId(tenant)
                    .buildBasic();
        }
        if (oAuthAccessToken != null && !oAuthAccessToken.isEmpty()) {
            return CumulocityCredentials.builder()
                    .oAuthAccessToken(oAuthAccessToken)
                    .xsrfToken(xsrfToken)
                    .buildOAuth();
        }
        throw new IllegalStateException("Missing configuration to create credentials");
    }
}
