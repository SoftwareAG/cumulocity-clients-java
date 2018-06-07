package com.cumulocity.microservice.health.indicator;

import com.cumulocity.microservice.context.ContextService;
import com.cumulocity.microservice.context.credentials.Credentials;
import com.cumulocity.microservice.context.credentials.MicroserviceCredentials;
import com.cumulocity.microservice.subscription.model.core.PlatformProperties;
import com.cumulocity.sdk.client.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.actuate.health.AbstractHealthIndicator;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.Status;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.Map;
import java.util.Map.Entry;

import static javax.ws.rs.core.MediaType.*;

@Slf4j
@Component
@ConditionalOnProperty(value = "management.health.platform.enabled", havingValue = "true", matchIfMissing = true)
@RequiredArgsConstructor
public class PlatformHealthIndicator extends AbstractHealthIndicator {

    private final ContextService<MicroserviceCredentials> microservice;
    private final PlatformProperties properties;
    private final PlatformHealthIndicatorProperties configuration;

    @Override
    @SuppressWarnings("unchecked")
    protected void doHealthCheck(Health.Builder builder) {
        try (final Platform platform = platform().build()) {
            final RestOperations rest = platform.rest();
            try {
                final Map<String, Object> health = rest.get(configuration.getPath(), APPLICATION_JSON_TYPE, Map.class);

                try {
                    rest.get(configuration.getPath(), APPLICATION_JSON_TYPE, Map.class);
                    builder.up();
                } catch (SDKException ex) {
                    throw ex;
                }

                if (configuration.getDetails().getEnabled()) {
                    final Map<String, Object> details = rest.get(configuration.getDetails().getPath(), APPLICATION_JSON_TYPE, Map.class);
                    if (details.containsKey("status")) {
                        // tenant health endpoint contains detailed information about database connections
                        assignDetails(builder, details);
                    }
                }

            } catch (SDKException ex) {
                if (ex.getHttpStatus() == HttpStatus.FORBIDDEN.value()) {
                    builder.unknown().withDetail("message", ex.getMessage());
                } else {
                    builder.down(ex);
                }
            }
            log.debug("health {}", builder.build());
        }
    }

    private void assignDetails(Health.Builder builder, Map<String, Object> health) {

        for (final Entry<String, Object> entry : health.entrySet()) {
            if (!entry.getKey().equals("status")) {
                builder.withDetail(entry.getKey(), entry.getValue());
            }
        }
    }

    private PlatformBuilder platform() {
        final ObjectMapper mapper = new ObjectMapper();
        final Credentials context = getCredentials();
        return PlatformBuilder.platform()
                .withBaseUrl(properties.getUrl().get())
                .withUsername(context.getUsername())
                .withPassword(context.getPassword())
                .withTenant(context.getTenant())
                .withResponseMapper(new ResponseMapper() {
                    @SneakyThrows
                    public <T> T read(InputStream stream, Class<T> clazz) {
                        return mapper.readValue(stream, clazz);
                    }

                    @SneakyThrows
                    public String write(Object object) {
                        return mapper.writeValueAsString(object);
                    }
                });
    }

    private Credentials getCredentials() {
        if (microservice.isInContext()) {
            return microservice.getContext();
        }
        return properties.getMicroserviceBoostrapUser();
    }
}