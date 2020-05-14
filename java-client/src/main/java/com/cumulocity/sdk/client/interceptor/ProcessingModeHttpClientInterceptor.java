package com.cumulocity.sdk.client.interceptor;

import com.sun.jersey.api.client.WebResource;

public abstract class ProcessingModeHttpClientInterceptor implements HttpClientInterceptor {

    public static final String X_CUMULOCITY_PROCESSING_MODE = "X-Cumulocity-Processing-Mode";

    @Override
    public WebResource.Builder apply(WebResource.Builder builder) {
        builder.header(X_CUMULOCITY_PROCESSING_MODE, getProcessingModeName());
        return builder;
    }

    protected abstract String getProcessingModeName();
}
