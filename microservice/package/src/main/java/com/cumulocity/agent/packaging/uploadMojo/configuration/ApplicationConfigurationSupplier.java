package com.cumulocity.agent.packaging.uploadMojo.configuration;

import com.cumulocity.agent.packaging.uploadMojo.configuration.common.ServerUtils;
import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang.StringUtils;
import org.apache.maven.project.MavenProject;
import org.apache.maven.settings.Server;
import org.apache.maven.settings.Settings;

import java.util.List;

@RequiredArgsConstructor
public class ApplicationConfigurationSupplier {

    private final String serviceId;
    private final Settings settings;
    private final ApplicationConfiguration application;
    private final String packageName;
    private final MavenProject project;

    @Getter(lazy = true)
    private final ApplicationConfiguration object = get();

    private ApplicationConfiguration get() {
        Optional<ApplicationConfiguration> settingsConfig = getSettingsConfig();

        ApplicationConfiguration.ApplicationConfigurationBuilder builder = this.application.toBuilder();

//        application name
        if (StringUtils.isBlank(application.getName())) {
            if (settingsConfig.isPresent()) {
                String value = settingsConfig.get().getName();
                if (StringUtils.isNotBlank(value)) {
                    builder.name(value);
                } else {
                    builder.name(packageName);
                }
            }
        }
//        delete
        if (application.getDelete() == null) {
            if (settingsConfig.isPresent()) {
                Boolean value = settingsConfig.get().getDelete();
                if (value != null) {
                    builder.delete(value);
                } else {
                    builder.delete(true);
                }
            }
        }

//        create
        if (application.getCreate() == null) {
            if (settingsConfig.isPresent()) {
                Boolean value = settingsConfig.get().getCreate();
                if (value != null) {
                    builder.create(value);
                } else {
                    builder.create(true);
                }
            }
        }

//        fail on errors
        if (application.getFailOnError() == null) {
            if (settingsConfig.isPresent()) {
                Boolean value = settingsConfig.get().getFailOnError();
                if (value != null) {
                    builder.failOnError(value);
                } else {
                    builder.failOnError(true);
                }
            }
        }

//        subscriptions
        if (application.getSubscriptions() == null) {
            if (settingsConfig.isPresent()) {
                List<String> subscriptions = settingsConfig.get().getSubscriptions();
                if (subscriptions != null) {
                    builder.subscriptions(subscriptions);
                } else {
                    builder.subscriptions(Lists.<String>newArrayList());
                }
            }
        }

        return builder.build();
    }

    private Optional<ApplicationConfiguration> getSettingsConfig() {
        return getServer()
                .transformAndConcat(ServerUtils.getConfigurationList(ApplicationConfiguration.class, "applications"))
                .filter(new Predicate<ApplicationConfiguration>() {
                    public boolean apply(ApplicationConfiguration input) {
                        if (input.getGroupId() != null && !input.getGroupId().equals(project.getGroupId())) {
                            return false;
                        }
                        if (input.getArtifactId() != null && !input.getArtifactId().equals(project.getArtifactId())) {
                            return false;
                        }
                        return true;
                    }
                })
                .first();
    }

    private FluentIterable<Server> getServer() {
        final Server server = settings.getServer(serviceId);
        if (server == null) {
            return FluentIterable.of();
        }
        return FluentIterable.of(server);
    }
}
