package com.cumulocity.agent.server.repository;

import com.cumulocity.sdk.client.RestConnector;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.ws.rs.core.Response;

import static com.cumulocity.rest.representation.tenant.OptionMediaType.OPTION_COLLECTION;

@Component
public class SystemOptionRepository {

    public static final String SYSTEM_OPTION_COLLECTION_RESOURCE_URL = "/tenant/system/options";

    private final RestConnector rest;
    private final String baseUrl;

    @Autowired
    public SystemOptionRepository(RestConnector rest, @Value("${C8Y.baseURL}") String baseUrl) {
        this.rest = rest;
        this.baseUrl = baseUrl;
    }

    public boolean exists() {
        return rest.getStatus(url(), OPTION_COLLECTION) == Response.Status.OK;
    }

    private String url() {
        return baseUrl + SYSTEM_OPTION_COLLECTION_RESOURCE_URL;
    }
}
