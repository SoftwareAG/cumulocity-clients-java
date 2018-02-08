package com.cumulocity.microservice.health.indicator;

import com.cumulocity.microservice.context.ContextService;
import com.cumulocity.microservice.context.credentials.Credentials;
import com.cumulocity.microservice.context.credentials.MicroserviceCredentials;
import com.cumulocity.microservice.subscription.model.core.PlatformProperties;
import com.cumulocity.sdk.client.Platform;
import com.cumulocity.sdk.client.PlatformBuilder;
import com.cumulocity.sdk.client.ResponseMapper;
import com.cumulocity.sdk.client.RestOperations;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.actuate.health.AbstractHealthIndicator;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.Status;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.Map;
import java.util.Map.Entry;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON_TYPE;

@Slf4j
@Component
@RequiredArgsConstructor
public class PlatformHealthIndicator extends AbstractHealthIndicator {

    private final ContextService<MicroserviceCredentials> microservice;
    private final PlatformProperties properties;

    @Override
    @SuppressWarnings("unchecked")
    protected void doHealthCheck(Health.Builder builder) {
        try (final Platform platform = platform().build()) {
            final RestOperations rest = platform.rest();
            final Map<String, Object> health = rest.get("/tenant/health", APPLICATION_JSON_TYPE, Map.class);

            builder.status(new Status(String.valueOf(health.get("status"))));
            for (final Entry<String, Object> entry : health.entrySet()) {
//                tenant health endpoint contains detailed information about database connections
                builder.withDetail(entry.getKey(), entry.getValue());
            }

            log.debug("health {}", builder.build());
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