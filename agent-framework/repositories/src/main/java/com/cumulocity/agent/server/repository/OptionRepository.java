package com.cumulocity.agent.server.repository;

import static com.cumulocity.rest.representation.tenant.OptionMediaType.OPTION;

import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.cumulocity.rest.representation.tenant.OptionRepresentation;
import com.cumulocity.sdk.client.RestConnector;
import com.cumulocity.sdk.client.SDKException;

@Component
public class OptionRepository {

    private static final String TENANT_OPTIONS_URL = "/tenant/options";

    private final RestConnector rest;

    private final String baseUrl;

    @Autowired
    public OptionRepository(RestConnector rest, @Value("${C8Y.baseURL}") String baseUrl) {
        this.rest = rest;
        this.baseUrl = baseUrl;
    }

    public OptionRepresentation find(String category, String key) {
        try {
            return rest.get(optionUrl(category, key), OPTION, OptionRepresentation.class);
        } catch (SDKException ex) {
            if (ex.getHttpStatus() == HttpStatus.SC_NOT_FOUND) {
                return null;
            } else {
                throw ex;
            }
        }
    }

    public void delete(String category, String key) {
        rest.delete(optionUrl(category, key));
    }

    private String optionUrl(String category, String key) {
        return baseUrl + TENANT_OPTIONS_URL + "/" + category + "/" + key;
    }

    public OptionRepresentation save(OptionRepresentation option) {
        return rest.put(optionUrl(option.getCategory(), option.getKey()), OPTION, option);
    }
}
