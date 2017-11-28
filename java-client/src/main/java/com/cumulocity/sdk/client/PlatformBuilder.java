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
    private String proxyHost;
    private Integer proxyPort;
    private String tfaToken;

    public Platform build(){
        return proxy(new PlatformImpl(baseUrl,buildCredentials()));
    }

    private PlatformImpl proxy(PlatformImpl platform) {
        if (proxyHost != null && !proxyHost.isEmpty()) {
            platform.setProxyHost(proxyHost);
        }
        if (proxyPort != null && proxyPort > 0) {
            platform.setProxyPort(proxyPort);
        }
        return platform;
    }

    private CumulocityCredentials buildCredentials() {
        final CumulocityCredentials.Builder credentials = CumulocityCredentials.Builder.cumulocityCredentials(username,password);
        if(tenant != null && !tenant.isEmpty()){
            return credentials.withTenantId(tenant).build();
        }
        return credentials.build();
    }
}
