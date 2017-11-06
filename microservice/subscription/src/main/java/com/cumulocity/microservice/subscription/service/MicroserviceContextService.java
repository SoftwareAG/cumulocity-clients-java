package com.cumulocity.microservice.subscription.service;

import com.cumulocity.agent.server.context.DeviceContext;
import com.cumulocity.agent.server.context.DeviceContextService;
import com.cumulocity.agent.server.context.DeviceCredentials;
import com.cumulocity.microservice.subscription.model.core.Credentials;
import com.cumulocity.microservice.subscription.model.core.MicroserviceCredentials;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.concurrent.Callable;

@Lazy
@Service
@RequiredArgsConstructor
public class MicroserviceContextService {
    public interface Runnable {
//        can throw exception
        void run() throws Exception;
    }

    private final DeviceContextService deviceContextService;

    @SneakyThrows
    public <V> V callWithiContext(Credentials credentials, Callable<V> callable) {
        final DeviceCredentials deviceCredentials = new DeviceCredentials(
                credentials.getTenant(),
                credentials.getName(),
                credentials.getPassword(),
                null,
                null);
        return deviceContextService.callWithinContext(new DeviceContext(deviceCredentials), callable);
    }

    public void runWithinContext(Credentials credentials, final Runnable runnable) {
        callWithiContext(credentials, new Callable<Void>() {
            public Void call() throws Exception {
                runnable.run();
                return null;
            }
        });
    }

    public MicroserviceCredentials getCredentials() {
        final DeviceCredentials credentials = deviceContextService.getCredentials();
        return new MicroserviceCredentials(credentials.getTenant(), credentials.getUsername(), credentials.getPassword());
    }

}
