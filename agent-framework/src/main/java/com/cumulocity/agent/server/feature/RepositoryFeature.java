package com.cumulocity.agent.server.feature;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@ComponentScan(basePackages = "com.cumulocity.agent.server.repository")
@Import(CumulocityClientFeature.class)
public class RepositoryFeature {

}
