package com.cumulocity.microservice.platform.api.annotation;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(CumulocityClientFeature.class)
public class EnableMicroservicePlatformInternalApiConfiguration {

}
