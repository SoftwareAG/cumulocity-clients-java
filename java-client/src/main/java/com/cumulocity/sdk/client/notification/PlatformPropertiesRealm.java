package com.cumulocity.sdk.client.notification;

import org.eclipse.jetty.client.security.Realm;

import com.cumulocity.sdk.client.PlatformParameters;

public class PlatformPropertiesRealm implements Realm {

    private PlatformParameters paramters;

    public PlatformPropertiesRealm(PlatformParameters paramters) {
        this.paramters = paramters;
    }

    @Override
    public String getId() {
        return null;
    }

    @Override
    public String getPrincipal() {
        return paramters.getTenantId() + "/" + paramters.getUser();
    }

    @Override
    public String getCredentials() {
        return paramters.getPassword();
    }

}
