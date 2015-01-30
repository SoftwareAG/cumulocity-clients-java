package com.cumulocity.agent.server.servers.mvc;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.cumulocity.agent.server.config.CommonConfiguration;
import com.cumulocity.agent.server.context.AuthorizationHeaderDeviceCredentialsResolver;
import com.cumulocity.agent.server.context.ServletDeviceContextFilter;

@Configuration
@ComponentScan(basePackageClasses = MvcServer.class)
@Import({ CommonConfiguration.class, ServletDeviceContextFilter.class, AuthorizationHeaderDeviceCredentialsResolver.class })
public class MvcServerConfiguration {

}
