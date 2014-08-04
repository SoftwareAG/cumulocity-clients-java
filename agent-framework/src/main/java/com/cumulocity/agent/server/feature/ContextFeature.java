package com.cumulocity.agent.server.feature;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.cumulocity.agent.server.context.*;

@Configuration
@Import(ScopesFeature.class)
public class ContextFeature {

    @Bean
    public DeviceContextService contextService() {
        return new DeviceContextServiceImpl();
    }

    @Bean
    public ContextScopeContainerRegistry contextScopeContainerRegistry() {
        return new ContextScopeContainerRegistry();
    }

    @Bean
    public DeviceScopeContainerRegistry deviceScopeContainerRegistry() {
        return new DeviceScopeContainerRegistry();
    }

    @Bean
    public DeviceBootstrapDeviceCredentialsSupplier deviceBootstrapDeviceCredentialsSupplier() {
        return new DeviceBootstrapDeviceCredentialsSupplier();
    }

}
