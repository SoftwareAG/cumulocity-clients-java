package com.cumulocity.sdk.client.notification;

import com.cumulocity.rest.representation.BaseResourceRepresentation;
import com.cumulocity.sdk.client.notification.MessageExchange.ResponseHandler;
import com.cumulocity.sdk.client.rest.providers.CumulocityJSONMessageBodyReader;
import org.cometd.bayeux.Message;
import org.cometd.client.transport.TransportListener;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.mockito.ArgumentMatchers;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import javax.ws.rs.client.*;
import javax.ws.rs.core.Response;

import java.io.IOException;
import java.io.InputStream;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import java.util.concurrent.FutureTask;
import java.util.concurrent.ScheduledExecutorService;

import static com.google.common.base.Throwables.propagate;
import static com.google.common.util.concurrent.Callables.returning;
import static java.util.concurrent.Executors.newScheduledThreadPool;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON_TYPE;
import static org.mockito.BDDMockito.given;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


public class MessageExchangeBlockingThreadsTest {

    private final ScheduledExecutorService executorService = newScheduledThreadPool(1);

    @Test
    @Timeout(value = 2000, unit = MILLISECONDS)
    public void shouldNotBlockedThreadWhenTryingToReadResponse() throws Exception {
        //given
        final Client client = mock(Client.class);
        final TransportListener listener = mock(TransportListener.class);
        final MessageExchange messageExchange = new MessageExchange(mock(CumulocityLongPollingTransport.class), client, executorService, listener, mock(ConnectionHeartBeatWatcher.class), mock(UnauthorizedConnectionWatcher.class), Collections.<Message.Mutable>emptyList());

        givenUnfinishedClientResponse(client);

        //when
        messageExchange.execute("", "");
        Thread.sleep(50);
        messageExchange.cancel();

        //then
        executorShouldHaveFreeThread();
        verify(listener).onFailure(any(Throwable.class), any(List.class));
    }

    private void givenUnfinishedClientResponse(final Client client) {
        final WebTarget resource = mock(WebTarget.class);
        final Invocation.Builder builder = mock(Invocation.Builder.class);
        final AsyncInvoker asyncInvoker = mock(AsyncInvoker.class);
        final InputStream blockOnRead = new InputStream() {
            @Override
            public synchronized int read() throws IOException {
                try {
                    wait();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    throw new IOException(e);
                }
                return 0;
            }

            @Override
            public synchronized void close() {
            }
        };

        given(client.target(any(String.class))).willReturn(resource);
        given(resource.request(APPLICATION_JSON_TYPE)).willReturn(builder);
        given(builder.async()).willReturn(asyncInvoker);
        given(asyncInvoker.post(any(Entity.class), any(InvocationCallback.class))).will(new Answer<Future<Response>>() {
            @Override
            public Future<Response> answer(final InvocationOnMock invocationOnMock) throws Throwable {
                final Response response = spy(Response.ok().build());

                doAnswer(invocation -> {
                    final CumulocityJSONMessageBodyReader reader = new CumulocityJSONMessageBodyReader();
                    return reader.readFrom(BaseResourceRepresentation.class, null, null, null, null,blockOnRead);
                    }).when(response)
                        .readEntity(ArgumentMatchers.<Class<?>>any());

                final FutureTask<Response> futureTask = new FutureTask<Response>(returning(response)) {
                    protected void done() {
                        try {
                            final ResponseHandler responseHandler = (ResponseHandler) invocationOnMock.getArguments()[1];
                            responseHandler.completed(this.get());
                        } catch (InterruptedException | ExecutionException e) {
                            propagate(e);
                        }
                    }
                };
                executorService.submit(futureTask).get();
                return futureTask;
            }
        });
    }

    private void executorShouldHaveFreeThread() throws Exception {
        final Future<String> task = executorService.submit(() -> "OK");
        Assertions.assertThat(task.get(500, MILLISECONDS)).isEqualTo("OK");
    }
}
