package com.cumulocity.sdk.client.interceptor;

import com.cumulocity.model.cep.ProcessingMode;
import com.sun.jersey.api.client.WebResource.Builder;

public class CepProcessingHttpClientInterceptor extends ProcessingModeHttpClientInterceptor {

    private static CepProcessingHttpClientInterceptor instance;

    private CepProcessingHttpClientInterceptor() {}

    public static CepProcessingHttpClientInterceptor getInstance() {
        if (instance == null) {
            instance = new CepProcessingHttpClientInterceptor();
        }
        return instance;
    }

    @Override
    protected String getProcessingModeName() {
        return ProcessingMode.CEP.name();
    }
}
