package com.cumulocity.sdk.client.notification;

import static org.junit.Assert.fail;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Map;

import javax.management.NotificationListener;

import org.cometd.bayeux.Message;
import org.cometd.bayeux.client.ClientSession;
import org.cometd.bayeux.client.ClientSessionChannel;
import org.cometd.client.BayeuxClient;
import org.cometd.client.BayeuxClient.State;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.cumulocity.sdk.client.SDKException;

@RunWith(MockitoJUnitRunner.class)
public class SubscriberImplTest {

    @Mock
    ClientSession client;

    @Mock
    SubscriptionNameResolver<Object> subscriptionNameResolver;

    @Mock
    BayeuxSessionProvider bayeuxSessionProvider;

    Subscriber<Object, Message> subscriber;

    @Mock
    SubscriptionListener<Object, Message> listener;

    @Before
    public void setup() throws SDKException {
        subscriber = new SubscriberImpl<Object>(subscriptionNameResolver, bayeuxSessionProvider);
        mockClientProvider();
    }

    private void mockClientProvider() throws SDKException {
        Mockito.when(bayeuxSessionProvider.get()).thenReturn(client);
    }

    @Test
    public final void shouldStartSuccessfuly() throws SDKException {
        //Given
        //When
        subscriber.start();
        //Then
        verifyConnected();
    }

    @Test(expected = IllegalArgumentException.class)
    public final void shouldFailSubscribeWhenSubscriptionObjectIsNull() {
        //Given
        //when
        subscriber.subscribe(null, listener);
    }

    @Test(expected = IllegalArgumentException.class)
    public final void shouldFailSubscribeWhenNotificationListenerIsNull() {
        //Given
        //when
        subscriber.subscribe(new Object(), null);
    }

    @Test(expected = IllegalStateException.class)
    public final void shouldFailSubscribeWhenNotConnected() {
        //Given
        //when
        subscriber.subscribe(new Object(), listener);
    }

    @Test
    public final void shouldSuccesfulySubscribeWhenConnected() throws SDKException {
        //Given
        final Object objectToSubscribe = new Object();
        subsciberStarted();
        final ClientSessionChannel channel = mockChannel(objectToSubscribe);
        //when
        subscriber.subscribe(objectToSubscribe, listener);
        //Then
        verify(channel).subscribe(Mockito.any(MessageListenerAdapter.class));
    }

    @Test(expected = IllegalStateException.class)
    public final void shouldFailDisconnectWithIllegalStateExceptionWhenNotConnected() {
        //Given
        //when
        subscriber.stop();
    }

    @Test
    public final void shouldSuccesfulySubscribeAndMakeHandshake() throws SDKException {
        //Given
        final Object objectToSubscribe = new Object();
        final ClientSessionChannel channel = mockChannel(objectToSubscribe);
        subsciberStarted();
        //when
        subscriber.subscribe(objectToSubscribe, listener);
        verifyConnected();
        verifySubscribed(channel);
    }

    @Test
    public final void shouldSuccesfulyUnSubscribeAfterSubscribe() throws SDKException {
        //Given
        final Object objectToSubscribe = new Object();
        final ClientSessionChannel channel = mockChannel(objectToSubscribe);
        subsciberStarted();
        //when
        final Subscription<Object> subscription = subscriber.subscribe(objectToSubscribe, listener);
        subscription.unsubscribe();
        //Then
        verifyConnected();
        verifySubscribed(channel);
        verifyUnsubscribe(channel);
    }

    @Test
    public final void shouldSuccesfulyDisconnectOnStop() throws SDKException {
        //Given
        subsciberStarted();
        //When
        subscriber.stop();
        //Then
        verifyDisconnected();
    }

    private void verifyUnsubscribe(final ClientSessionChannel channel) {
        verify(channel).unsubscribe(Mockito.any(MessageListenerAdapter.class));
    }

    private void verifySubscribed(final ClientSessionChannel channel) {
        verify(channel).subscribe(Mockito.any(MessageListenerAdapter.class));
    }

    private void verifyDisconnected() {
        verify(client).disconnect();
    }

    private void verifyConnected() throws SDKException {
        verify(bayeuxSessionProvider).get();
    }

    private ClientSessionChannel mockChannel(Object objectToSubscribe) {
        final String id = objectToSubscribe.toString();
        when(subscriptionNameResolver.apply(objectToSubscribe)).thenReturn(id);
        ClientSessionChannel channel = Mockito.mock(ClientSessionChannel.class);
        when(client.getChannel(id)).thenReturn(channel);
        return channel;
    }

    private void subsciberStarted() throws SDKException {
        subscriber.start();
    }

}
