package com.cumulocity.sdk.services.client.rest;

import org.springframework.web.client.RestTemplate;

import com.cumulocity.model.authentication.CumulocityCredentials;

public class RestController {

    public RestTemplate authorizedTemplate(CumulocityCredentials credentials) {
        final RestTemplate restTemplate = new RestTemplate();
        restTemplate.getInterceptors().add(new AuthorizationInterceptor(credentials));
        return restTemplate;
    }
}
