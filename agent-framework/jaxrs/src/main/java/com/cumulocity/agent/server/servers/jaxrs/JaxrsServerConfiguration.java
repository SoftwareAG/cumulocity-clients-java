package com.cumulocity.agent.server.servers.jaxrs;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.cumulocity.agent.server.config.CommonConfiguration;

@Configuration
@ComponentScan(basePackageClasses = JaxrsServer.class)
@Import({ CommonConfiguration.class })
public class JaxrsServerConfiguration {
}
