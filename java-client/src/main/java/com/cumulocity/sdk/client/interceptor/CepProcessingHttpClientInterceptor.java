package com.cumulocity.sdk.client.interceptor;

import com.cumulocity.model.cep.ProcessingMode;
import com.sun.jersey.api.client.WebResource.Builder;

public class CepProcessingHttpClientInterceptor extends ProcessingModeHttpClientInterceptor {

    @Override
    public Builder apply(Builder builder) {
        builder.header(X_CUMULOCITY_PROCESSING_MODE, ProcessingMode.CEP.name());
        return builder;
    }
}
