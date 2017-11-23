package com.cumulocity.sdk.client;

import com.cumulocity.model.authentication.CumulocityCredentials;

public class PlatformBuilder {
    private String baseUrl;
    private String tenant;
    private String username;
    private String password;

    public static PlatformBuilder platform(){
        return new PlatformBuilder();
    }

    public PlatformBuilder withBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
        return this;
    }

    public PlatformBuilder withTenant(String tenant) {
        this.tenant = tenant;
        return this;
    }

    public PlatformBuilder withUsername(String username) {
        this.username = username;
        return this;
    }

    public PlatformBuilder withPassword(String password) {
        this.password = password;
        return this;
    }

    public Platform build(){
        return new PlatformImpl(baseUrl,buildCredentials());
    }

    private CumulocityCredentials buildCredentials() {
        final CumulocityCredentials.Builder credentials = CumulocityCredentials.Builder.cumulocityCredentials(username,password);
        if(tenant != null && !tenant.isEmpty()){
            return credentials.withTenantId(tenant).build();
        }
        return credentials.build();
    }
}
