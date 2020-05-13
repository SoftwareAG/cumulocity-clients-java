package com.cumulocity.sdk.client.interceptor.service;

import com.cumulocity.model.cep.ProcessingMode;
import com.cumulocity.sdk.client.PlatformParameters;
import com.cumulocity.sdk.client.RestConnector;
import com.cumulocity.sdk.client.interceptor.CepProcessingHttpClientInterceptor;
import com.cumulocity.sdk.client.interceptor.ProcessingModeHttpClientInterceptor;
import com.cumulocity.sdk.client.interceptor.QuiescentProcessingHttpClientInterceptor;
import com.cumulocity.sdk.client.interceptor.TransientProcessingHttpClientInterceptor;

public abstract class ProcessingModeService {

    private ProcessingModeHttpClientInterceptor processingModeHttpClientInterceptor;

    private RestConnector restConnector;

    public ProcessingModeService(RestConnector restConnector) {
        this.restConnector = restConnector;
    }

    protected abstract boolean isSupportedProcessingMode(ProcessingMode processingMode);

    public void register(ProcessingMode processingMode) {
        if (isSupportedProcessingMode(processingMode)) {
            PlatformParameters platformParameters = restConnector.getPlatformParameters();
            switch (processingMode) {
                case TRANSIENT:
                    processingModeHttpClientInterceptor = new TransientProcessingHttpClientInterceptor();
                    platformParameters.registerInterceptor(processingModeHttpClientInterceptor);
                    break;
                case QUIESCENT:
                    processingModeHttpClientInterceptor = new QuiescentProcessingHttpClientInterceptor();
                    platformParameters.registerInterceptor(processingModeHttpClientInterceptor);
                    break;
                case CEP:
                    processingModeHttpClientInterceptor = new CepProcessingHttpClientInterceptor();
                    platformParameters.registerInterceptor(processingModeHttpClientInterceptor);
                    break;
            }
        }
    }

    public void unregister() {
        if (processingModeHttpClientInterceptor != null) {
            PlatformParameters platformParameters = restConnector.getPlatformParameters();
            platformParameters.unregisterInterceptor(processingModeHttpClientInterceptor);
            processingModeHttpClientInterceptor = null;
        }
    }
}
