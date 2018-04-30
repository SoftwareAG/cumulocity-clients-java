package com.cumulocity.microservice.health.indicator.memory;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("management.health.heapmemory")
public class HeapMemoryHealthIndicatorProperties extends AbstractMemoryHealthIndicatorProperties {

}
