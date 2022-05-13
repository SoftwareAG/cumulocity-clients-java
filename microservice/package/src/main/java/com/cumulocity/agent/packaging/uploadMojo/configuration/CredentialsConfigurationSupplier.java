package com.cumulocity.agent.packaging.uploadMojo.configuration;

import com.cumulocity.agent.packaging.uploadMojo.configuration.common.ServerUtils;
import java.util.Optional;
import java.util.stream.Stream;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
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
            builder.password(getSettingsPassword().orElse(null));
        }

        if (StringUtils.isBlank(source.getUsername())) {
            builder.username(getSettingsUsername().orElse(null));
        }

        if (StringUtils.isBlank(source.getUrl())) {
            builder.url(getSettingsUrl().orElse(null));
        }

        final CredentialsConfiguration result = builder.build();
        if (result.isPresent()) {
            return Optional.of(result);
        }
        return Optional.empty();
    }

    private Optional<String> getSettingsUsername() {
        return getServer()
                .map(getServerUsername())
                .filter(ServerUtils.isNotBlank())
                .findFirst();
    }

    private Optional<String> getSettingsPassword() {
        return getServer()
                .map(getServerPassword())
                .filter(ServerUtils.isNotBlank())
                .findFirst();
    }

    private Optional<String> getSettingsUrl() {
        return getServer()
                .map(getConfigurationString("url"))
                .filter(ServerUtils.isNotBlank())
                .findFirst();
    }

    private Stream<Server> getServer() {
        final Server server = settings.getServer(serviceId);
        if (server == null) {
            return Stream.of();
        }
        return Stream.of(server);
    }
}
