package com.cumulocity.lpwan.util;

import com.cumulocity.microservice.context.credentials.MicroserviceCredentials;
import com.cumulocity.microservice.subscription.service.MicroserviceSubscriptionsService;
import lombok.*;

import java.util.Collection;
import java.util.Optional;
import java.util.concurrent.Callable;

@AllArgsConstructor
@NoArgsConstructor
public class ImmediateMicroserviceSubscriptionsService implements MicroserviceSubscriptionsService {

    @Setter
    @Getter
    private String tenant;

    @Override
    public void runForEachTenant(Runnable runnable) {
        runnable.run();
    }

    @Override
    public void runForTenant(String tenant, Runnable runnable) {
        runnable.run();
    }

    @Override
    @SneakyThrows
    public <T> T callForTenant(String tenant, Callable<T> callable) {
        return callable.call();
    }

    @Override
    public void subscribe() {

    }

    @Override
    public Collection<MicroserviceCredentials> getAll() {
        return null;
    }

    @Override
    public Optional<MicroserviceCredentials> getCredentials(String tenant) {
        return null;
    }

    @Override public boolean isRegisteredSuccessfully() {
        return true;
    }
}
