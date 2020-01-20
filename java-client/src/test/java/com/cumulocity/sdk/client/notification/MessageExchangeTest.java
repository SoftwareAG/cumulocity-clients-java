package com.cumulocity.sdk.client.notification;

import com.cumulocity.common.notification.ClientSvensonJSONContext;
import com.cumulocity.rest.representation.operation.OperationRepresentation;
import com.google.common.base.Ascii;
import com.google.common.collect.ImmutableList;
import com.sun.jersey.api.client.AsyncWebResource;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientRequest;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.async.FutureListener;
import org.cometd.bayeux.Message.Mutable;
import org.cometd.client.transport.TransportListener;
import org.cometd.common.TransportException;
import org.hamcrest.collection.IsCollectionWithSize;
import org.hamcrest.collection.IsIterableWithSize;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.hamcrest.MockitoHamcrest;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

import java.io.ByteArrayInputStream;
import java.text.ParseException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class MessageExchangeTest {

    private static final String URL = "";

    private static final String MESSAGES = "";

    @Mock
    private CumulocityLongPollingTransport transport;

    @Mock
    private ConnectionHeartBeatWatcher watcher;

    @Mock
    private UnauthorizedConnectionWatcher unauthorizedConnectionWatcher;

    @Mock
    private Mutable message;

    @Mock
    private TransportListener listener;

    @Mock
    private ClientResponse response;

    @Mock
    private ScheduledExecutorService executorService;

    @Mock
    private Client client;

    @Mock
    private AsyncWebResource resource;

    @Mock
    private Future<ClientResponse> request;

    @Captor
    private ArgumentCaptor<FutureListener<ClientResponse>> responseHandler;

    @Captor
    private ArgumentCaptor<Runnable> task;

    private MessageExchange exchange;

    @Before
    public void setup() {
        exchange = new MessageExchange(transport, client, executorService, listener, watcher, unauthorizedConnectionWatcher, Arrays.asList(message));
        exchange.reconnectionWaitingTime = SECONDS.toMillis(1);
        when(client.asyncResource(any(String.class))).thenReturn(resource);
        when(resource.handle(any(ClientRequest.class), responseHandler.capture())).thenReturn(request);
    }

    @After
    public void tearDown() {
        executorService.shutdown();
    }

    @Test
    public void shouldStopWatcherAndNotifyWhenCancel() throws InterruptedException, ExecutionException {
        //Given
        when(request.cancel(anyBoolean())).thenReturn(true);
        //When
        exchange.execute(URL, MESSAGES);
        exchange.cancel();
        //Then
        verify(listener).onFailure(any(RuntimeException.class), eq(Arrays.asList(message)));
        verifyNoMoreInteractions(listener);
        verify(watcher).stop();
    }

    @Test
    public void shouldScheduleTaskWhenReciveReponse() throws InterruptedException, ExecutionException {
        //Given
        when(request.get()).thenReturn(response);
        //When
        recivedResponse();
        //Then
        verify(executorService).submit(any(Runnable.class));
    }

    private void recivedResponse() throws InterruptedException {
        exchange.execute(URL, MESSAGES);
        verify(resource).handle(any(ClientRequest.class), responseHandler.capture());
        responseHandler.getValue().onComplete(request);
    }

    @Test
    public void shouldNotfyAboutFailWhenRequestFailed() throws InterruptedException, ExecutionException {
        //Given
        when(request.get()).thenThrow(ExecutionException.class);
        //When
        recivedResponse();
        //Then
        verify(listener).onFailure(any(ExecutionException.class), eq(Arrays.asList(message)));
        verify(watcher).stop();
    }

    @Test
    public void shouldNotfyAboutFailWhenResponseStatusIsNotOk() throws InterruptedException, ExecutionException {
        //Given
        when(request.get()).thenReturn(response);
        when(response.getClientResponseStatus()).thenReturn(ClientResponse.Status.FORBIDDEN);
        //When
        recivedResponse();
        responseConsumed();
        //Then
        verify(listener).onFailure(any(TransportException.class), eq(Arrays.asList(message)));
        verifyNoMoreInteractions(listener);
        verify(watcher).stop();
    }

    @Test
    public void shouldNotifyAboutHeartBeatsWhenRecivedFromServer() throws InterruptedException, ExecutionException {
        //Given
        when(request.get()).thenReturn(response);
        when(response.getClientResponseStatus()).thenReturn(ClientResponse.Status.OK);
        when(response.getEntityInputStream()).thenReturn(new ByteArrayInputStream(new byte[] { Ascii.SPACE, Ascii.SPACE, Ascii.BEL }));
        //When
        recivedResponse();
        responseConsumed();
        //Then
        verify(watcher, times(2)).heartBeat();
        verify(watcher).stop();
    }

    @Test
    public void shouldNotPassRecivedContentAsMessagesToListnerWhenResponseIsEmptyString() throws InterruptedException, ExecutionException {
        //Given
        //Given
        when(request.get()).thenReturn(response);
        when(response.getClientResponseStatus()).thenReturn(ClientResponse.Status.OK);
        when(response.getEntityInputStream()).thenReturn(new ByteArrayInputStream(new byte[] { Ascii.SPACE, Ascii.SPACE, Ascii.BEL }));
        when(response.getEntity(String.class)).thenReturn("");
        //When
        recivedResponse();
        responseConsumed();
        //Then
        verify(listener, never()).onMessages(ArgumentMatchers.anyListOf(Mutable.class));
        verify(listener).onFailure(any(TransportException.class), eq(Arrays.asList(message)));
        verifyNoMoreInteractions(listener);
        verify(watcher).stop();
    }

    private void responseConsumed() {
        verify(executorService).submit(task.capture());
        task.getValue().run();
    }

    @Test
    public void shouldPassRecivedContentAsMessagesToListnerWhenResponseIsNotEmptyString() throws InterruptedException, ExecutionException,
            ParseException {
        //Given
        when(request.get()).thenReturn(response);
        when(response.getClientResponseStatus()).thenReturn(ClientResponse.Status.OK);
        when(response.getEntityInputStream()).thenReturn(new ByteArrayInputStream(new byte[] { Ascii.SPACE, Ascii.SPACE, Ascii.BEL }));
        when(response.getEntity(String.class)).thenReturn("non_empty_content");
        final ImmutableList<Mutable> messages = ImmutableList.of(mock(Mutable.class));
        when(transport.parseMessages("non_empty_content")).thenReturn(messages);
        //When
        recivedResponse();
        responseConsumed();
        //Then
        verify(listener).onMessages(messages);
        verifyNoMoreInteractions(listener);
        verify(watcher).stop();
    }

    @Test
    public void shouldRetryParsingMessageWhenSingleJsonElementIsBroken() throws ExecutionException, InterruptedException, ParseException {
        //Given
        when(request.get()).thenReturn(response);
        when(response.getClientResponseStatus()).thenReturn(ClientResponse.Status.OK);
        when(response.getEntityInputStream()).thenReturn(new ByteArrayInputStream(new byte[] { Ascii.SPACE, Ascii.SPACE, Ascii.BEL }));
        String invalidMessage = "[{\"data\":{\"creationTime\":\"2019-11-07T10:44:07.287Z\",\"deviceId\":\"564\",\"deviceName\":\"Linux MAC CA55B6FBB6F5\",\"self\":\"http://cumulocity.default.svc.cluster.local/devicecontrol/operations/713\",\"id\":\"713\",\n" +
                "\"status\":\"PENDING\",\"description\":\"Opening remote access tunnel to 'SSH1'\",\"c8y_Availability\": { \"status\": \"xxx\"}},\n" +
                "\"channel\":\"/564\",\"id\":\"462\"},{\"ext\":{\"ack\":3},\"channel\":\"/meta/connect\",\"id\":\"5\",\"successful\":true}]";
        when(response.getEntity(String.class)).thenReturn(invalidMessage);
        when(transport.parseMessages(any(String.class))).thenAnswer(new Answer<List<Mutable>>() {
            @Override
            public List<Mutable> answer(InvocationOnMock invocation) throws Throwable {
                String content = invocation.getArgument(0);
                return Arrays.asList(new ClientSvensonJSONContext(OperationRepresentation.class).parse(content));
            }
        });
        //When
        recivedResponse();
        responseConsumed();
        //Then
        verify(listener).onMessages((List<Mutable>)MockitoHamcrest.argThat(IsIterableWithSize.<Mutable>iterableWithSize(1)));
        verifyNoMoreInteractions(listener);
        verify(watcher).stop();
    }


}
