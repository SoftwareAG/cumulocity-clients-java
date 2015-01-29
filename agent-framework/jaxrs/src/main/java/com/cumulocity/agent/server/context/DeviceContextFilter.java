package com.cumulocity.agent.server.context;

import java.io.IOException;
import java.util.List;

import javax.ws.rs.container.*;
import javax.ws.rs.ext.Provider;

import org.springframework.beans.factory.annotation.Autowired;

@Provider
@PreMatching
public class DeviceContextFilter implements ContainerRequestFilter, ContainerResponseFilter {

    private final DeviceContextService contextService;

    private final DeviceBootstrapDeviceCredentialsSupplier deviceBootstrapDeviceCredentialsSupplier;

    private final List<DeviceCredentailsResolver<ContainerRequestContext>> deviceCredentailsResolvers;

    @Autowired
    public DeviceContextFilter(DeviceContextService contextService,
            List<DeviceCredentailsResolver<ContainerRequestContext>> deviceCredentailsResolvers,
            DeviceBootstrapDeviceCredentialsSupplier deviceBootstrapDeviceCredentialsSupplier) {
        this.contextService = contextService;
        this.deviceCredentailsResolvers = deviceCredentailsResolvers;
        this.deviceBootstrapDeviceCredentialsSupplier = deviceBootstrapDeviceCredentialsSupplier;
    }

    private DeviceCredentials getCredentials(final ContainerRequestContext request) {

        for (DeviceCredentailsResolver<ContainerRequestContext> resolver : deviceCredentailsResolvers) {
            if (resolver.supports(request)) {
                return resolver.get(request);
            }
        }
        return deviceBootstrapDeviceCredentialsSupplier.get();

    }

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        contextService.enterContext(new DeviceContext(getCredentials(requestContext)));
    }

    @Override
    public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) throws IOException {
        contextService.leaveContext();
    }

}
