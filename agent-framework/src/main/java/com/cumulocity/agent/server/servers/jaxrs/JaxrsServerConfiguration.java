package com.cumulocity.agent.server.servers.jaxrs;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.context.annotation.*;

import com.cumulocity.agent.server.config.CommonConfiguration;
import com.cumulocity.agent.server.context.*;
import com.cumulocity.agent.server.feature.ScopesFeature;

@Configuration
@ComponentScan(basePackageClasses = JaxrsServer.class)
@Import({CommonConfiguration.class, ScopesFeature.class})
public class JaxrsServerConfiguration {

    @Bean
    public ContextFilter filter(DeviceContextService contextService,
            List<DeviceCredentailsResolver<HttpServletRequest>> deviceCredentailsResolvers,
            DeviceBootstrapDeviceCredentialsSupplier deviceBootstrapDeviceCredentialsSupplier) {
        return new ContextFilter(contextService, deviceCredentailsResolvers, deviceBootstrapDeviceCredentialsSupplier);
    }

    @Bean
    public AuthorizationHeaderDeviceCredentialsResolver authorizationHeaderDeviceCredentialsResolver() {
        return new AuthorizationHeaderDeviceCredentialsResolver();
    }
}
