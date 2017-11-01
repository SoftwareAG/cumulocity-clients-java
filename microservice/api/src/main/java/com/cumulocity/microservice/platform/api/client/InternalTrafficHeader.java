package com.cumulocity.microservice.platform.api.client;

import com.cumulocity.sdk.client.interceptor.HttpClientInterceptor;
import com.sun.jersey.api.client.WebResource;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(of = "key")
public class InternalTrafficHeader implements HttpClientInterceptor {

    public static final InternalTrafficHeader EMPTY = from("EMPTY");
    private final String key = "X-Cumulocity-Internal-Traffic";
    private final String value;

    private InternalTrafficHeader(String value) {
        this.value = value;
    }

    public static InternalTrafficHeader from(String value) {
        return new InternalTrafficHeader(value);
    }

    @Override
    public WebResource.Builder apply(WebResource.Builder builder) {
        return builder.header(key, value);
    }
    
}
