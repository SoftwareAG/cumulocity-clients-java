package com.cumulocity.microservice.subscription;

import com.cumulocity.microservice.subscription.annotation.EnableMicroserviceSubscriptionConfiguration;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@Disabled
@SpringBootTest(classes = EnableMicroserviceSubscriptionConfiguration.class)
@TestPropertySource(properties = "application.name=test")
public class ConfigurationTest {

    @Test
    public void shouldLoadMetadataFromProvidedConfig() {

    }
}
