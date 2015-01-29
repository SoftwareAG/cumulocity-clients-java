package com.cumulocity.agent.server;

import static com.google.common.base.Throwables.propagate;
import static com.google.common.collect.FluentIterable.from;
import static com.google.common.collect.Iterables.concat;
import static java.nio.file.Files.exists;
import static org.springframework.beans.BeanUtils.instantiateClass;

import java.net.InetSocketAddress;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Properties;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.bridge.SLF4JBridgeHandler;
import org.springframework.beans.BeanInstantiationException;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.ResourceLoader;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.joran.JoranConfigurator;
import ch.qos.logback.core.joran.spi.JoranException;

import com.cumulocity.agent.server.config.CommonConfiguration;
import com.cumulocity.agent.server.config.PropertiesFactoryBean;
import com.google.common.collect.ImmutableList;

public class ServerBuilder {

    private final Logger log = LoggerFactory.getLogger(ServerBuilder.class);

    public static interface ApplicationBuilder {
        public ServerBuilder application(String id);
    }

    private final InetSocketAddress address;

    private final String applicationId;

    private final Set<String> configurations = new LinkedHashSet<String>();

    private final Set<Class<?>> features = new LinkedHashSet<Class<?>>();

    protected InetSocketAddress address() {
        return address;
    }

    public static ApplicationBuilder on(final InetSocketAddress address) {
        return new ApplicationBuilder() {
            @Override
            public ServerBuilder application(String id) {
                return new ServerBuilder(address, id);
            }
        };
    }

    public static ApplicationBuilder create() {
        return new ApplicationBuilder() {
            @Override
            public ServerBuilder application(String id) {
                return new ServerBuilder(null, id);
            }
        };
    }

    private static void configureLogger(String id, String config) {
        Collection<Path> logbackConfig = ImmutableList.of(Paths.get("/etc", id, config + ".xml"),
                Paths.get(System.getProperty("user.home"), "." + id, config + ".xml"));
        SLF4JBridgeHandler.removeHandlersForRootLogger();
        SLF4JBridgeHandler.install();
        for (Path path : logbackConfig) {
            if (searchLoggerConfiguration(path)) {
                System.out.println("configuration founded on path " + path);
                try {
                    loadLoggingConfiguration(path);

                    return;
                } catch (Exception ex) {
                    System.out.println("unable to load " + path);
                    ex.printStackTrace();
                }
            }
        }

        throw new RuntimeException("Can't load logger configuration on paths " + logbackConfig);
    }

    private static boolean searchLoggerConfiguration(Path logbackConfig) {

        if (!exists(logbackConfig)) {
            System.err.println("Not logback configuration found: " + logbackConfig + ".");
            return false;
        }
        return true;

    }

    private static void loadLoggingConfiguration(Path logbackConfig) {
        LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
        try {
            JoranConfigurator configurator = new JoranConfigurator();
            configurator.setContext(context);
            context.reset();
            configurator.doConfigure(logbackConfig.toFile());
        } catch (JoranException je) {
            propagate(je);
        }
    }

    public static ApplicationBuilder on(final int port) {
        return on(new InetSocketAddress(port));
    }

    private ServerBuilder(InetSocketAddress address, String id) {
        this.address = address;
        this.applicationId = id;
    }

    public ServerBuilder logging(String config) {
        configureLogger(applicationId.toLowerCase(), config);
        return this;
    }

    /**
     *  file:/etc/{applicationId}/{resource}-default.properties
     *  file:/etc/{applicationId}/{resource}.properties
     *  file:{user.home}/.{applicationId}/{resource}.properties
     *  classpath:META-INF/{applicationId}/{resource}.properties
     *  classpath:META-INF/spring/{resource}.properties
     * 
     * @param resource
     * @return
     */
    public ServerBuilder loadConfiguration(String resource) {
        configurations.add(resource);
        return this;
    }

    public ServerBuilder enable(Class<?> feature) {
        features.add(feature);
        return this;
    }

    public <T> T server(Class<T> builderClass) {
        try {
            return (T) instantiateClass(builderClass.getConstructor(ServerBuilder.class), this);
        } catch (BeanInstantiationException | NoSuchMethodException | SecurityException e) {
            throw propagate(e);
        }
    }

    protected AnnotationConfigApplicationContext getContext() {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext();

        final Properties configuration = new Properties();
        if (address() != null) {
            configuration.setProperty("server.host", address().getHostString());
            configuration.setProperty("server.port", String.valueOf(address().getPort()));
        }
        configuration.setProperty("server.id", applicationId);
        applicationContext.getEnvironment().getPropertySources()
                .addFirst(new PropertiesPropertySource("base-configuration", configuration));
        for (String resource : configurations) {
            log.debug("registering configuration {}", resource);
            applicationContext.getEnvironment().getPropertySources()
                    .addLast(new PropertiesPropertySource(resource, loadResource(applicationContext, resource)));
        }
        applicationContext.register(from(concat(common(), features)).toArray(Class.class));
        applicationContext.refresh();

        return applicationContext;
    }

    private ImmutableList<Class> common() {
        return ImmutableList.<Class> of(CommonConfiguration.class);
    }

    private Properties loadResource(AnnotationConfigApplicationContext applicationContext, String resource) {
        ResourceLoader loader = new DefaultResourceLoader(applicationContext.getClassLoader());
        PropertiesFactoryBean factoryBean = new PropertiesFactoryBean(applicationId.toLowerCase(), resource,
                applicationContext.getEnvironment(), loader, false);
        try {
            return factoryBean.getObject();
        } catch (Exception e) {
            throw new RuntimeException("Unable to load resource " + resource, e);
        }
    }
}
