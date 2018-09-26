package com.cumulocity.microservice.health.indicator.platform;

import com.cumulocity.microservice.context.ContextService;
import com.cumulocity.microservice.context.credentials.Credentials;
import com.cumulocity.microservice.context.credentials.MicroserviceCredentials;
import com.cumulocity.microservice.subscription.model.core.PlatformProperties;
import com.cumulocity.model.authentication.CumulocityCredentials;
import com.cumulocity.model.authentication.CumulocityLogin;
import com.cumulocity.sdk.client.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.jersey.api.client.ClientResponse;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.actuate.health.AbstractHealthIndicator;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.Map;
import java.util.Map.Entry;

import static java.lang.String.format;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON_TYPE;
import static org.apache.commons.httpclient.HttpStatus.SC_OK;

@Slf4j
@Component
@ConditionalOnProperty(value = "management.health.platform.enabled", havingValue = "true")
@RequiredArgsConstructor
public class PlatformHealthIndicator extends AbstractHealthIndicator {

    private final ContextService<MicroserviceCredentials> microservice;
    private final PlatformProperties properties;
    private final PlatformHealthIndicatorProperties configuration;

    @Override
    @SuppressWarnings("unchecked")
    protected void doHealthCheck(Health.Builder builder) {
        try {
            final RestConnector rest = new RestConnector(platformParameters(), new ResponseParser(new JacksonResponseMapper()));
            final ClientResponse response = rest.get(configuration.getPath(), APPLICATION_JSON_TYPE);
            if (response.getStatus() == SC_OK) {
                builder.up();
            } else {
                builder.unknown().withDetail("message", format("Platform respond with wrong status %s", response.getStatus()));
            }

            if (configuration.getDetails().getEnabled() && response.getStatus() == SC_OK) {
                final Map<String, Object> details = rest.get(configuration.getDetails().getPath(), APPLICATION_JSON_TYPE, Map.class);
                if (details.containsKey("status")) {
                    // tenant health endpoint contains detailed information about database connections
                    assignDetails(builder, details);
                }
            }
            log.debug("health {}", builder.build());
        } catch (Exception ex) {
            log.warn("Error while checking connection to the platform", ex);
            builder.down(ex).withDetail("message", ex.getMessage());
        }
    }

    private void assignDetails(Health.Builder builder, Map<String, Object> health) {

        for (final Entry<String, Object> entry : health.entrySet()) {
            if (!entry.getKey().equals("status")) {
                builder.withDetail(entry.getKey(), entry.getValue());
            }
        }
    }

    private PlatformParameters platformParameters() {
        final Credentials context = getCredentials();

        final CumulocityCredentials credentials = new CumulocityCredentials(
                CumulocityLogin.fromLoginString(context.getTenant() + "/" + context.getUsername()),
                context.getPassword(),
                context.getOAuthAccessToken(),
                context.getXsrfToken(),
                context.getAppKey(),
                null
        );
        final PlatformParameters params = new PlatformParameters(properties.getUrl().get(), credentials, new ClientConfiguration());
        params.setForceInitialHost(properties.getForceInitialHost());
        params.setTfaToken(context.getTfaToken());
        return params;
    }

    private Credentials getCredentials() {
        if (microservice.isInContext()) {
            return microservice.getContext();
        }
        return properties.getMicroserviceBoostrapUser();
    }

    private static final class JacksonResponseMapper implements ResponseMapper {
        private final ObjectMapper mapper;

        public JacksonResponseMapper() {
            this.mapper = new ObjectMapper();
        }

        @SneakyThrows
        public <T> T read(InputStream stream, Class<T> clazz) {
            return mapper.readValue(stream, clazz);
        }

        @SneakyThrows
        public String write(Object object) {
            return mapper.writeValueAsString(object);
        }
    }
}