package com.cumulocity.agent.server.feature;

import java.util.List;

import javax.ws.rs.container.ContainerRequestContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.cumulocity.agent.server.context.*;

@Configuration
@Import({ ScopesFeature.class })
public class DeviceAuthorizationFeature {
    @Bean
    public AuthorizationHeaderDeviceCredentialsResolver authorizationHeaderDeviceCredentialsResolver() {
        return new AuthorizationHeaderDeviceCredentialsResolver();
    }

    @Bean
    @Autowired
    public DeviceContextFilter deviceContextFilter(DeviceContextService contextService,
            List<DeviceCredentailsResolver<ContainerRequestContext>> deviceCredentailsResolvers,
            DeviceBootstrapDeviceCredentialsSupplier deviceBootstrapDeviceCredentialsSupplier) {
        return new DeviceContextFilter(contextService, deviceCredentailsResolvers, deviceBootstrapDeviceCredentialsSupplier);
    }
}
