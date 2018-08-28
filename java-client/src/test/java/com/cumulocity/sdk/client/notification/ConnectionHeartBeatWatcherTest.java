package com.cumulocity.sdk.client.notification;

import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static org.joda.time.DateTimeUtils.currentTimeMillis;
import static org.joda.time.DateTimeUtils.setCurrentMillisFixed;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

public class ConnectionHeartBeatWatcherTest {

    public static final int HEARTBEAT_INTERVAL = 12;
    private ScheduledExecutorService executorService = Mockito.mock(ScheduledExecutorService.class);

    private final ConnectionHeartBeatWatcher watcher = new ConnectionHeartBeatWatcher(executorService, TimeUnit.MINUTES.toSeconds(HEARTBEAT_INTERVAL));

    private final ArgumentCaptor<Runnable> captor = ArgumentCaptor.forClass(Runnable.class);

    private ConnectionIdleListener listener = Mockito.mock(ConnectionIdleListener.class);
    ;

    @Test
    public void shouldDontSendNotificationWhenConnectionIsActive() {
        //Given
        watcher.addConnectionListener(listener);
        //When
        watcher.start();
        verifyWatcherStarted();
        executeWatcherTask();
        //Then
        verify(listener, never()).onConnectionIdle();
    }

    @Test
    public void shouldDontSendNotificationWhenRecivedHeartBeat() {
        //Given
        watcher.addConnectionListener(listener);
        stopTime();
        //When
        watcher.start();
        verifyWatcherStarted();
        moveTimeForward(HEARTBEAT_INTERVAL + 1);
        watcher.heartBeat();
        executeWatcherTask();
        //Then
        verify(listener, never()).onConnectionIdle();
    }

    private void stopTime() {
        setCurrentMillisFixed(currentTimeMillis());
    }

    private void moveTimeForward(final int minutes) {
        setCurrentMillisFixed(currentTimeMillis() + TimeUnit.MILLISECONDS.convert(minutes, TimeUnit.MINUTES));
    }

    @Test
    public void shouldSendNotificationWhenHeartBeatNotRecived() {
        //Given
        watcher.addConnectionListener(listener);
        stopTime();
        //When
        watcher.start();
        verifyWatcherStarted();
        moveTimeForward(HEARTBEAT_INTERVAL + 1);
        executeWatcherTask();
        //Then
        verify(listener).onConnectionIdle();
    }

    private void executeWatcherTask() {
        captor.getValue().run();
    }

    private void verifyWatcherStarted() {
        verify(executorService).scheduleWithFixedDelay(captor.capture(), Mockito.anyLong(), Mockito.anyLong(), Mockito.any(TimeUnit.class));
    }

}
