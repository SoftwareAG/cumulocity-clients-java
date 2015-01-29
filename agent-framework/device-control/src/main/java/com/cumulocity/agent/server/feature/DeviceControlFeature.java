package com.cumulocity.agent.server.feature;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@ComponentScan(basePackages = "com.cumulocity.agent.server.devicecontrol")
@Import({ ContextFeature.class, RepositoryFeature.class })
public class DeviceControlFeature {

}
