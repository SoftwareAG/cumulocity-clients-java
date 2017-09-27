package com.cumulocity.agent.server.context.scope;

import com.cumulocity.agent.server.context.DeviceContextScope;
import com.cumulocity.agent.server.context.DeviceContextService;
import com.cumulocity.agent.server.context.DeviceCredentials;
import lombok.SneakyThrows;
import org.junit.Test;
import org.springframework.beans.factory.ObjectFactory;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.powermock.api.mockito.PowerMockito.when;

public class BaseScopeTest {
    final Executor executor = Executors.newFixedThreadPool(3);

    @Test
    @SneakyThrows
    public void shouldSynchronize() {
        final AtomicInteger integer = new AtomicInteger(0);
        final DeviceCredentials credentials = mock(DeviceCredentials.class);
        final DeviceContextService contextService = mock(DeviceContextService.class);
        when(contextService.getCredentials()).thenReturn(credentials);
        final DeviceContextScope deviceScope = new DeviceContextScope(contextService, null) {
            @SneakyThrows
            protected Object doGet(String name, ObjectFactory<?> objectFactory) {
                integer.incrementAndGet();

                Thread.sleep(2000);
                return null;
            }
        };

        final Runnable task = new Runnable() {
            public void run() {
                deviceScope.get("connector", null);
            }
        };

        executor.execute(task);
        executor.execute(task);

        Thread.sleep(1000);

        assertThat(integer.get()).isEqualTo(1);
    }
}
