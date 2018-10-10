package com.cumulocity.microservice.subscription;

import com.cumulocity.microservice.subscription.annotation.EnableMicroserviceSubscriptionConfiguration;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = EnableMicroserviceSubscriptionConfiguration.class)
public class ConfigurationTest {

    @Test
    public void shouldLoadMetadateFromProvidedConfig(){

    }
}
