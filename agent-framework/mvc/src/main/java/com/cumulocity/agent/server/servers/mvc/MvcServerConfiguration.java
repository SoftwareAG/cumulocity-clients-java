package com.cumulocity.agent.server.servers.mvc;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.cumulocity.agent.server.config.CommonConfiguration;

@Configuration
@ComponentScan(basePackageClasses = MvcServer.class)
@Import({ CommonConfiguration.class })
public class MvcServerConfiguration {

}
