package com.cumulocity.agent.server.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

import com.cumulocity.agent.server.encryption.Encryption;
import com.cumulocity.agent.server.encryption.PasswordSupplier;
import com.cumulocity.agent.server.encryption.impl.ApplicationIdPasswordSupplier;
import com.cumulocity.agent.server.encryption.impl.DefaultEncryption;

@Configuration
public class CommonConfiguration {
    @Bean
    public static PropertySourcesPlaceholderConfigurer placeholderConfigurer() {
        PropertySourcesPlaceholderConfigurer configurer = new PropertySourcesPlaceholderConfigurer();
        configurer.setIgnoreUnresolvablePlaceholders(false);
        configurer.setOrder(100);
        return configurer;
    }

    @Bean
    public static PasswordSupplier passwordGeneratorService() {
        return new ApplicationIdPasswordSupplier();
    }
}
