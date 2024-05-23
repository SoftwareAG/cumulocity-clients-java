package com.cumulocity.microservice.monitoring.health.indicator.subscribtion;

import com.cumulocity.microservice.subscription.service.MicroserviceSubscriptionsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import static org.mockito.Mockito.mock;

@Configuration
public class TestMicroserviceSubscriptionConfiguration {

    @Bean
    @Primary
    public MicroserviceSubscriptionsService microserviceSubscriptionsService() {
        return mock(MicroserviceSubscriptionsService.class);
    }

}
