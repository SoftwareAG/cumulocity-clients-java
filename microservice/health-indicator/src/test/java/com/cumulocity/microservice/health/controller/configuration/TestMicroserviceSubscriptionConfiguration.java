package com.cumulocity.microservice.health.controller.configuration;

import com.cumulocity.microservice.subscription.service.MicroserviceSubscriptionsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

@Configuration
public class TestMicroserviceSubscriptionConfiguration {

    @Bean
    @Primary
    public MicroserviceSubscriptionsService microserviceSubscriptionsService() {
        MicroserviceSubscriptionsService service = mock(MicroserviceSubscriptionsService.class);
        doReturn(false).when(service).isSubscribed();
        return service;
    }

}
