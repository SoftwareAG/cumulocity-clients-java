package com.cumulocity.sdk.client.interceptor;

import com.cumulocity.model.cep.ProcessingMode;
import com.sun.jersey.api.client.WebResource.Builder;

public class TransientProcessingHttpClientInterceptor extends ProcessingModeHttpClientInterceptor {

    private static TransientProcessingHttpClientInterceptor interceptor;

    private TransientProcessingHttpClientInterceptor() {}

    public static TransientProcessingHttpClientInterceptor getInstance() {
        if (interceptor == null) {
            interceptor = new TransientProcessingHttpClientInterceptor();
        }
        return interceptor;
    }

    @Override
    protected String getProcessingModeName() {
        return ProcessingMode.TRANSIENT.name();
    }
}
