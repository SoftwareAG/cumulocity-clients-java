package com.cumulocity.sdk.client.interceptor;

import com.cumulocity.model.cep.ProcessingMode;
import com.sun.jersey.api.client.WebResource.Builder;

public class QuiescentProcessingHttpClientInterceptor extends ProcessingModeHttpClientInterceptor {

    private static QuiescentProcessingHttpClientInterceptor instance;

    private QuiescentProcessingHttpClientInterceptor() {}

    public static QuiescentProcessingHttpClientInterceptor getInstance() {
        if (instance == null) {
            instance = new QuiescentProcessingHttpClientInterceptor();
        }
        return instance;
    }

    @Override
    protected String getProcessingModeName() {
        return ProcessingMode.QUIESCENT.name();
    }
}
