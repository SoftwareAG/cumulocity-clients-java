package com.cumulocity.sdk.client.notification;

import org.glassfish.jersey.client.ClientAsyncExecutor;
import org.glassfish.jersey.spi.ExecutorServiceProvider;

import java.util.concurrent.ExecutorService;

@ClientAsyncExecutor
public class LongPollingExecutorServiceProvider implements ExecutorServiceProvider {

    private final ExecutorService executorService;

    public LongPollingExecutorServiceProvider(ExecutorService executorService) {
        this.executorService = executorService;
    }

    @Override
    public ExecutorService getExecutorService() {
        return executorService;
    }

    @Override
    public void dispose(ExecutorService executorService) {
        // do nothing, executor service lifecycle is being managed by CumulocityLongPollingTransport
    }
}
