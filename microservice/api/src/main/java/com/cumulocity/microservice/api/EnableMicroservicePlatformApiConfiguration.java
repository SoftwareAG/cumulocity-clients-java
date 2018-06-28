package com.cumulocity.microservice.api;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(CumulocityClientFeature.class)
public class EnableMicroservicePlatformApiConfiguration {
}
