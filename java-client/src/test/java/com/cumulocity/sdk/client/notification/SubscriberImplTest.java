package com.cumulocity.sdk.client.notification;

import static org.junit.Assert.fail;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Map;

import javax.management.NotificationListener;

import org.cometd.bayeux.client.ClientSessionChannel;
import org.cometd.client.BayeuxClient;
import org.cometd.client.BayeuxClient.State;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;


@RunWith(MockitoJUnitRunner.class)
public class SubscriberImplTest {

    @Mock
    BayeuxClient client;

    @Mock
    SubscriptionNameResolver<Object> subscriptionNameResolver;

    @Mock
    BayeuxClientProvider bayeuxClientProvider;

    Subscriber<Object> subscriber;
    
    @Mock
    SubscriptionListener<Object> listener; 

    @Before
    public void setup() {
        subscriber = new SubscriberImpl<Object>("", subscriptionNameResolver, bayeuxClientProvider);
        mockClientProvider();
    }

    private void mockClientProvider() {
        Mockito.when(bayeuxClientProvider.get(Mockito.anyString())).thenReturn(client);
    }

    @Test
    public final void shouldStartSuccessfuly() {
        //Given
        clientCanConnect();
        //When
        subscriber.start();
        //Then
        verify(client).handshake();
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
    public final void shouldSuccesfulySubscribeWhenConnected() {
        //Given
        final Object objectToSubscribe = new Object();
        subsciberStarted();
        final ClientSessionChannel channel = mockChannel(objectToSubscribe);
        //when
        subscriber.subscribe(objectToSubscribe, listener);
        //Then
        verify(channel).subscribe(Mockito.any(MessageListenerAdapter.class));
    }
    
    
    
    
    @Test(expected=IllegalStateException.class)
    public final void shouldFailDisconnectWithIllegalStateExceptionWhenNotConnected() {
        //Given
        //when
        subscriber.stop();
    }
    
    
    
    @Test
    public final void shouldSuccesfulySubscribeAndMakeHandshake() {
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
    public final void shouldSuccesfulyUnSubscribeAfterSubscribe() {
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
    public final void shouldSuccesfulyDisconnectOnStop(){
        //Given
        subsciberStarted();
        clientCanDisconnect();
        //When
        subscriber.stop();
        //Then
        verifyDisconnected();
    }
    
    private void clientCanConnect() {
        when(client.waitFor(Mockito.anyLong(), Mockito.eq(State.CONNECTED))).thenReturn(true);
    }
    private void clientCanDisconnect() {
        when(client.waitFor(Mockito.anyLong(), Mockito.eq(State.DISCONNECTED))).thenReturn(true);
        when(client.disconnect(Mockito.anyLong())).thenReturn(true);
    }

    private void verifyUnsubscribe(final ClientSessionChannel channel) {
        verify(channel).unsubscribe(Mockito.any(MessageListenerAdapter.class));
    }

    private void verifySubscribed(final ClientSessionChannel channel) {
        verify(channel).subscribe(Mockito.any(MessageListenerAdapter.class));
    }

    private void verifyDisconnected() {
        verify(client).disconnect(Mockito.anyLong());
    }

    private void verifyConnected() {
        verify(client).handshake();
    }

    private ClientSessionChannel mockChannel(Object objectToSubscribe) {
        final String id = objectToSubscribe.toString();
        when(subscriptionNameResolver.apply(objectToSubscribe)).thenReturn(id);
        ClientSessionChannel channel = Mockito.mock(ClientSessionChannel.class);
        when(client.getChannel(id)).thenReturn(channel); 
        return channel;
    }

    private void subsciberStarted() {
        clientCanConnect();
        subscriber.start();
    }


   

}


