package com.cumulocity.agent.packaging.uploadMojo.configuration;

import com.cumulocity.agent.packaging.uploadMojo.configuration.common.ServerUtils;
import com.google.common.base.Optional;
import com.google.common.collect.FluentIterable;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang.StringUtils;
import org.apache.maven.settings.Server;
import org.apache.maven.settings.Settings;

import static com.cumulocity.agent.packaging.uploadMojo.configuration.common.ServerUtils.*;

@RequiredArgsConstructor
public class CredentialsConfigurationSupplier {

    private final String serviceId;
    private final Settings settings;
    private final CredentialsConfiguration source;

    @Getter(lazy = true)
    private final Optional<CredentialsConfiguration> object = get();

    /**
     * Returns configuration from credentials property. Looks up for defaults in settings xml.
     */
    private Optional<CredentialsConfiguration> get() {
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

        final CredentialsConfiguration result = builder.build();
        if (result.isPresent()) {
            return Optional.of(result);
        }
        return Optional.absent();
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
