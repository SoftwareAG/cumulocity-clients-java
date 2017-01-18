package com.cumulocity.agent.server.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

import com.cumulocity.agent.server.service.PasswordGeneratorService;
import com.cumulocity.agent.server.service.StringEncryptionService;
import com.cumulocity.agent.server.service.impl.ApplicationIdPasswordGeneratorService;
import com.cumulocity.agent.server.service.impl.StringEncryptionServiceImpl;

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
    public static StringEncryptionService valueEncryptionService() {
        return new StringEncryptionServiceImpl();
    }

    @Bean
    public static PasswordGeneratorService passwordGeneratorService() {
        return new ApplicationIdPasswordGeneratorService();
    }
}
