package com.cumulocity.sdk.client.interceptor;

import com.cumulocity.model.cep.ProcessingMode;

public class PersistenceProcessingMode extends ProcessingModeHttpClientInterceptor {

    private static PersistenceProcessingMode instance;

    private PersistenceProcessingMode() {}

    public static PersistenceProcessingMode getInstance() {
        if (instance == null) {
            instance = new PersistenceProcessingMode();
        }
        return instance;
    }

    @Override
    protected String getProcessingModeName() {
        return ProcessingMode.PERSISTENT.name();
    }
}
