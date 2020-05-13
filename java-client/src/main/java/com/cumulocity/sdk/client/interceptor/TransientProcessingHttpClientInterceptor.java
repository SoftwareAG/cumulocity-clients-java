package com.cumulocity.sdk.client.interceptor;

import com.cumulocity.model.cep.ProcessingMode;
import com.sun.jersey.api.client.WebResource.Builder;

public class TransientProcessingHttpClientInterceptor extends ProcessingModeHttpClientInterceptor {

    @Override
    public Builder apply(Builder builder) {
        builder.header(ProcessingModeHttpClientInterceptor.X_CUMULOCITY_PROCESSING_MODE, ProcessingMode.TRANSIENT.name());
        return builder;
    }
}
