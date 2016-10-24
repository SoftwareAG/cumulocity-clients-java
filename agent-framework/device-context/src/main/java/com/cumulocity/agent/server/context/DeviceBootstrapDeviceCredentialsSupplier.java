package com.cumulocity.agent.server.context;

import org.springframework.beans.factory.annotation.Value;

import com.google.common.base.Supplier;

public class DeviceBootstrapDeviceCredentialsSupplier implements Supplier<DeviceCredentials> {

    private @Value("${C8Y.devicebootstrap.tenant:management}") String tenant;

    private @Value("${C8Y.devicebootstrap.user:devicebootstrap}") String username;

    private @Value("${C8Y.devicebootstrap.password:Fhdt1bb1f}") String passwrod;

    @Override
    public DeviceCredentials get() {
        return new DeviceCredentials(tenant, username, passwrod, null, null);
    }

}
