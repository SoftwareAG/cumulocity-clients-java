package com.cumulocity.sdk.client.notification;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

import java.io.ByteArrayInputStream;
import java.text.ParseException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;

import net.sf.cglib.beans.ImmutableBean;

import org.cometd.bayeux.Message;
import org.cometd.bayeux.Message.Mutable;
import org.cometd.client.transport.TransportListener;
import org.cometd.common.TransportException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.google.common.base.Ascii;
import com.google.common.collect.ImmutableList;
import com.sun.jersey.api.client.ClientResponse;

@RunWith(MockitoJUnitRunner.class)
public class MessageExchangeTest {

    @Mock
    private CumulocityLongPollingTransport transport;

    @Mock
    private ConnectionHeartBeatWatcher watcher;

    @Mock
    private Message message;

    @Mock
    private TransportListener listener;

    @Mock
    private ClientResponse response;

    @Mock
    private ScheduledExecutorService executorService;

    private MessageExchange exchange;

    @Before
    public void setup() {
        exchange = new MessageExchange(transport, executorService, listener, watcher, message);
        when(transport.removeExchange(exchange)).thenReturn(true);
    }

    @After
    public void tearDown() {
        executorService.shutdown();
    }

    @Test
    public void shouldFailMessagesWhenRequestCannceled() throws InterruptedException {
        //Given
        Future<ClientResponse> f = mock(Future.class);
        when(f.isCancelled()).thenReturn(true);
        //When
        exchange.onComplete(f);
        //Then
        verify(listener).onConnectException(any(RuntimeException.class), eq(new Message[] { message }));
        verify(watcher).start();
        verify(watcher).stop();
    }

    @Test
    public void shouldStopWatcherAndNotifyWhenCancel() throws InterruptedException, ExecutionException {
        //Given
        Future<ClientResponse> f = mock(Future.class);
        exchange.setRequest(f);
        //When
        exchange.cancel();
        //Then
        verify(listener).onConnectException(any(RuntimeException.class), eq(new Message[] { message }));
        verify(watcher).stop();
    }

    @Test
    public void shouldScheduleTaskWhenReciveReponse() throws InterruptedException, ExecutionException {
        //Given
        Future<ClientResponse> f = mock(Future.class);
        when(f.get()).thenReturn(response);
        //When
        exchange.onComplete(f);
        //Then
        verify(executorService).submit(exchange);
    }

    @Test
    public void shouldNotfyAboutFailWhenRequestFailed() throws InterruptedException, ExecutionException {
        //Given
        Future<ClientResponse> f = mock(Future.class);
        when(f.get()).thenThrow(ExecutionException.class);
        //When
        exchange.onComplete(f);
        //Then
        verify(listener).onConnectException(any(RuntimeException.class), eq(new Message[] { message }));
        verify(watcher).stop();
    }

    @Test
    public void shouldNotfyAboutFailWhenResponseStatusIsNotOk() throws InterruptedException, ExecutionException {
        //Given
        Future<ClientResponse> f = mock(Future.class);
        when(f.get()).thenReturn(response);
        when(response.getClientResponseStatus()).thenReturn(ClientResponse.Status.FORBIDDEN);
        //When
        exchange.onComplete(f);
        exchange.run();
        //Then
        verify(listener).onException(any(TransportException.class), eq(new Message[] { message }));
        verify(watcher).stop();
    }

    @Test
    public void shouldNotifyAboutHeartBeatsWhenRecivedFromServer() throws InterruptedException, ExecutionException {
        //Given
        Future<ClientResponse> f = mock(Future.class);
        when(f.get()).thenReturn(response);
        when(response.getClientResponseStatus()).thenReturn(ClientResponse.Status.OK);
        when(response.getEntityInputStream()).thenReturn(new ByteArrayInputStream(new byte[] { Ascii.SPACE, Ascii.SPACE, Ascii.BEL }));
        //When
        exchange.onComplete(f);
        exchange.run();
        //Then
        verify(watcher, times(2)).heatBeat();
    }
    
    @Test
    public void shouldNotPassRecivedContentAsMessagesToListnerWhenResponseIsEmptyString() throws InterruptedException, ExecutionException {
        //Given
        //Given
        Future<ClientResponse> f = mock(Future.class);
        when(f.get()).thenReturn(response);
        when(response.getClientResponseStatus()).thenReturn(ClientResponse.Status.OK);
        when(response.getEntityInputStream()).thenReturn(new ByteArrayInputStream(new byte[] { Ascii.SPACE, Ascii.SPACE, Ascii.BEL }));
        when(response.getEntity(String.class)).thenReturn("");
        //When
        exchange.onComplete(f);
        exchange.run();
        //Then
        verify(listener,never()).onMessages(Mockito.anyList());
        verify(listener).onException(any(TransportException.class), eq(new Message[] { message }));
    }
    @Test
    public void shouldPassRecivedContentAsMessagesToListnerWhenResponseIsNotEmptyString() throws InterruptedException, ExecutionException, ParseException {
        //Given
        Future<ClientResponse> f = mock(Future.class);
        when(f.get()).thenReturn(response);
        when(response.getClientResponseStatus()).thenReturn(ClientResponse.Status.OK);
        when(response.getEntityInputStream()).thenReturn(new ByteArrayInputStream(new byte[] { Ascii.SPACE, Ascii.SPACE, Ascii.BEL }));
        when(response.getEntity(String.class)).thenReturn("non_empty_content");
        final ImmutableList<Mutable> messages = ImmutableList.of(mock(Mutable.class));
        when(transport.parseMessages("non_empty_content")).thenReturn(messages);
        //When
        exchange.onComplete(f);
        exchange.run();
        //Then
        verify(listener).onMessages(messages);
    }
}
