package com.cumulocity.microservice.monitoring.version.indicator;

import org.springframework.boot.actuate.autoconfigure.endpoint.condition.ConditionalOnAvailableEndpoint;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.info.ProjectInfoAutoConfiguration;
import org.springframework.boot.info.BuildProperties;
import org.springframework.context.annotation.Bean;

@AutoConfiguration(after = ProjectInfoAutoConfiguration.class)
@ConditionalOnAvailableEndpoint(endpoint = VersionEndpoint.class)
public class VersionEndpointAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnBean(value = BuildProperties.class)
    VersionEndpoint versionEndpoint(BuildProperties buildProperties) {
        return new VersionEndpoint(buildProperties);
    }
}
