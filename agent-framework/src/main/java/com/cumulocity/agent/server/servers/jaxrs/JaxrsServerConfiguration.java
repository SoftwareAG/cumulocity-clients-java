package com.cumulocity.agent.server.servers.jaxrs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.cumulocity.agent.server.config.CommonConfiguration;
import com.cumulocity.agent.server.context.AuthorizationHeaderDeviceCredentialsResolver;
import com.cumulocity.agent.server.feature.ScopesFeature;

@Configuration
@ComponentScan(basePackageClasses = JaxrsServer.class)
@Import({CommonConfiguration.class, ScopesFeature.class})
public class JaxrsServerConfiguration {

    @Bean
    public AuthorizationHeaderDeviceCredentialsResolver authorizationHeaderDeviceCredentialsResolver() {
        return new AuthorizationHeaderDeviceCredentialsResolver();
    }
}
