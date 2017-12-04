package com.cumulocity.microservice.properties;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringApplicationRunListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.CompositePropertySource;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.DefaultPropertySourceFactory;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.core.io.support.PropertySourceFactory;
import org.springframework.core.io.support.ResourcePropertySource;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class PropertiesRunListener implements SpringApplicationRunListener {

    private final List<String> propertySourceNames = new ArrayList<>();

    public PropertiesRunListener(SpringApplication application, String[] args) {
    }

    @Override
    public void starting() {
    }

    @Override
    public void environmentPrepared(ConfigurableEnvironment environment) {
        final String applicationName = environment.getProperty("application.name");
        final ConfigurationFileProvider provider = new ConfigurationFileProvider(applicationName);
        Iterable<File> locations = provider.find(applicationName + ".properties", applicationName + "-agent-server.properties");
        processPropertySource(environment, locations);
    }

    @Override
    public void contextPrepared(ConfigurableApplicationContext configurableApplicationContext) {
    }

    @Override
    public void contextLoaded(ConfigurableApplicationContext configurableApplicationContext) {
    }

    @Override
    public void finished(ConfigurableApplicationContext configurableApplicationContext, Throwable throwable) {
    }

    private void processPropertySource(ConfigurableEnvironment environment, Iterable<File> locations) {
        final PropertySourceFactory factory = new DefaultPropertySourceFactory();

        for (final File location : locations) {
            try {
                final Resource resource = new FileSystemResource(location);
                final PropertySource<?> propertySource = factory.createPropertySource(null, new EncodedResource(resource, "UTF8"));
                final String name = propertySource.getName();

                final MutablePropertySources propertySources = environment.getPropertySources();
                if (propertySources.contains(name) && propertySourceNames.contains(name)) {
                    // We've already added a version, we need to extend it
                    PropertySource<?> existing = propertySources.get(name);
                    PropertySource<?> newSource = (propertySource instanceof ResourcePropertySource ? ((ResourcePropertySource) propertySource).withResourceName() : propertySource);
                    if (existing instanceof CompositePropertySource) {
                        ((CompositePropertySource) existing).addFirstPropertySource(newSource);
                    } else {
                        if (existing instanceof ResourcePropertySource) {
                            existing = ((ResourcePropertySource) existing).withResourceName();
                        }
                        CompositePropertySource composite = new CompositePropertySource(name);
                        composite.addPropertySource(newSource);
                        composite.addPropertySource(existing);
                        propertySources.replace(name, composite);
                    }
                } else {
                    if (propertySourceNames.isEmpty()) {
                        propertySources.addLast(propertySource);
                    } else {
                        String firstProcessed = propertySourceNames.get(propertySourceNames.size() - 1);
                        propertySources.addBefore(firstProcessed, propertySource);
                    }
                }
                propertySourceNames.add(name);
            } catch (final FileNotFoundException ex) {
//                ok
            } catch (final IOException ex) {
                log.error(ex.getMessage(), ex);
            }
        }
    }

}
