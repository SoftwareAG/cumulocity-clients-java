package com.cumulocity.microservice.logging.configuration;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.joran.JoranConfigurator;
import ch.qos.logback.core.joran.spi.JoranException;
import ch.qos.logback.core.util.StatusPrinter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringApplicationRunListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.util.StringUtils;

import java.io.File;
import java.util.Arrays;

import static java.lang.System.getProperty;
import static java.lang.System.getenv;

@Order(5)
public class LoggingEnvironmentRunListener implements SpringApplicationRunListener {

    private static final Logger log = LoggerFactory.getLogger(LoggingEnvironmentRunListener.class);

    private String applicationName;

    public LoggingEnvironmentRunListener(SpringApplication application, String[] args) {
    }

    @Override
    public void starting() {

    }

    /**
     * for older spring boot versions
     */
    public void started() {
    }

    @Override
    public void environmentPrepared(ConfigurableEnvironment configurableEnvironment) {
        applicationName = configurableEnvironment.getProperty("application.name");
        initLogging();
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

    public void initLogging() {
        if (!StringUtils.isEmpty(applicationName)) {
            log.info("Attempt to configure logback: look in standard locations for log files");
            setUpConfigFiles();
        } else {
            log.warn("Application name not provided, cannot configure logback using standard locations");
        }
    }

    private void setUpConfigFiles() {
        for(File file:getFiles()){
            if(file != null && file.exists()){
                setupLoggingFile(file);
                return;
            }
        }
    }

    public Iterable<File> getFiles() {
        return Arrays.asList(
                file(getenv(applicationName.toUpperCase() + "_CONF_DIR"), "." + applicationName, applicationName + "-agent-server-logging.xml"),
                file(getenv(applicationName.toUpperCase() + "_CONF_DIR"), "." + applicationName, "logging.xml"),
                file(getenv(applicationName.toUpperCase() + "_CONF_DIR"), applicationName, applicationName + "-agent-server-logging.xml"),
                file(getenv(applicationName.toUpperCase() + "_CONF_DIR"), applicationName, "logging.xml"),

                file(getProperty("user.home"), "." + applicationName, applicationName + "-agent-server-logging.xml"),
                file(getProperty("user.home"), "." + applicationName, "logging.xml"),

                file(getenv("CONF_DIR"), "." + applicationName, applicationName + "-agent-server-logging.xml"),
                file(getenv("CONF_DIR"), "." + applicationName, "logging.xml"),
                file(getenv("CONF_DIR"), applicationName, applicationName + "-agent-server-logging.xml"),
                file(getenv("CONF_DIR"), applicationName, "logging.xml"),

                file("/etc", applicationName, applicationName + "-agent-server-logging.xml"),
                file("/etc", applicationName, applicationName + "-server-logging.xml"),
                file("/etc", applicationName, applicationName + "-logging.xml"),

                file("/etc", applicationName, "logging.xml")
        );
    }

    private static File setupLoggingFile(File file) {
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

    private static File file(final String home, final String dir, final String filename) {
        final File homeFile = new File(home, dir);
        if (homeFile.exists()) {
            final File file = new File(homeFile, filename);
            if (file.exists()) {
                return file;
            }
        }
        return null;
    }

}
