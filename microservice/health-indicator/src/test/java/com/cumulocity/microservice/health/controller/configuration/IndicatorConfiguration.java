package com.cumulocity.microservice.health.controller.configuration;

import com.cumulocity.microservice.health.annotation.EnableHealthIndicator;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableHealthIndicator
public class IndicatorConfiguration {
}
