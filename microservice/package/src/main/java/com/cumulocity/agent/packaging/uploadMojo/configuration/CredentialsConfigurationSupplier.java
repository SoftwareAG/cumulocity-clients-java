package com.cumulocity.agent.packaging.uploadMojo.configuration;

import com.cumulocity.agent.packaging.uploadMojo.configuration.common.ServerUtils;
import com.google.common.base.Optional;
import com.google.common.collect.FluentIterable;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang.StringUtils;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.settings.Server;
import org.apache.maven.settings.Settings;

import static com.cumulocity.agent.packaging.uploadMojo.configuration.common.ServerUtils.getConfigurationString;
import static com.cumulocity.agent.packaging.uploadMojo.configuration.common.ServerUtils.getServerPassword;
import static com.cumulocity.agent.packaging.uploadMojo.configuration.common.ServerUtils.getServerUsername;

@RequiredArgsConstructor
public class CredentialsConfigurationSupplier {

    private final String serviceId;
    private final Settings settings;
    private final CredentialsConfiguration source;
    private final Log log;

    @Getter(lazy = true)
    private final CredentialsConfiguration object = get();

    /**
     * Returns configuration from credentials property. Looks up for defaults in settings xml.
     */
    private CredentialsConfiguration get() {
        final CredentialsConfiguration.CredentialsConfigurationBuilder builder = source.toBuilder();

        if (StringUtils.isBlank(source.getPassword())) {
            builder.password(getSettingsPassword().orNull());
        }

        if (StringUtils.isBlank(source.getUsername())) {
            builder.username(getSettingsUsername().orNull());
        }

        if (StringUtils.isBlank(source.getUrl())) {
            builder.url(getSettingsUrl().orNull());
        }

        return builder.build();
    }

    private Optional<String> getSettingsUsername() {
        return getServer()
                .transform(getServerUsername())
                .filter(ServerUtils.isNotBlank())
                .first();
    }

    private Optional<String> getSettingsPassword() {
        return getServer()
                .transform(getServerPassword())
                .filter(ServerUtils.isNotBlank())
                .first();
    }

    private Optional<String> getSettingsUrl() {
        return getServer()
                .transform(getConfigurationString("url"))
                .filter(ServerUtils.isNotBlank())
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
