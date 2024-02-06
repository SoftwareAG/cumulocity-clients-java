package com.cumulocity.sdk.client.notification;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UnauthorizedConnectionWatcherTest {

    @Mock
    private ConnectionListener connectionListener;

    @Test
    public void shouldNotReactIfThereAreRetries() {
        // given
        UnauthorizedConnectionWatcher watcher = new UnauthorizedConnectionWatcher(1);
        watcher.addListener(connectionListener);

        // when
        watcher.unauthorizedAccess();

        // then
        verifyNoInteractions(connectionListener);
    }

    @Test
    public void shouldReactWhenNoRetries() {
        // given
        UnauthorizedConnectionWatcher watcher = new UnauthorizedConnectionWatcher(0);
        watcher.addListener(connectionListener);

        // when
        watcher.unauthorizedAccess();

        // then
        verify(connectionListener).onDisconnection(eq(401));
    }

    @Test
    public void shouldNotReactIfThereAreRetriesLeft() {
        // given
        UnauthorizedConnectionWatcher watcher = new UnauthorizedConnectionWatcher(2);
        watcher.addListener(connectionListener);

        // when
        watcher.unauthorizedAccess();
        watcher.unauthorizedAccess();

        // then
        verifyNoInteractions(connectionListener);
    }

    @Test
    public void shouldReactIfThereAreNoRetriesLeft() {
        // given
        UnauthorizedConnectionWatcher watcher = new UnauthorizedConnectionWatcher(2);
        watcher.addListener(connectionListener);

        // when
        watcher.unauthorizedAccess();
        watcher.unauthorizedAccess();
        watcher.unauthorizedAccess();

        // then
        verify(connectionListener).onDisconnection(eq(401));
    }
}
