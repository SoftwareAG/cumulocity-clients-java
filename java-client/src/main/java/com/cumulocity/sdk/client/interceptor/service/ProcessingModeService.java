package com.cumulocity.sdk.client.interceptor.service;

import com.cumulocity.model.cep.ProcessingMode;
import com.cumulocity.sdk.client.PlatformParameters;
import com.cumulocity.sdk.client.RestConnector;
import com.cumulocity.sdk.client.interceptor.*;

import java.util.Set;

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
            processingModeHttpClientInterceptor = getByProcessingMode(processingMode);
            platformParameters.registerInterceptor(processingModeHttpClientInterceptor);
        }
    }

    public void unregister() {
        if (processingModeHttpClientInterceptor != null) {
            PlatformParameters platformParameters = restConnector.getPlatformParameters();
            platformParameters.unregisterInterceptor(processingModeHttpClientInterceptor);
            processingModeHttpClientInterceptor = null;
        }
    }

    public static ProcessingModeHttpClientInterceptor getByProcessingMode(ProcessingMode processingMode) {
        switch (processingMode) {
            case TRANSIENT:
                return TransientProcessingHttpClientInterceptor.getInstance();
            case QUIESCENT:
                return QuiescentProcessingHttpClientInterceptor.getInstance();
            case CEP:
                return CepProcessingHttpClientInterceptor.getInstance();
            default:
                return PersistenceProcessingMode.getInstance();
        }
    }

    public Set<HttpClientInterceptor> getHttpInterceptors() {
        return restConnector.getPlatformParameters().getInterceptorSet();
    }

    public PlatformParameters getPlatformParameters() {
        return restConnector.getPlatformParameters();
    }
}
