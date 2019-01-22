package com.cumulocity.microservice.settings;

import com.cumulocity.microservice.settings.service.MicroserviceSettingsService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;

import org.springframework.core.env.EnumerablePropertySource;


import java.util.Collection;

@Slf4j
public class TenantOptionPropertySource extends EnumerablePropertySource<MicroserviceSettingsService> implements ApplicationContextAware {

    public static final String NAME = "tenant-option";

    public TenantOptionPropertySource(MicroserviceSettingsService microserviceSettingsService) {
        super(NAME, microserviceSettingsService);
    }

    @Override
    public String[] getPropertyNames() {
        final Collection<String> options = getSource().getAll().keySet();
        return options.toArray(new String[options.size()]);
    }

    @Override
    public Object getProperty(final String name) {
        return getSource().getAll().get(name);
    }

    @Override
    public void setApplicationContext(final ApplicationContext applicationContext) throws BeansException {
        logger.info("Overriding application settings with tenant options is enabled.");
        ConfigurableApplicationContext configurableApplicationContext = (ConfigurableApplicationContext) applicationContext;
        configurableApplicationContext.getEnvironment().getPropertySources().addFirst(this);
    }

}
