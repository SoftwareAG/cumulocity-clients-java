package com.cumulocity.microservice.logging.annotation;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.joran.JoranConfigurator;
import ch.qos.logback.core.joran.spi.JoranException;
import ch.qos.logback.core.util.StatusPrinter;
import com.cumulocity.microservice.logging.model.LoggingConfiguration;
import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.base.Supplier;
import com.google.common.base.Throwables;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.util.StringUtils;

import java.io.File;

import static com.google.common.collect.FluentIterable.from;
import static com.google.common.collect.Lists.newArrayList;
import static java.lang.System.getProperty;
import static java.lang.System.getenv;

@Configuration
public class EnableMicroserviceLoggingConfiguration {

    private static final Logger log = LoggerFactory.getLogger(EnableMicroserviceLoggingConfiguration.class);

    public interface ConfigurationFilesProvider extends Supplier<Iterable<Supplier<File>>> {
    }

    private final static class DefaultConfigurationFilesProvider implements ConfigurationFilesProvider {
        private final String directory, file;

        @java.beans.ConstructorProperties({"directory", "file"})
        public DefaultConfigurationFilesProvider(String directory, String file) {
            this.directory = directory;
            this.file = file;
        }

        public Iterable<Supplier<File>> get() {
            return newArrayList(
                    file(getenv(directory.toUpperCase() + "_CONF_DIR"), "." + directory, file),
                    file(getenv(directory.toUpperCase() + "_CONF_DIR"), directory, file),

                    file(getProperty("user.home"), "." + directory, file),

                    file(getenv("CONF_DIR"), "." + directory, file),
                    file(getenv("CONF_DIR"), directory, file),

                    file("/etc", directory, file)
            );
        }
    }

    @Autowired(required = false)
    private ConfigurationFilesProvider configurationFilesProvider;

    @Autowired(required = false)
    private LoggingConfiguration configuration;

    @Value("${application.name:}")
    private String applicationName;

    @EventListener(ContextRefreshedEvent.class)
    public void onStart() {
        try {
            loggingInit().afterPropertiesSet();
        } catch (Exception ex) {
            throw Throwables.propagate(ex);
        }
    }

    @Bean
    public InitializingBean loggingInit() {
        return new InitializingBean() {
            public void afterPropertiesSet() throws Exception {
                if (configuration != null) {
                    setUpConfFiles(configuration.getDirectory(), configuration.getFile());
                } else if (!StringUtils.isEmpty(applicationName)) {
                    setUpConfFiles(applicationName, applicationName  + "-agent-server-logging.xml");
                }
            }
        };
    }

    private void setUpConfFiles(String name, String subname) {
        ConfigurationFilesProvider provider = provider(name, subname);
        setUpConfigFiles(provider);
    }

    private ConfigurationFilesProvider provider(String name, String subname) {
        if (configurationFilesProvider != null) {
            return configurationFilesProvider;
        }
        return new DefaultConfigurationFilesProvider(name, subname);
    }



    private static void setUpConfigFiles(ConfigurationFilesProvider provider) {
        from(provider.get())
                .transform(new Function<Supplier<File>, File>() {
                    public File apply(Supplier<File> fileSupplier) {
                        return fileSupplier.get();
                    }
                })
                .filter(new Predicate<File>() {
                    public boolean apply(File file) {
                        return file != null && file.exists();
                    }
                })
                .first()
                .transform(new Function<File, File>() {
                    public File apply(File file) {
                        return setupLoggingFIle(file);
                    }
                });
    }

    private static File setupLoggingFIle(File file) {
        log.info("logging.conf: {}", file.getAbsoluteFile());

        if (!file.getAbsoluteFile().getName().equals(System.getProperty("logging.conf"))) {
            System.setProperty("logging.conf", file.getAbsolutePath());

            final LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
            try {
                final JoranConfigurator configurator = new JoranConfigurator();
                configurator.setContext(context);
                context.reset();
                configurator.doConfigure(file);
            } catch (final JoranException je) {
                // StatusPrinter will handle this
            }
            StatusPrinter.printInCaseOfErrorsOrWarnings(context);
        }
        return file;
    }

    private static Supplier<File> file(final String home, final String dir, final String filename) {
        return new Supplier<File>() {
            public File get() {
                final File homeFile = new File(home, dir);
                if (homeFile.exists()) {
                    final File file = new File(homeFile, filename);
                    if (file.exists()) {
                        return file;
                    }
                }
                return null;
            }
        };
    }

}
